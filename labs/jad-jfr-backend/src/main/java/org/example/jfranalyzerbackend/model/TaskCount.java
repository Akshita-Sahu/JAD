
package org.example.jfranalyzerbackend.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 */
@Setter
@Getter
public class TaskCount extends BaseTaskResult {
    
    /**
     * 
     */
    private long count;

    /**
     * 
     */
    public TaskCount() {
        super();
    }

    /**
     * 
     * @param task 
     */
    public TaskCount(Task task) {
        super(task);
    }

    @Override
    public long getValue() {
        return count;
    }

    @Override
    public void setValue(long value) {
        this.count = value;
    }
}
