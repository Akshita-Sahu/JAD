package com.akshita.jad.core.mcp.tool.function.basic1000;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

/**
 * Options MCP Tool:  JAD 
 */
public class OptionsTool extends AbstractJADTool {

    @Tool(
        name = "options",
        description = "Options :  JAD ， JAD  options 。\n" +
                ":\n" +
                "- : \n" +
                "-  name: \n" +
                "-  name  value: \n" +
                ":\n" +
                "- unsafe: （ false）\n" +
                "- dump:  dump （ false）\n" +
                "- json-format:  JSON （ false）\n" +
                "- strict: ，（ true）"
    )
    public String options(
            @ToolParam(description = "，: unsafe, dump, json-format, strict ", required = false)
            String name,

            @ToolParam(description = "，", required = false)
            String value,

            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("options");
        if (name != null && !name.trim().isEmpty()) {
            cmd.append(" ").append(name.trim());
            if (value != null && !value.trim().isEmpty()) {
                cmd.append(" ").append(value.trim());
            }
        }
        return executeSync(toolContext, cmd.toString());
    }
}
