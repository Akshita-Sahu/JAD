package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class GetStaticTool extends AbstractJADTool {


    @Tool(
        name = "getstatic",
        description = "GetStatic : ， ClassLoader， OGNL 。 JAD  getstatic 。"
    )
    public String getstatic(
            @ToolParam(description = "ClassLoaderhashcode（16），ClassLoader", required = false)
            String classLoaderHash,

            @ToolParam(description = "ClassLoader，sun.misc.Launcher$AppClassLoader，hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = "，java.lang.Stringdemo.MathGame")
            String className,

            @ToolParam(description = "")
            String fieldName,

            @ToolParam(description = "OGNL ", required = false)
            String ognlExpression,

            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("getstatic");

        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }

        addParameter(cmd, className);
        addParameter(cmd, fieldName);

        if (ognlExpression != null && !ognlExpression.trim().isEmpty()) {
            cmd.append(" ").append(ognlExpression.trim());
        }

        return executeSync(toolContext, cmd.toString());
    }
}
