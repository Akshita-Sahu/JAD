package org.example.jfranalyzerbackend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 */
@Setter
@Getter
public abstract class BaseTaskResult {
    
    /**
     * 
     */
    private Task task;
    
    /**
     * 
     */
    private Map<StackTrace, Long> samples;

    /**
     * 
     * @param task 
     */
    public BaseTaskResult(Task task) {
        this.task = task;
        this.samples = new HashMap<>();
    }

    /**
     * 
     */
    public BaseTaskResult() {
        this.samples = new HashMap<>();
    }

    /**
     * 
     * @param stackTrace 
     * @param value 
     */
    public void combineSampleData(StackTrace stackTrace, long value) {
        if (samples == null) {
            samples = new HashMap<>();
        }
        if (stackTrace == null || value <= 0) {
            return;
        }
        samples.put(stackTrace, samples.containsKey(stackTrace) ? samples.get(stackTrace) + value : value);
    }

    /**
     * （）
     * @return 
     */
    public abstract long getValue();

    /**
     * （）
     * @param value 
     */
    public abstract void setValue(long value);
}
