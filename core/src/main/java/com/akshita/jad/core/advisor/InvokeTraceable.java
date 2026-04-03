package com.akshita.jad.core.advisor;

/**
 * <br/>
 * ，
 * Created by vlinux on 15/5/27.
 */
public interface InvokeTraceable {

    /**
     * 
     *
     * @param tracingClassName  
     * @param tracingMethodName 
     * @param tracingMethodDesc 
     * @param tracingLineNumber 
     * @throws Throwable 
     */
    void invokeBeforeTracing(
            ClassLoader classLoader,
            String tracingClassName,
            String tracingMethodName,
            String tracingMethodDesc,
            int tracingLineNumber) throws Throwable;

    /**
     * 
     *
     * @param tracingClassName  
     * @param tracingMethodName 
     * @param tracingMethodDesc 
     * @param tracingLineNumber 
     * @throws Throwable 
     */
    void invokeThrowTracing(
            ClassLoader classLoader,
            String tracingClassName,
            String tracingMethodName,
            String tracingMethodDesc,
            int tracingLineNumber) throws Throwable;


    /**
     * 
     *
     * @param tracingClassName  
     * @param tracingMethodName 
     * @param tracingMethodDesc 
     * @param tracingLineNumber 
     * @throws Throwable 
     */
    void invokeAfterTracing(
            ClassLoader classLoader,
            String tracingClassName,
            String tracingMethodName,
            String tracingMethodDesc,
            int tracingLineNumber) throws Throwable;


}
