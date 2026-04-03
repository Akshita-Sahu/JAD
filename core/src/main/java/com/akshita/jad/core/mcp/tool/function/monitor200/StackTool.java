package com.akshita.jad.core.mcp.tool.function.monitor200;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class StackTool extends AbstractJADTool {

    public static final int DEFAULT_NUMBER_OF_EXECUTIONS = 1;
    public static final int DEFAULT_POLL_INTERVAL_MS = 50;

    /**
     * stack 
     * 
     * :
     * - classPattern: ，
     * - methodPattern: ， 
     * - condition: OGNL，
     * - numberOfExecutions: ，
     * - regex: ，false
     * - excludeClassPattern: 
     */
    @Tool(
        name = "stack",
        description = "Stack : ，。 JAD  stack 。",
        streamable = true
    )
    public String stack(
            @ToolParam(description = "，，demo.MathGame")
            String classPattern,

            @ToolParam(description = "，，primeFactors", required = false)
            String methodPattern,

            @ToolParam(description = "OGNL，，params[0]<0", required = false)
            String condition,

            @ToolParam(description = "，1。", required = false)
            Integer numberOfExecutions,

            @ToolParam(description = "，，false", required = false)
            Boolean regex,

            @ToolParam(description = "，，" + AbstractJADTool.DEFAULT_TIMEOUT_SECONDS +  "。", required = false)
            Integer timeout,

            ToolContext toolContext
    ) {
        int execCount = getDefaultValue(numberOfExecutions, DEFAULT_NUMBER_OF_EXECUTIONS);
        int timeoutSeconds = getDefaultValue(timeout, DEFAULT_TIMEOUT_SECONDS);

        StringBuilder cmd = buildCommand("stack");
        cmd.append(" --timeout ").append(timeoutSeconds);
        cmd.append(" -n ").append(execCount);
        
        addFlag(cmd, "-E", regex);
        addParameter(cmd, classPattern);
        
        if (methodPattern != null && !methodPattern.trim().isEmpty()) {
            cmd.append(" ").append(methodPattern.trim());
        }
        
        addQuotedParameter(cmd, condition);

        return executeStreamable(toolContext, cmd.toString(), execCount, DEFAULT_POLL_INTERVAL_MS, timeoutSeconds * 1000,
                                "Stack execution completed successfully");
    }
}
