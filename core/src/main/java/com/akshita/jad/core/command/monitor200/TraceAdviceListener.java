package com.akshita.jad.core.command.monitor200;

import com.akshita.jad.core.advisor.InvokeTraceable;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author beiwei30 on 29/11/2016.
 */
public class TraceAdviceListener extends AbstractTraceAdviceListener implements InvokeTraceable {

    /**
     * Constructor
     */
    public TraceAdviceListener(TraceCommand command, CommandProcess process, boolean verbose) {
        super(command, process);
        super.setVerbose(verbose);
    }

    /**
     * trace ，，，，，
     */
    @Override
    public void invokeBeforeTracing(ClassLoader classLoader, String tracingClassName, String tracingMethodName, String tracingMethodDesc, int tracingLineNumber)
            throws Throwable {
        // normalize className later
        threadLocalTraceEntity(classLoader).tree.begin(tracingClassName, tracingMethodName, tracingLineNumber, true);
    }

    @Override
    public void invokeAfterTracing(ClassLoader classLoader, String tracingClassName, String tracingMethodName, String tracingMethodDesc, int tracingLineNumber)
            throws Throwable {
        threadLocalTraceEntity(classLoader).tree.end();
    }

    @Override
    public void invokeThrowTracing(ClassLoader classLoader, String tracingClassName, String tracingMethodName, String tracingMethodDesc, int tracingLineNumber)
            throws Throwable {
        threadLocalTraceEntity(classLoader).tree.end(true);
    }

}
