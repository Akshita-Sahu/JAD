package com.akshita.jad.core.mcp.tool.function.klass100;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

import java.nio.file.Paths;

public class MemoryCompilerTool extends AbstractJADTool {

    public static final String DEFAULT_DUMP_DIR = Paths.get("jad-output").toAbsolutePath().toString();

    @Tool(
            name = "mc",
            description = "Memory Compiler/，.java.class"
    )
    public String mc(
            @ToolParam(description = ".java，，")
            String javaFilePaths,

            @ToolParam(description = "ClassLoaderhashcode（16），ClassLoader", required = false)
            String classLoaderHash,

            @ToolParam(description = "ClassLoader，sun.misc.Launcher$AppClassLoader，hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = "，jad-output", required = false)
            String outputDir,

            ToolContext toolContext) {
        StringBuilder cmd = buildCommand("mc");

        addParameter(cmd, javaFilePaths);

        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }

        if (outputDir != null && !outputDir.trim().isEmpty()) {
            addParameter(cmd, "-d", outputDir);
        } else {
            cmd.append(" -d ").append(DEFAULT_DUMP_DIR);
        }

        return executeSync(toolContext, cmd.toString());
    }
}
