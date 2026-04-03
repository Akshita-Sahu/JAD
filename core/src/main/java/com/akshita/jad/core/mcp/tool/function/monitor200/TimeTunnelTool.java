package com.akshita.jad.core.mcp.tool.function.monitor200;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class TimeTunnelTool extends AbstractJADTool {

    public static final int DEFAULT_NUMBER_OF_EXECUTIONS = 1;
    public static final int DEFAULT_POLL_INTERVAL_MS = 100;
    public static final int DEFAULT_MAX_MATCH_COUNT = 50;

    /**
     * tt  (TimeTunnel)
     * ，，
     */
    @Tool(
            name = "tt",
            description = "TimeTunnel : ，， JAD  tt 。、、、、、。",
            streamable = true
    )
    public String timeTunnel(
            @ToolParam(description = ": record/t(), list/l(), search/s(), info/i(), replay/p(), delete/d(), deleteAll/da()，record")
            String action,

            @ToolParam(description = "，，demo.MathGame*Test。record", required = false)
            String classPattern,

            @ToolParam(description = "，，primeFactors*method。record", required = false)
            String methodPattern,

            @ToolParam(description = "OGNL，，params[0]<0'params.length==1'", required = false)
            String condition,

            @ToolParam(description = "， 1。（record）", required = false)
            Integer numberOfExecutions,

            @ToolParam(description = "，，false", required = false)
            Boolean regex,

            @ToolParam(description = "，info/replay/delete", required = false)
            Integer index,

            @ToolParam(description = "，search，OGNL'method.name==\"primeFactors\"'", required = false)
            String searchExpression,

            @ToolParam(description = "Class，ClassJVM，50", required = false)
            Integer maxMatchCount,

            @ToolParam(description = "()。 tt -M/--sizeLimit， 10 * 1024 * 1024", required = false)
            Integer sizeLimit,

            @ToolParam(description = "，，" + AbstractJADTool.DEFAULT_TIMEOUT_SECONDS +  "。（record）", required = false)
            Integer timeout,

            ToolContext toolContext
    ) {
        String ttAction = normalizeAction(action);
        int execCount = getDefaultValue(numberOfExecutions, DEFAULT_NUMBER_OF_EXECUTIONS);
        int maxMatch = getDefaultValue(maxMatchCount, DEFAULT_MAX_MATCH_COUNT);
        int timeoutSeconds = getDefaultValue(timeout, DEFAULT_TIMEOUT_SECONDS);

        validateParameters(ttAction, classPattern, methodPattern, index, searchExpression);

        StringBuilder cmd = buildCommand("tt");
        if (sizeLimit != null && sizeLimit > 0) {
            cmd.append(" -M ").append(sizeLimit);
        }

        switch (ttAction) {
            case "record":
                cmd = buildRecordCommand(cmd, classPattern, methodPattern, condition, execCount, maxMatch, regex, timeoutSeconds);
                break;
            case "list":
                cmd = buildListCommand(cmd, searchExpression);
                break;
            case "info":
                cmd.append(" -i ").append(index);
                break;
            case "search":
                cmd.append(" -s '").append(searchExpression.trim()).append("'");
                break;
            case "replay":
                cmd.append(" -i ").append(index).append(" -p");
                break;
            case "delete":
                cmd.append(" -i ").append(index).append(" -d");
                break;
            case "deleteall":
                cmd.append(" --delete-all");
                break;
            default:
                throw new IllegalArgumentException("Unsupported action: " + ttAction +
                        ". Supported actions: record(t), list(l), info(i), search(s), replay(p), delete(d), deleteAll(da)");
        }

        return executeStreamable(toolContext, cmd.toString(), execCount, DEFAULT_POLL_INTERVAL_MS, timeoutSeconds * 1000,
                "TimeTunnel recording completed successfully");
    }

    /**
     * 
     */
    private void validateParameters(String action, String classPattern, String methodPattern, 
                                   Integer index, String searchExpression) {
        switch (action) {
            case "record":
                if (classPattern == null || classPattern.trim().isEmpty()) {
                    throw new IllegalArgumentException("classPattern is required for record operation");
                }
                if (methodPattern == null || methodPattern.trim().isEmpty()) {
                    throw new IllegalArgumentException("methodPattern is required for record operation");
                }
                break;
            case "info":
            case "replay":
                if (index == null) {
                    throw new IllegalArgumentException(action + " operation requires index parameter");
                }
                break;
            case "search":
                if (searchExpression == null || searchExpression.trim().isEmpty()) {
                    throw new IllegalArgumentException("search operation requires searchExpression parameter");
                }
                break;
            case "delete":
                if (index == null) {
                    throw new IllegalArgumentException("delete operation requires index parameter");
                }
                break;
            case "list":
            case "deleteall":
                break;
            default:
                throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }

    /**
     * 
     */
    private String normalizeAction(String action) {
        if (action == null || action.trim().isEmpty()) {
            return "record";
        }

        String normalizedAction = action.trim().toLowerCase();

        switch (normalizedAction) {
            case "record":
            case "r":
            case "-t":
            case "t":
                return "record";

            case "list":
            case "l":
            case "-l":
                return "list";

            case "info":
            case "i":
            case "-i":
                return "info";

            case "search":
            case "s":
            case "-s":
                return "search";

            case "replay":
            case "p":
            case "-p":
                return "replay";

            case "delete":
            case "d":
            case "-d":
                return "delete";

            case "deleteall":
            case "da":
            case "--delete-all":
                return "deleteall";

            default:
                return normalizedAction;
        }
    }

    private StringBuilder buildRecordCommand(StringBuilder cmd, String classPattern, String methodPattern,
                                             String condition, int execCount, int maxMatch, Boolean regex, int timeoutSeconds) {

        cmd.append(" -t");

        cmd.append(" --timeout ").append(timeoutSeconds);
        cmd.append(" -n ").append(execCount);
        cmd.append(" -m ").append(maxMatch);

        if (Boolean.TRUE.equals(regex)) {
            cmd.append(" -E");
        }

        cmd.append(" '").append(classPattern.trim()).append("'");
        cmd.append(" '").append(methodPattern.trim()).append("'");

        if (condition != null && !condition.trim().isEmpty()) {
            cmd.append(" '").append(condition.trim()).append("'");
        }

        return cmd;
    }

    private StringBuilder buildListCommand(StringBuilder cmd, String searchExpression) {
        cmd.append(" -l");
        if (searchExpression != null && !searchExpression.trim().isEmpty()) {
            cmd.append(" '").append(searchExpression.trim()).append("'");
        }
        return cmd;
    }

}
