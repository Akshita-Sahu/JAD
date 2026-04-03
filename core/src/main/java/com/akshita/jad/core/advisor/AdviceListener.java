package com.akshita.jad.core.advisor;

/**
 * <br/>
 * Created by vlinux on 15/5/17.
 */
public interface AdviceListener {

    long id();

    /**
     * <br/>
     * 
     */
    void create();

    /**
     * <br/>
     * 
     */
    void destroy();

    /**
     * 
     *
     * @param clazz      
     * @param methodName 
     * @param methodDesc 
     * @param target     
     *                   ,null
     * @param args       
     * @throws Throwable 
     */
    void before(
            Class<?> clazz, String methodName, String methodDesc,
            Object target, Object[] args) throws Throwable;

    /**
     * 
     *
     * @param clazz        
     * @param methodName   
     * @param methodDesc   
     * @param target       
     *                     ,null
     * @param args         
     * @param returnObject 
     *                     (void),null
     * @throws Throwable 
     */
    void afterReturning(
            Class<?> clazz, String methodName, String methodDesc,
            Object target, Object[] args,
            Object returnObject) throws Throwable;

    /**
     * 
     *
     * @param clazz      
     * @param methodName 
     * @param methodDesc 
     * @param target     
     *                   ,null
     * @param args       
     * @param throwable  
     * @throws Throwable 
     */
    void afterThrowing(
            Class<?> clazz, String methodName, String methodDesc,
            Object target, Object[] args,
            Throwable throwable) throws Throwable;

}
