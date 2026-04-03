package com.akshita.jad.core.command.monitor200;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.advisor.Advice;
import com.akshita.jad.core.advisor.AdviceListenerAdapter;
import com.akshita.jad.core.advisor.JADMethod;
import com.akshita.jad.core.command.model.StackModel;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.LogUtil;
import com.akshita.jad.core.util.ThreadLocalWatch;
import com.akshita.jad.core.util.ThreadUtil;

import java.time.LocalDateTime;

/**
 * @author beiwei30 on 29/11/2016.
 */
public class StackAdviceListener extends AdviceListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(StackAdviceListener.class);

    private final ThreadLocalWatch threadLocalWatch = new ThreadLocalWatch();
    private StackCommand command;
    private CommandProcess process;

    public StackAdviceListener(StackCommand command, CommandProcess process, boolean verbose) {
        this.command = command;
        this.process = process;
        super.setVerbose(verbose);
    }

    @Override
    public void before(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args)
            throws Throwable {
        // 
        threadLocalWatch.start();
    }

    @Override
    public void afterThrowing(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args,
                              Throwable throwable) throws Throwable {
        Advice advice = Advice.newForAfterThrowing(loader, clazz, method, target, args, throwable);
        finishing(advice);
    }

    @Override
    public void afterReturning(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args,
                               Object returnObject) throws Throwable {
        Advice advice = Advice.newForAfterReturning(loader, clazz, method, target, args, returnObject);
        finishing(advice);
    }

    private void finishing(Advice advice) {
        // 
        try {
            double cost = threadLocalWatch.costInMillis();
            boolean conditionResult = isConditionMet(command.getConditionExpress(), advice, cost);
            if (this.isVerbose()) {
                process.write("Condition express: " + command.getConditionExpress() + " , result: " + conditionResult + "\n");
            }
            if (conditionResult) {
                // TODO: concurrency issues for process.write
                StackModel stackModel = ThreadUtil.getThreadStackModel(advice.getLoader(), Thread.currentThread());
                stackModel.setTs(LocalDateTime.now());
                process.appendResult(stackModel);
                process.times().incrementAndGet();
                if (isLimitExceeded(command.getNumberOfLimit(), process.times().get())) {
                    abortProcess(process, command.getNumberOfLimit());
                }
            }
        } catch (Throwable e) {
            logger.warn("stack failed.", e);
            process.end(-1, "stack failed, condition is: " + command.getConditionExpress() + ", " + e.getMessage()
                          + ", visit " + LogUtil.loggingFile() + " for more details.");
        }
    }
}
