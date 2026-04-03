package com.akshita.jad.core.mcp.tool.function.klass100;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class DumpClassTool extends AbstractJADTool {

    /**
     * Dump  - JVM
     * class
     */
    @Tool(
            name = "dump",
            description = "JVMclassdump，class"
    )
    public String dump(
            @ToolParam(description = "，java.lang.Stringdemo.MathGame")
            String classPattern,

            @ToolParam(description = "，jad-output", required = false)
            String outputDir,

            @ToolParam(description = "ClassLoaderhashcode（16），ClassLoader", required = false)
            String classLoaderHashcode,

            @ToolParam(description = "ClassLoader，sun.misc.Launcher$AppClassLoader，hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = "，false", required = false)
            Boolean includeInnerClasses,

            @ToolParam(description = "dump，", required = false)
            Integer limit,

            ToolContext toolContext) {
        StringBuilder cmd = buildCommand("dump");

        addParameter(cmd, classPattern);

        addParameter(cmd, "-d", outputDir);

        if (classLoaderHashcode != null && !classLoaderHashcode.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHashcode);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }

        addFlag(cmd, "--include-inner-classes", includeInnerClasses);

        if (limit != null && limit > 0) {
            cmd.append(" --limit ").append(limit);
        }

        return executeSync(toolContext, cmd.toString());
    }
}
