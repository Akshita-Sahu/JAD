package com.akshita.jad.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author gongdewei 2020/5/18
 */
public class ResultUtils {

    /**
     * class，className
     * @param classes
     * @param pageSize
     * @param handler
     */
    public static void processClassNames(Collection<Class<?>> classes, int pageSize, PaginationHandler<List<String>> handler) {
        List<String> classNames = new ArrayList<String>(pageSize);
        int segment = 0;
        for (Class aClass : classes) {
            classNames.add(aClass.getName());
            //slice segment
            if(classNames.size() >= pageSize) {
                handler.handle(classNames, segment++);
                classNames = new ArrayList<String>(pageSize);
            }
        }
        //last segment
        if (classNames.size() > 0) {
            handler.handle(classNames, segment++);
        }
    }

    /**
     * 
     * @param <T>
     */
    public interface PaginationHandler<T> {

        /**
         * 
         * @param list
         * @param segment
         * @return  true ， false 
         */
        boolean handle(T list, int segment);
    }
}
