package com.akshita.jad.core.util.affect;

import static java.lang.System.currentTimeMillis;

/**
 * 
 * Created by vlinux on 15/5/21.
 * @author diecui1202 on 2017/10/26
 */
public class Affect {

    private final long start = currentTimeMillis();

    /**
     * (ms)
     *
     * @return (ms)
     */
    public long cost() {
        return currentTimeMillis() - start;
    }

    @Override
    public String toString() {
        return String.format("Affect cost in %s ms.", cost());
    }
}
