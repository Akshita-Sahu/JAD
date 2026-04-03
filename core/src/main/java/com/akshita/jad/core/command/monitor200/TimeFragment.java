package com.akshita.jad.core.command.monitor200;

import com.akshita.jad.core.advisor.Advice;

import java.time.LocalDateTime;

/**
 * 
 */
class TimeFragment {

    public TimeFragment(Advice advice, LocalDateTime gmtCreate, double cost) {
        this.advice = advice;
        this.gmtCreate = gmtCreate;
        this.cost = cost;
    }

    private final Advice advice;
    private final LocalDateTime gmtCreate;
    private final double cost;

    public Advice getAdvice() {
        return advice;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public double getCost() {
        return cost;
    }
}
