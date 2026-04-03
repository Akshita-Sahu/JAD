package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;

public class MemoryTool extends AbstractJADTool {

    @Tool(
        name = "memory",
        description = "Memory :  JVM ， JAD  memory 。"
    )
    public String memory(ToolContext toolContext) {
        return executeSync(toolContext, "memory");
    }
}
