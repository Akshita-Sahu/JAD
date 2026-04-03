package com.akshita.jad.core.advisor;

import java.util.concurrent.atomic.AtomicLong;

import com.akshita.jad.core.command.express.ExpressException;
import com.akshita.jad.core.command.express.ExpressFactory;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.shell.system.Process;
import com.akshita.jad.core.shell.system.ProcessAware;
import com.akshita.jad.core.util.Constants;
import com.akshita.jad.core.util.StringUtils;

/**
 * 
 * @author hengyunabc 2020-05-20
 *
 */
public abstract class AdviceListenerAdapter implements AdviceListener, ProcessAware {
    private static final  AtomicLong ID_GENERATOR = new AtomicLong(0);
    private Process process;
    private long id = ID_GENERATOR.addAndGet(1);

    private boolean verbose;

    @Override
    public long id() {
        return id;
    }

    @Override
    public void create() {
        // default no-op
    }

    @Override
    public void destroy() {
        // default no-op
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    final public void before(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args)
            throws Throwable {
        before(clazz.getClassLoader(), clazz, new JADMethod(clazz, methodName, methodDesc), target, args);
    }

    @Override
    final public void afterReturning(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args,
            Object returnObject) throws Throwable {
        afterReturning(clazz.getClassLoader(), clazz, new JADMethod(clazz, methodName, methodDesc), target, args,
                returnObject);
    }

    @Override
    final public void afterThrowing(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args,
            Throwable throwable) throws Throwable {
        afterThrowing(clazz.getClassLoader(), clazz, new JADMethod(clazz, methodName, methodDesc), target, args,
                throwable);
    }

    /**
     * 
     *
     * @param loader 
     * @param clazz  
     * @param method 
     * @param target  ,null
     * @param args   
     * @throws Throwable 
     */
    public abstract void before(ClassLoader loader, Class<?> clazz, JADMethod method, Object target, Object[] args)
            throws Throwable;

    /**
     * 
     *
     * @param loader       
     * @param clazz        
     * @param method       
     * @param target        ,null
     * @param args         
     * @param returnObject  (void),null
     * @throws Throwable 
     */
    public abstract void afterReturning(ClassLoader loader, Class<?> clazz, JADMethod method, Object target,
            Object[] args, Object returnObject) throws Throwable;

    /**
     * 
     *
     * @param loader    
     * @param clazz     
     * @param method    
     * @param target     ,null
     * @param args      
     * @param throwable 
     * @throws Throwable 
     */
    public abstract void afterThrowing(ClassLoader loader, Class<?> clazz, JADMethod method, Object target,
            Object[] args, Throwable throwable) throws Throwable;

    /**
     * ，
     * 
     * @param conditionExpress 
     * @param advice           advice
     * @param cost             
     * @return true 
     */
    protected boolean isConditionMet(String conditionExpress, Advice advice, double cost) throws ExpressException {
        return StringUtils.isEmpty(conditionExpress)
                || ExpressFactory.threadLocalExpress(advice).bind(Constants.COST_VARIABLE, cost).is(conditionExpress);
    }

    protected Object getExpressionResult(String express, Advice advice, double cost) throws ExpressException {
        return ExpressFactory.threadLocalExpress(advice).bind(Constants.COST_VARIABLE, cost).get(express);
    }

    /**
     * ，，
     * 
     * @param limit        
     * @param currentTimes 
     * @return true 
     */
    protected boolean isLimitExceeded(int limit, int currentTimes) {
        return currentTimes >= limit;
    }

    /**
     * ，，
     * 
     * @param process the process to be aborted
     * @param limit   the limit to be printed
     */
    protected void abortProcess(CommandProcess process, int limit) {
        process.write("Command execution times exceed limit: " + limit
                + ", so command will exit. You can set it with -n option.\n");
        process.end();
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

}
