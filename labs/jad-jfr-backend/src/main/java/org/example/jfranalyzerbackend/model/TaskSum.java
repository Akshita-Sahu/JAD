
package org.example.jfranalyzerbackend.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 */
@Setter
@Getter
public class TaskSum extends BaseTaskResult {
    
    /**
     * 
     */
    private long sum;

    /**
     * 
     */
    public TaskSum() {
        super();
    }

    /**
     * 
     * @param task 
     */
    public TaskSum(Task task) {
        super(task);
    }

    @Override
    public long getValue() {
        return sum;
    }

    @Override
    public void setValue(long value) {
        this.sum = value;
    }
}
