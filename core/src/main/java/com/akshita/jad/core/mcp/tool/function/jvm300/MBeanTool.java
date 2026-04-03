package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class MBeanTool extends AbstractJADTool {

    public static final int DEFAULT_NUMBER_OF_EXECUTIONS = 1;
    public static final int DEFAULT_REFRESH_INTERVAL_MS = 3000;

    /**
     * mbean :  MBean 
     * :
     * - namePattern: MBean ，（ -E）
     * - attributePattern: ，（ -E）
     * - metadata:  (-m)
     * - intervalMs:  (ms) (-i)，required=false
     * - numberOfExecutions:  (-n)， <=0  DEFAULT_NUMBER_OF_EXECUTIONS
     * - regex:  (-E)，required=false
     */
    @Tool(
        name = "mbean",
        description = "MBean :  MBean ， JAD  mbean 。"
    )
    public String mbean(
            @ToolParam(description = "MBean，java.lang:type=GarbageCollector,name=*")
            String namePattern,

            @ToolParam(description = "，CollectionCount", required = false)
            String attributePattern,

            @ToolParam(description = " (-m)", required = false)
            Boolean metadata,

            @ToolParam(description = "，， 3000ms。", required = false)
            Integer intervalMs,

            @ToolParam(description = "， 1。", required = false)
            Integer numberOfExecutions,

            @ToolParam(description = "，，false", required = false)
            Boolean regex,

            ToolContext toolContext
    ) {
        boolean needStreamOutput = (intervalMs != null && intervalMs > 0) || (numberOfExecutions != null && numberOfExecutions > 0);
        
        int interval = getDefaultValue(intervalMs, DEFAULT_REFRESH_INTERVAL_MS);
        int execCount = getDefaultValue(numberOfExecutions, DEFAULT_NUMBER_OF_EXECUTIONS);

        StringBuilder cmd = buildCommand("mbean");

        addFlag(cmd, "-m", metadata);
        addFlag(cmd, "-E", regex);
        
        //  -i  -n 
        if (needStreamOutput && !Boolean.TRUE.equals(metadata)) {
            cmd.append(" -i ").append(interval);
            cmd.append(" -n ").append(execCount);
        }
        
        if (namePattern != null && !namePattern.trim().isEmpty()) {
            cmd.append(" ").append(namePattern.trim());
        }
        if (attributePattern != null && !attributePattern.trim().isEmpty()) {
            cmd.append(" ").append(attributePattern.trim());
        }

        logger.info("Starting mbean execution: {}", cmd.toString());
        return executeSync(toolContext, cmd.toString());
    }
}
