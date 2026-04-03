package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class PerfCounterTool extends AbstractJADTool {

    @Tool(
        name = "perfcounter",
        description = "PerfCounter :  JVM Perf Counter ， JAD  perfcounter 。"
    )
    public String perfcounter(
            @ToolParam(description = " (-d)", required = false)
            Boolean detailed,
            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("perfcounter");
        addFlag(cmd, "-d", detailed);
        return executeSync(toolContext, cmd.toString());
    }
}
