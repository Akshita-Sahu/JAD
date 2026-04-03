package com.akshita.jad.core.util.metrics;

/**
 * <pre>
 * 。
 * ，5：
 * 267, 457, 635, 894, 1398
 * ：( (457-267) + (635-457) + (894-635) + (1398-894) ) / 4 = 282
 * </pre>
 * 
 * @author hengyunabc 20151218 3:40:26
 *
 */
public class SumRateCounter {

    RateCounter rateCounter;

    Long previous = null;

    public SumRateCounter() {
        rateCounter = new RateCounter();
    }

    public SumRateCounter(int size) {
        rateCounter = new RateCounter(size);
    }

    public int size() {
        return rateCounter.size();
    }

    public void update(long value) {
        if (previous == null) {
            previous = value;
            return;
        }
        rateCounter.update(value - previous);
        previous = value;
    }

    public double rate() {
        return rateCounter.rate();
    }

}
