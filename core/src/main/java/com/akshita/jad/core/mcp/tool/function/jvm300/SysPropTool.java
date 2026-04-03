package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class SysPropTool extends AbstractJADTool {

    @Tool(
        name = "sysprop",
        description = "SysProp : ， JAD  sysprop 。"
    )
    public String sysprop(
            @ToolParam(description = "", required = false)
            String propertyName,

            @ToolParam(description = "；，", required = false)
            String propertyValue,

            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("sysprop");
        if (propertyName != null && !propertyName.trim().isEmpty()) {
            cmd.append(" ").append(propertyName.trim());
            if (propertyValue != null && !propertyValue.trim().isEmpty()) {
                cmd.append(" ").append(propertyValue.trim());
            }
        }
        return executeSync(toolContext, cmd.toString());
    }
}
