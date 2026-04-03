package com.akshita.jad.core.command.express;

/**
 * 
 * Created by vlinux on 15/5/20.
 */
public class ExpressException extends Exception {

    private final String express;

    /**
     * 
     *
     * @param express 
     * @param cause   
     */
    public ExpressException(String express, Throwable cause) {
        super(cause);
        this.express = express;
    }

    /**
     * 
     *
     * @return 
     */
    public String getExpress() {
        return express;
    }
}
