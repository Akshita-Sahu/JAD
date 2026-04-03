package com.akshita.jad.core.distribution;

import com.akshita_sahu.fastjson2.JSON;
import com.akshita.jad.core.command.model.Countable;
import com.akshita.jad.core.command.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 *
 * @author gongdewei 2020/5/18
 */
public class ResultConsumerHelper {

    private static final Logger logger = LoggerFactory.getLogger(ResultConsumerHelper.class);

    private static ConcurrentHashMap<String, List<Field>> modelFieldMap = new ConcurrentHashMap<String, List<Field>>();

    /**
     * item，，Consumer，。
     * ：，
     *
     * @param model
     * @return
     */
    public static int getItemCount(ResultModel model) {
        //Countable，model
        if (model instanceof Countable) {
            return ((Countable) model).size();
        }

        //Model，
        //Field，
        Class modelClass = model.getClass();
        List<Field> fields = modelFieldMap.get(modelClass.getName());
        if (fields == null) {
            fields = new ArrayList<Field>();
            Field[] declaredFields = modelClass.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field field = declaredFields[i];
                Class<?> fieldClass = field.getType();
                //List/Map/Array/Countable，
                if (Collection.class.isAssignableFrom(fieldClass)
                        || Map.class.isAssignableFrom(fieldClass)
                        || Countable.class.isAssignableFrom(fieldClass)
                        || fieldClass.isArray()) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
            List<Field> old_fields = modelFieldMap.putIfAbsent(modelClass.getName(), fields);
            if (old_fields != null) {
                fields = old_fields;
            }
        }

        //Modelitem
        int count = 0;
        try {
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object value = field.get(model);
                if (value != null) {
                    if (value instanceof Collection) {
                        count += ((Collection) value).size();
                    } else if (value.getClass().isArray()) {
                        count += Array.getLength(value);
                    } else if (value instanceof Map) {
                        count += ((Map) value).size();
                    } else if (value instanceof Countable) {
                        count += ((Countable) value).size();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("get item count of result model failed, model: {}", JSON.toJSONString(model), e);
        }

        return count > 0 ? count : 1;
    }

}
