package com.akshita.jad.grpcweb.grpc.service.advisor;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.advisor.AccessPoint;
import com.akshita.jad.core.advisor.Advice;
import com.akshita.jad.core.advisor.AdviceListenerAdapter;
import com.akshita.jad.core.advisor.JADMethod;
import com.akshita.jad.core.command.express.ExpressException;
import com.akshita.jad.core.command.model.MessageModel;
import com.akshita.jad.core.command.model.ObjectVO;
import com.akshita.jad.core.util.LogUtil;
import com.akshita.jad.core.util.ThreadLocalWatch;
import com.akshita.jad.grpcweb.grpc.model.WatchRequestModel;
import com.akshita.jad.grpcweb.grpc.model.WatchResponseModel;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WatchRpcAdviceListener extends AdviceListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WatchRpcAdviceListener.class);
    private final ThreadLocalWatch threadLocalWatch = new ThreadLocalWatch();

    private final AtomicInteger idGenerator = new AtomicInteger(0);

    private Map<Long/*RESULT_ID*/, Object> results = new HashMap<>();
    private WatchRequestModel watchRequestModel;

    private JADStreamObserver jadStreamObserver;

    public WatchRpcAdviceListener(JADStreamObserver jadStreamObserver, boolean verbose) {
        this.jadStreamObserver = jadStreamObserver;
        this.watchRequestModel = (WatchRequestModel) jadStreamObserver.getRequestModel();
        super.setVerbose(verbose);
    }

    public void setJADStreamObserver(JADStreamObserver jadStreamObserver) {
        this.jadStreamObserver = jadStreamObserver;
        this.watchRequestModel = (WatchRequestModel) jadStreamObserver.getRequestModel();
    }

    private boolean isFinish() {
        return watchRequestModel.isFinish() || !watchRequestModel.isBefore() && !watchRequestModel.isException() && !watchRequestModel.isSuccess();
    }

    @Override
    public void before(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args)
            throws Throwable {
        // 
        threadLocalWatch.start();
        if (watchRequestModel.isBefore()) {
            watching(Advice.newForBefore(loader, clazz, method, target, args));
        }
    }

    @Override
    public void afterReturning(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args,
                               Object returnObject) throws Throwable {
        Advice advice = Advice.newForAfterReturning(loader, clazz, method, target, args, returnObject);
        if (watchRequestModel.isSuccess()) {
            watching(advice);
        }
        finishing(advice);
    }

    @Override
    public void afterThrowing(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args,
                              Throwable throwable) {
        Advice advice = Advice.newForAfterThrowing(loader, clazz, method, target, args, throwable);
        if (watchRequestModel.isException()) {
            watching(advice);
        }
        finishing(advice);
    }

    private void finishing(Advice advice) {
        if (isFinish()) {
            watching(advice);
        }
    }

    private void watching(Advice advice) {
        try {
            // 
            System.out.println("************job:  "+ jadStreamObserver.getJobId() + "  rpc watch advice,*****************");
            System.out.println("listener ID: + " + jadStreamObserver.getListener().id());
            System.out.println(": \n" + watchRequestModel.toString());
            System.out.println("###################***************** \n\n");
            double cost = threadLocalWatch.costInMillis();
            boolean conditionResult = isConditionMet(watchRequestModel.getConditionExpress(), advice, cost);
            if (this.isVerbose()) {
                String msg = "Condition express: " + watchRequestModel.getConditionExpress() + " , result: " + conditionResult + "\n";
                jadStreamObserver.appendResult(new MessageModel(msg));
            }
            if (conditionResult) {
                long resultId = idGenerator.incrementAndGet();
                results.put(resultId, advice);
                Object value = getExpressionResult(watchRequestModel.getExpress(), advice, cost);

                WatchResponseModel model = new WatchResponseModel();
                model.setResultId(resultId);
                model.setTs(LocalDateTime.now());
                model.setCost(cost);
                model.setValue(new ObjectVO(value, watchRequestModel.getExpand()));
                model.setSizeLimit(watchRequestModel.getSizeLimit());
                model.setClassName(advice.getClazz().getName());
                model.setMethodName(advice.getMethod().getName());
                if (advice.isBefore()) {
                    model.setAccessPoint(AccessPoint.ACCESS_BEFORE.getKey());
                } else if (advice.isAfterReturning()) {
                    model.setAccessPoint(AccessPoint.ACCESS_AFTER_RETUNING.getKey());
                } else if (advice.isAfterThrowing()) {
                    model.setAccessPoint(AccessPoint.ACCESS_AFTER_THROWING.getKey());
                }
                jadStreamObserver.appendResult(model);
                jadStreamObserver.times().incrementAndGet();
                if (isLimitExceeded(watchRequestModel.getNumberOfLimit(), jadStreamObserver.times().get())) {
                    String msg = "Command execution times exceed limit: " + watchRequestModel.getNumberOfLimit()
                            + ", so command will exit.\n";
                    jadStreamObserver.end();
                }
            }
        } catch (Throwable e) {
            logger.warn("watch failed.", e);
            jadStreamObserver.end(-1, "watch failed, condition is: " + watchRequestModel.getConditionExpress() + ", express is: "
                    + watchRequestModel.getExpress() + ", " + e.getMessage() + ", visit " + LogUtil.loggingFile()
                    + " for more details.");
        }
    }
}
