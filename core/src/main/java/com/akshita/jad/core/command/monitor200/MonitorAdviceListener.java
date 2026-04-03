package com.akshita.jad.core.command.monitor200;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.advisor.Advice;
import com.akshita.jad.core.advisor.AdviceListenerAdapter;
import com.akshita.jad.core.advisor.JADMethod;
import com.akshita.jad.core.command.express.ExpressException;
import com.akshita.jad.core.command.model.MonitorModel;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.util.ThreadLocalWatch;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static com.akshita.jad.core.util.JADCheckUtils.isEquals;

/**
 * :<br/>
 * <style type="text/css">
 * table, th, td {
 * borders:1px solid #cccccc;
 * borders-collapse:collapse;
 * }
 * </style>
 * <table>
 * <tr>
 * <th></th>
 * <th>(s)</th>
 * <th></th>
 * <th></th>
 * <th></th>
 * <th></th>
 * <th></th>
 * <th>(ms)</th>
 * <th></th>
 * </tr>
 * <tr>
 * <td>2012-11-07 05:00:01</td>
 * <td>120</td>
 * <td>com.akshita_sahu.item.ItemQueryServiceImpl</td>
 * <td>queryItemForDetail</td>
 * <td>1500</td>
 * <td>1000</td>
 * <td>500</td>
 * <td>15</td>
 * <td>30%</td>
 * </tr>
 * <tr>
 * <td>2012-11-07 05:00:01</td>
 * <td>120</td>
 * <td>com.akshita_sahu.item.ItemQueryServiceImpl</td>
 * <td>queryItemById</td>
 * <td>900</td>
 * <td>900</td>
 * <td>0</td>
 * <td>7</td>
 * <td>0%</td>
 * </tr>
 * </table>
 *
 * @author beiwei30 on 28/11/2016.
 */
class MonitorAdviceListener extends AdviceListenerAdapter {
    // 
    private Timer timer;
    private static final Logger logger = LoggerFactory.getLogger(MonitorAdviceListener.class);
    // 
    private ConcurrentHashMap<Key, AtomicReference<MonitorData>> monitorData = new ConcurrentHashMap<Key, AtomicReference<MonitorData>>();
    private final ThreadLocalWatch threadLocalWatch = new ThreadLocalWatch();
    private final ThreadLocal<Boolean> conditionResult = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    };
    private MonitorCommand command;
    private CommandProcess process;

    MonitorAdviceListener(MonitorCommand command, CommandProcess process, boolean verbose) {
        this.command = command;
        this.process = process;
        super.setVerbose(verbose);
    }

    @Override
    public synchronized void create() {
        if (timer == null) {
            timer = new Timer("Timer-for-jad-monitor-" + process.session().getSessionId(), true);
            timer.scheduleAtFixedRate(new MonitorTimer(monitorData, process, command.getNumberOfLimit()),
                    0, command.getCycle() * 1000L);
        }
    }

    @Override
    public synchronized void destroy() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void before(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args)
            throws Throwable {
        threadLocalWatch.start();
        if (!StringUtils.isEmpty(this.command.getConditionExpress()) && command.isBefore()) {
            Advice advice = Advice.newForBefore(loader, clazz, method, target, args);
            long cost = threadLocalWatch.cost();
            this.conditionResult.set(isConditionMet(this.command.getConditionExpress(), advice, cost));
            //(condition-express)
            threadLocalWatch.start();
        }
    }

    @Override
    public void afterReturning(ClassLoader loader, Class<?> clazz, JADMethod method, Object target,
                               Object[] args, Object returnObject) throws Throwable {
        finishing(clazz, method, false, Advice.newForAfterReturning(loader, clazz, method, target, args, returnObject));
    }

    @Override
    public void afterThrowing(ClassLoader loader, Class<?> clazz, JADMethod method, Object target,
                              Object[] args, Throwable throwable) {
        finishing(clazz, method, true, Advice.newForAfterThrowing(loader, clazz, method, target, args, throwable));
    }

    private void finishing(Class<?> clazz, JADMethod method, boolean isThrowing, Advice advice) {
        double cost = threadLocalWatch.costInMillis();

        if (command.isBefore()) {
            if (!this.conditionResult.get()) {
                return;
            }
        } else {
            try {
                //condition-express
                if (!isConditionMet(this.command.getConditionExpress(), advice, cost)) {
                    return;
                }
            } catch (ExpressException e) {
                //condition-express
                logger.warn("monitor execute condition-express failed.", e);
                return;
            }
        }

        final Key key = new Key(clazz.getName(), method.getName());

        while (true) {
            AtomicReference<MonitorData> value = monitorData.get(key);
            if (null == value) {
                monitorData.putIfAbsent(key, new AtomicReference<MonitorData>(new MonitorData()));
                continue;
            }

            while (true) {
                MonitorData oData = value.get();
                MonitorData nData = new MonitorData();
                nData.setCost(oData.getCost() + cost);
                nData.setTimestamp(LocalDateTime.now());
                if (isThrowing) {
                    nData.setFailed(oData.getFailed() + 1);
                    nData.setSuccess(oData.getSuccess());
                } else {
                    nData.setFailed(oData.getFailed());
                    nData.setSuccess(oData.getSuccess() + 1);
                }
                nData.setTotal(oData.getTotal() + 1);
                if (value.compareAndSet(oData, nData)) {
                    break;
                }
            }
            break;
        }
    }

    private class MonitorTimer extends TimerTask {
        private Map<Key, AtomicReference<MonitorData>> monitorData;
        private CommandProcess process;
        private int limit;

        MonitorTimer(Map<Key, AtomicReference<MonitorData>> monitorData, CommandProcess process, int limit) {
            this.monitorData = monitorData;
            this.process = process;
            this.limit = limit;
        }

        @Override
        public void run() {
            if (monitorData.isEmpty()) {
                return;
            }
            // ，，
            if (process.times().getAndIncrement() >= limit) {
                this.cancel();
                abortProcess(process, limit);
                return;
            }

            List<MonitorData> monitorDataList = new ArrayList<MonitorData>(monitorData.size());
            for (Map.Entry<Key, AtomicReference<MonitorData>> entry : monitorData.entrySet()) {
                final AtomicReference<MonitorData> value = entry.getValue();

                MonitorData data;
                while (true) {
                    data = value.get();
                    //swap monitor data to new instance
                    if (value.compareAndSet(data, new MonitorData())) {
                        break;
                    }
                }

                if (null != data) {
                    data.setClassName(entry.getKey().getClassName());
                    data.setMethodName(entry.getKey().getMethodName());
                    monitorDataList.add(data);
                }
            }
            process.appendResult(new MonitorModel(monitorDataList));
        }

    }

    /**
     * Key
     *
     * @author vlinux
     */
    private static class Key {
        private final String className;
        private final String methodName;

        Key(String className, String behaviorName) {
            this.className = className;
            this.methodName = behaviorName;
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }

        @Override
        public int hashCode() {
            return className.hashCode() + methodName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key okey = (Key) obj;
            return isEquals(okey.className, className) && isEquals(okey.methodName, methodName);
        }

    }

}
