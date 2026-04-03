package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class VMOptionTool extends AbstractJADTool {

    @Tool(
        name = "vmoption",
        description = "VMOption :  JVM VM options， JAD  vmoption 。"
    )
    public String vmoption(
            @ToolParam(description = "Name of the VM option.", required = false)
            String key,

            @ToolParam(description = "，", required = false)
            String value,

            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("vmoption");
        if (key != null && !key.trim().isEmpty()) {
            cmd.append(" ").append(key.trim());
            if (value != null && !value.trim().isEmpty()) {
                cmd.append(" ").append(value.trim());
            }
        }
        return executeSync(toolContext, cmd.toString());
    }
}
