package com.akshita.jad.core.mcp.tool.function.klass100;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class JadTool extends AbstractJADTool {

    @Tool(
            name = "jad",
            description = "，JVMclassbytecodejava"
    )
    public String jad(
            @ToolParam(description = "，java.lang.Stringdemo.MathGame")
            String classPattern,

            @ToolParam(description = "ClassLoaderhashcode（16），ClassLoader", required = false)
            String classLoaderHash,

            @ToolParam(description = "ClassLoader，sun.misc.Launcher$AppClassLoader，hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = "，false", required = false)
            Boolean sourceOnly,

            @ToolParam(description = "，false", required = false)
            Boolean noLineNumber,

            @ToolParam(description = "，，false", required = false)
            Boolean useRegex,

            @ToolParam(description = "dump class，dumplogback.xmllog", required = false)
            String dumpDirectory,

            ToolContext toolContext) {
        
        StringBuilder cmd = buildCommand("jad");

        addParameter(cmd, classPattern);

        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }

        addFlag(cmd, "--source-only", sourceOnly);
        addFlag(cmd, "-E", useRegex);
        
        if (Boolean.TRUE.equals(noLineNumber)) {
            cmd.append(" --lineNumber false");
        }

        addParameter(cmd, "-d", dumpDirectory);
        
        return executeSync(toolContext, cmd.toString());
    }
}
