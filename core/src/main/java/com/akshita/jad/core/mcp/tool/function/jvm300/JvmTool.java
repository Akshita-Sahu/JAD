package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;

public class JvmTool extends AbstractJADTool {

    @Tool(
        name = "jvm",
        description = "Jvm :  JVM 。 JAD  jvm 。"
    )
    public String jvm(ToolContext toolContext) {
        return executeSync(toolContext, "jvm");
    }
}
