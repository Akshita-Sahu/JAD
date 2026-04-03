package com.akshita.jad.core.util.affect;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * Created by vlinux on 15/5/21.
 */
public final class RowAffect extends Affect {

    private final AtomicInteger rCnt = new AtomicInteger();

    public RowAffect() {
    }

    public RowAffect(int rCnt) {
        this.rCnt(rCnt);
    }

    /**
     * 
     *
     * @param mc 
     * @return 
     */
    public int rCnt(int mc) {
        return rCnt.addAndGet(mc);
    }

    /**
     * 
     *
     * @return 
     */
    public int rCnt() {
        return rCnt.get();
    }

    @Override
    public String toString() {
        return String.format("Affect(row-cnt:%d) cost in %s ms.",
                rCnt(),
                cost());
    }
}
