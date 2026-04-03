package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class OgnlTool extends AbstractJADTool {

    @Tool(
        name = "ognl",
        description = "OGNL :  OGNL ， JAD  ognl 。"
    )
    public String ognl(
            @ToolParam(description = "OGNL ")
            String expression,

            @ToolParam(description = "ClassLoaderhashcode（16），ClassLoader", required = false)
            String classLoaderHash,

            @ToolParam(description = "ClassLoader，sun.misc.Launcher$AppClassLoader，hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = " (-x)， 1", required = false)
            Integer expandLevel,

            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("ognl");

        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }

        if (expandLevel != null && expandLevel > 0) {
            cmd.append(" -x ").append(expandLevel);
        }

        addParameter(cmd, expression);

        return executeSync(toolContext, cmd.toString());
    }
}
