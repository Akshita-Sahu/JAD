package com.akshita.jad.core.mcp.util;

import com.akshita_sahu.fastjson2.filter.ValueFilter;
import com.akshita.jad.core.GlobalOptions;
import com.akshita.jad.core.command.model.ObjectVO;
import com.akshita.jad.core.view.ObjectView;
import com.akshita.jad.mcp.server.util.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mcp specific ObjectVO serialization filter
 */
public class McpObjectVOFilter implements ValueFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(McpObjectVOFilter.class);
    
    private static final McpObjectVOFilter INSTANCE = new McpObjectVOFilter();
    private static volatile boolean registered = false;
    
    /**
     * Register this filter to JsonParser
     */
    public static void register() {
        if (!registered) {
            synchronized (McpObjectVOFilter.class) {
                if (!registered) {
                    JsonParser.registerFilter(INSTANCE);
                    registered = true;
                    logger.debug("McpObjectVOFilter registered to JsonParser");
                }
            }
        }
    }
    
    @Override
    public Object apply(Object object, String name, Object value) {
        if (value == null) {
            return null;
        }
        
        // Direct type check instead of reflection
        if (value instanceof ObjectVO) {
            return handleObjectVO((ObjectVO) value);
        }
        
        return value;
    }

    private Object handleObjectVO(ObjectVO objectVO) {
        try {
            Object innerObject = objectVO.getObject();
            Integer expand = objectVO.getExpand();
            
            if (innerObject == null) {
                return "null";
            }

            if (objectVO.needExpand()) {
                //  GlobalOptions.isUsingJson 
                if (GlobalOptions.isUsingJson) {
                    return drawJsonView(innerObject);
                } else {
                    return drawObjectView(objectVO);
                }
            } else {
                return objectToString(innerObject);
            }
        } catch (Exception e) {
            logger.warn("Failed to handle ObjectVO: {}", e.getMessage());
            return "{\"error\":\"ObjectVO serialization failed\"}";
        }
    }

    /**
     *  ObjectView 
     */
    private String drawObjectView(ObjectVO objectVO) {
        try {
            ObjectView objectView = new ObjectView(objectVO);
            return objectView.draw();
        } catch (Exception e) {
            logger.debug("ObjectView serialization failed, using toString: {}", e.getMessage());
            return objectToString(objectVO.getObject());
        }
    }

    /**
     *  JSON 
     */
    private String drawJsonView(Object object) {
        try {
            return ObjectView.toJsonString(object);
        } catch (Exception e) {
            logger.debug("ObjectView-style serialization failed, using toString: {}", e.getMessage());
            return objectToString(object);
        }
    }

    private String objectToString(Object object) {
        if (object == null) {
            return "null";
        }
        try {
            return object.toString();
        } catch (Exception e) {
            return object.getClass().getSimpleName() + "@" + Integer.toHexString(object.hashCode());
        }
    }
}
