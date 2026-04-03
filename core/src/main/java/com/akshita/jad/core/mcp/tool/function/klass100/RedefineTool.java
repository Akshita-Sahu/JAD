package com.akshita.jad.core.mcp.tool.function.klass100;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class RedefineTool extends AbstractJADTool {

    @Tool(
            name = "redefine",
            description = "，JVM，，"
    )
    public String redefine(
            @ToolParam(description = ".class，，")
            String classFilePaths,

            @ToolParam(description = "ClassLoaderhashcode（16），ClassLoader", required = false)
            String classLoaderHash,

            @ToolParam(description = "ClassLoaderclass name，sun.misc.Launcher$AppClassLoader，hashcode", required = false)
            String classLoaderClass,

            ToolContext toolContext) {
        StringBuilder cmd = buildCommand("redefine");

        addParameter(cmd, classFilePaths);

        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }

        return executeSync(toolContext, cmd.toString());
    }
}
