package com.akshita.jad.core.mcp.tool.function.monitor200;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class MonitorTool extends AbstractJADTool {

    public static final int DEFAULT_NUMBER_OF_EXECUTIONS = 1;
    public static final int DEFAULT_REFRESH_INTERVAL_MS = 3000;
    public static final int DEFAULT_MAX_MATCH_COUNT = 50;

    /**
     * monitor 
     * 
     * :
     * - classPattern: ，
     * - methodPattern: ，
     * - condition: OGNL，
     * - intervalMs: ，3000ms
     * - numberOfExecutions: ，1
     * - regex: ，false
     * - excludeClassPattern: 
     * - maxMatch: ，，50
     */
    @Tool(
        name = "monitor",
        description = "Monitor : ，、、、RT、。 JAD  monitor 。",
        streamable = true
    )
    public String monitor(
            @ToolParam(description = "，，demo.MathGame")
            String classPattern,

            @ToolParam(description = "，，primeFactors", required = false)
            String methodPattern,

            @ToolParam(description = "OGNL，，params[0]<0", required = false)
            String condition,

            @ToolParam(description = "，， 3000ms。", required = false)
            Integer intervalMs,

            @ToolParam(description = "，" + DEFAULT_NUMBER_OF_EXECUTIONS + "。", required = false)
            Integer numberOfExecutions,

            @ToolParam(description = "，，false", required = false)
            Boolean regex,

            @ToolParam(description = "，，50", required = false)
            Integer maxMatch,

            @ToolParam(description = "，，" + AbstractJADTool.DEFAULT_TIMEOUT_SECONDS +  "。", required = false)
            Integer timeout,

            ToolContext toolContext
    ) {
        int interval = getDefaultValue(intervalMs, DEFAULT_REFRESH_INTERVAL_MS);
        int execCount = getDefaultValue(numberOfExecutions, DEFAULT_NUMBER_OF_EXECUTIONS);
        int maxMatchCount = getDefaultValue(maxMatch, DEFAULT_MAX_MATCH_COUNT);
        int timeoutSeconds = getDefaultValue(timeout, DEFAULT_TIMEOUT_SECONDS);

        StringBuilder cmd = buildCommand("monitor");

        cmd.append(" --timeout ").append(timeoutSeconds);
        cmd.append(" -c ").append(interval / 1000);
        cmd.append(" -n ").append(execCount);
        cmd.append(" -m ").append(maxMatchCount);

        addFlag(cmd, "-E", regex);
        addParameter(cmd, classPattern);
        
        if (methodPattern != null && !methodPattern.trim().isEmpty()) {
            cmd.append(" ").append(methodPattern.trim());
        }
        
        addQuotedParameter(cmd, condition);

        return executeStreamable(toolContext, cmd.toString(), execCount, interval / 10, timeoutSeconds * 1000,
                                "Monitor execution completed successfully");
    }
}
