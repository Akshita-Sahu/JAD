
package org.example.jfranalyzerbackend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @param <T> 
 */
@Setter
@Getter
public class DimensionResult<T> {

    /**
     * 
     */
    private List<T> list;

    /**
     * 
     * @param item 
     */
    public void addResultItem(T item) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(item);
    }
}
