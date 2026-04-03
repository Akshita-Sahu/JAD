package com.akshita.jad.core.command.monitor200;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.advisor.Advice;
import com.akshita.jad.core.advisor.AdviceListenerAdapter;
import com.akshita.jad.core.advisor.JADMethod;
import com.akshita.jad.core.command.express.ExpressException;
import com.akshita.jad.core.command.model.TimeFragmentVO;
import com.akshita.jad.core.command.model.TimeTunnelModel;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.LogUtil;
import com.akshita.jad.core.util.ThreadLocalWatch;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author beiwei30 on 30/11/2016.
 * @author hengyunabc 2020-05-20
 */
public class TimeTunnelAdviceListener extends AdviceListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(TimeTunnelAdviceListener.class);
    /**
     *  JDK  Object[]  ring stack（）， JADClassLoader  ObjectStack 
     *  ThreadLocalMap ， stop/detach  JADClassLoader  GC 。
     *
     * <pre>
     * ：
     * - store[0]  int[1]  pos（0..cap）
     * - store[1..cap]  args（Object[]）
     * </pre>
     */
    private static final int ARGS_STACK_SIZE = 512;
    private final ThreadLocal<Object[]> argsRef = ThreadLocal.withInitial(() -> {
        Object[] store = new Object[ARGS_STACK_SIZE + 1];
        store[0] = new int[1];
        return store;
    });

    private TimeTunnelCommand command;
    private CommandProcess process;

    // 
    private volatile boolean isFirst = true;

    // 
    private final ThreadLocalWatch threadLocalWatch = new ThreadLocalWatch();

    public TimeTunnelAdviceListener(TimeTunnelCommand command, CommandProcess process, boolean verbose) {
        this.command = command;
        this.process = process;
        super.setVerbose(verbose);
    }

    @Override
    public void before(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args)
            throws Throwable {
        pushArgs(args);
        threadLocalWatch.start();
    }

    @Override
    public void afterReturning(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args,
                               Object returnObject) throws Throwable {
        // args， args
        args = popArgs();
        afterFinishing(Advice.newForAfterReturning(loader, clazz, method, target, args, returnObject));
    }

    @Override
    public void afterThrowing(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args,
                              Throwable throwable) {
        // args， args
        args = popArgs();
        afterFinishing(Advice.newForAfterThrowing(loader, clazz, method, target, args, throwable));
    }

    private void pushArgs(Object[] args) {
        Object[] store = argsRef.get();
        int[] posHolder = (int[]) store[0];

        int cap = store.length - 1;
        int pos = posHolder[0];
        if (pos < cap) {
            pos++;
        } else {
            // if stack is full, reset pos
            pos = 1;
        }
        store[pos] = args;
        posHolder[0] = pos;
    }

    private Object[] popArgs() {
        Object[] store = argsRef.get();
        int[] posHolder = (int[]) store[0];

        int cap = store.length - 1;
        int pos = posHolder[0];
        if (pos > 0) {
            Object[] args = (Object[]) store[pos];
            store[pos] = null;
            posHolder[0] = pos - 1;
            return args;
        }

        pos = cap;
        Object[] args = (Object[]) store[pos];
        store[pos] = null;
        posHolder[0] = pos - 1;
        return args;
    }

    private void afterFinishing(Advice advice) {
        double cost = threadLocalWatch.costInMillis();
        TimeFragment timeTunnel = new TimeFragment(advice, LocalDateTime.now(), cost);

        boolean match = false;
        try {
            match = isConditionMet(command.getConditionExpress(), advice, cost);
            if (this.isVerbose()) {
                process.write("Condition express: " + command.getConditionExpress() + " , result: " + match + "\n");
            }
        } catch (ExpressException e) {
            logger.warn("tt failed.", e);
            process.end(-1, "tt failed, condition is: " + command.getConditionExpress() + ", " + e.getMessage()
                          + ", visit " + LogUtil.loggingFile() + " for more details.");
        }

        if (!match) {
            return;
        }

        int index = command.putTimeTunnel(timeTunnel);

        TimeFragmentVO timeFragmentVO = TimeTunnelCommand.createTimeFragmentVO(index, timeTunnel, command.getExpand());
        TimeTunnelModel timeTunnelModel = new TimeTunnelModel()
                .setTimeFragmentList(Collections.singletonList(timeFragmentVO))
                .setFirst(isFirst);
        process.appendResult(timeTunnelModel);

        if (isFirst) {
            isFirst = false;
        }

        process.times().incrementAndGet();
        if (isLimitExceeded(command.getNumberOfLimit(), process.times().get())) {
            abortProcess(process, command.getNumberOfLimit());
        }
    }
}
