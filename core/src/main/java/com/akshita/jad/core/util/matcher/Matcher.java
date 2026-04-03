package com.akshita.jad.core.util.matcher;

/**
 * 
 * Created by vlinux on 15/5/17.
 */
public interface Matcher<T> {

    /**
     * 
     *
     * @param target 
     * @return 
     */
    boolean matching(T target);

}
