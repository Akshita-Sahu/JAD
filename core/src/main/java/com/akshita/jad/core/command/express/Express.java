package com.akshita.jad.core.command.express;

/**
 * 
 * Created by vlinux on 15/5/20.
 */
public interface Express {

    /**
     * 
     *
     * @param express 
     * @return 
     * @throws ExpressException 
     */
    Object get(String express) throws ExpressException;

    /**
     * 
     *
     * @param express 
     * @return 
     * @throws ExpressException 
     */
    boolean is(String express) throws ExpressException;

    /**
     * 
     *
     * @param object 
     * @return this
     */
    Express bind(Object object);

    /**
     * 
     *
     * @param name  
     * @param value 
     * @return this
     */
    Express bind(String name, Object value);

    /**
     * 
     *
     * @return this
     */
    Express reset();


}
