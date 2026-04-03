package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class SysEnvTool extends AbstractJADTool {

    @Tool(
        name = "sysenv",
        description = "SysEnv : ， JAD  sysenv 。"
    )
    public String sysenv(
            @ToolParam(description = "。，；。", required = false)
            String envName,
            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("sysenv");
        if (envName != null && !envName.trim().isEmpty()) {
            cmd.append(" ").append(envName.trim());
        }
        return executeSync(toolContext, cmd.toString());
    }
}
