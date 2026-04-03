package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class DashboardTool extends AbstractJADTool {

    public static final int DEFAULT_NUMBER_OF_EXECUTIONS = 3;
    public static final int DEFAULT_REFRESH_INTERVAL_MS = 3000;

    /**
     * dashboard 
     * :
     * - intervalMs: ， ms， 3000ms
     * - count: ， -n ； DEFAULT_NUMBER_OF_EXECUTIONS (3)
     */
    @Tool(
            name = "dashboard",
            description = "Dashboard :  JVM/，。 JAD  dashboard 。",
            streamable = true
    )
    public String dashboard(
            @ToolParam(description = "，， 3000ms。", required = false)
            Integer intervalMs,

            @ToolParam(description = "， 3。", required = false)
            Integer numberOfExecutions,

            ToolContext toolContext
    ) {
        int interval = getDefaultValue(intervalMs, DEFAULT_REFRESH_INTERVAL_MS);
        int execCount = getDefaultValue(numberOfExecutions, DEFAULT_NUMBER_OF_EXECUTIONS);

        StringBuilder cmd = buildCommand("dashboard");
        cmd.append(" -i ").append(interval);
        cmd.append(" -n ").append(execCount);

        // Dashboards typically run a fixed number of times,
        // and the timeout is based on (number * interval) + buffer time
        int calculatedTimeoutMs = execCount * interval + 5000;

        return executeStreamable(toolContext, cmd.toString(), execCount, interval / 10, calculatedTimeoutMs,
                "Dashboard execution completed successfully");
    }
}
