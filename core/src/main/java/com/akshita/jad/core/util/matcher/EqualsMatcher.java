package com.akshita.jad.core.util.matcher;

import com.akshita.jad.core.util.JADCheckUtils;

/**
 * 
 * @author ralf0131 2017-01-06 13:18.
 */
public class EqualsMatcher<T> implements Matcher<T> {

    private final T pattern;

    public EqualsMatcher(T pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matching(T target) {
        return JADCheckUtils.isEquals(target, pattern);
    }
}
