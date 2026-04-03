
package org.example.jfranalyzerbackend.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 */
@Setter
@Getter
public class Task {

    /**
     * 
     */
    private long id;

    /**
     * 
     */
    private String name;

    /**
     * （：，-1）
     */
    private long start = -1;

    /**
     * （：，-1）
     */
    private long end = -1;
}
