package com.akshita.jad.core.mcp.tool.function.monitor200;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class TraceTool extends AbstractJADTool {

    public static final int DEFAULT_NUMBER_OF_EXECUTIONS = 1;
    public static final int DEFAULT_POLL_INTERVAL_MS = 100;
    public static final int DEFAULT_MAX_MATCH_COUNT = 50;

    /**
     * trace 
     * ，
     */
    @Tool(
        name = "trace",
        description = "Trace : ，， JAD  trace 。",
        streamable = true
    )
    public String trace(
            @ToolParam(description = "，，demo.MathGame")
            String classPattern,

            @ToolParam(description = "，，primeFactors", required = false)
            String methodPattern,

            @ToolParam(description = "OGNL，#cost，'#cost>100'100ms", required = false)
            String condition,

            @ToolParam(description = "，1。", required = false)
            Integer numberOfExecutions,

            @ToolParam(description = "，，false", required = false)
            Boolean regex,

            @ToolParam(description = "Class，50", required = false)
            Integer maxMatchCount,

            @ToolParam(description = "，，" + AbstractJADTool.DEFAULT_TIMEOUT_SECONDS +  "。", required = false)
            Integer timeout,

            ToolContext toolContext
    ) {
        int execCount = getDefaultValue(numberOfExecutions, DEFAULT_NUMBER_OF_EXECUTIONS);
        int maxMatch = getDefaultValue(maxMatchCount, DEFAULT_MAX_MATCH_COUNT);
        int timeoutSeconds = getDefaultValue(timeout, DEFAULT_TIMEOUT_SECONDS);

        StringBuilder cmd = buildCommand("trace");

        cmd.append(" --timeout ").append(timeoutSeconds);
        cmd.append(" -n ").append(execCount);
        cmd.append(" -m ").append(maxMatch);

        addFlag(cmd, "-E", regex);

        addParameter(cmd, classPattern);

        if (methodPattern != null && !methodPattern.trim().isEmpty()) {
            cmd.append(" ").append(methodPattern.trim());
        }

        addQuotedParameter(cmd, condition);

        return executeStreamable(toolContext, cmd.toString(), execCount, DEFAULT_POLL_INTERVAL_MS, timeoutSeconds * 1000,
                                "Trace execution completed successfully");
    }
}
