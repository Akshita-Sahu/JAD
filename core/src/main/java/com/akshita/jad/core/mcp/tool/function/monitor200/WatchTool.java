package com.akshita.jad.core.mcp.tool.function.monitor200;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class WatchTool extends AbstractJADTool {

    public static final int DEFAULT_NUMBER_OF_EXECUTIONS = 1;
    public static final int DEFAULT_POLL_INTERVAL_MS = 50;
    public static final int DEFAULT_MAX_MATCH_COUNT = 50;
    public static final int DEFAULT_EXPAND_LEVEL = 1;
    public static final String DEFAULT_EXPRESS = "{params, target, returnObj}";

    /**
     * watch 
     * ，、
     * 
     */
    @Tool(
        name = "watch",
        description = "Watch : ，、，。 JAD  watch 。",
        streamable = true
    )
    public String watch(
            @ToolParam(description = "，，demo.MathGame")
            String classPattern,

            @ToolParam(description = "，，primeFactors", required = false)
            String methodPattern,

            @ToolParam(description = "，{params, target, returnObj}，OGNL", required = false)
            String express,

            @ToolParam(description = "OGNL，，params[0]<0", required = false)
            String condition,

            @ToolParam(description = "(-b)，false", required = false)
            Boolean beforeMethod,

            @ToolParam(description = "(-e)，false", required = false)
            Boolean exceptionOnly,

            @ToolParam(description = "(-s)，false", required = false)
            Boolean successOnly,

            @ToolParam(description = "， 1。", required = false)
            Integer numberOfExecutions,

            @ToolParam(description = "，，false", required = false)
            Boolean regex,

            @ToolParam(description = "Class，50", required = false)
            Integer maxMatchCount,

            @ToolParam(description = "，1，4", required = false)
            Integer expandLevel,

            @ToolParam(description = "()。 watch -M/--sizeLimit， 10 * 1024 * 1024", required = false)
            Integer sizeLimit,

            @ToolParam(description = "，，" + AbstractJADTool.DEFAULT_TIMEOUT_SECONDS +  "。", required = false)
            Integer timeout,

            ToolContext toolContext
    ) {
        int execCount = getDefaultValue(numberOfExecutions, DEFAULT_NUMBER_OF_EXECUTIONS);
        int maxMatch = getDefaultValue(maxMatchCount, DEFAULT_MAX_MATCH_COUNT);
        int expandDepth = (expandLevel != null && expandLevel >= 1 && expandLevel <= 4) ? expandLevel : DEFAULT_EXPAND_LEVEL;
        String watchExpress = getDefaultValue(express, DEFAULT_EXPRESS);
        int timeoutSeconds = getDefaultValue(timeout, DEFAULT_TIMEOUT_SECONDS);

        StringBuilder cmd = buildCommand("watch");

        cmd.append(" --timeout ").append(timeoutSeconds);
        cmd.append(" -n ").append(execCount);
        cmd.append(" -m ").append(maxMatch);
        cmd.append(" -x ").append(expandDepth);
        if (sizeLimit != null && sizeLimit > 0) {
            cmd.append(" -M ").append(sizeLimit);
        }

        addFlag(cmd, "-E", regex);

        if (Boolean.TRUE.equals(beforeMethod)) {
            cmd.append(" -b");
        } else if (Boolean.TRUE.equals(exceptionOnly)) {
            cmd.append(" -e");
        } else if (Boolean.TRUE.equals(successOnly)) {
            cmd.append(" -s");
        } else {
            cmd.append(" -f");
        }

        addParameter(cmd, classPattern);

        if (methodPattern != null && !methodPattern.trim().isEmpty()) {
            cmd.append(" ").append(methodPattern.trim());
        }

        addQuotedParameter(cmd, watchExpress);

        addQuotedParameter(cmd, condition);

        return executeStreamable(toolContext, cmd.toString(), execCount, DEFAULT_POLL_INTERVAL_MS, timeoutSeconds * 1000,
                                "Watch execution completed successfully");
    }
}
