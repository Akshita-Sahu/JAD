package com.akshita.jad.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JAD
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Option {

    /*
     * ，
     */
    int level();

    /*
     * 
     */
    String name();

    /*
     * 
     */
    String summary();

    /*
     * 
     */
    String description();

}