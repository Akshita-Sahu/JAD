package com.akshita.jad.core.mcp.tool.function.klass100;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

/**
 * ， JAD  sm 
 *  JVM ，
 */
public class SearchMethodTool extends AbstractJADTool {

    @Tool(
            name = "sm",
            description = " JVM 。(*)，（、、、）"
    )
    public String sm(
            @ToolParam(description = "，。 *StringUtils  org.apache.commons.lang.*， '.'  '/'")
            String classPattern,

            @ToolParam(description = "。 get*  *Name。", required = false)
            String methodPattern,

            @ToolParam(description = "，、、、、。 true", required = false)
            Boolean detail,

            @ToolParam(description = "。 false（）", required = false)
            Boolean regex,

            @ToolParam(description = " ClassLoader  hashcode（16）， ClassLoader ", required = false)
            String classLoaderHash,

            @ToolParam(description = " ClassLoader ， sun.misc.Launcher$AppClassLoader， hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = "。 100，", required = false)
            Integer limit,

            ToolContext toolContext) {

        StringBuilder cmd = buildCommand("sm");

        // 
        boolean showDetail = (detail == null || detail);
        addFlag(cmd, "-d", showDetail);

        // 
        addFlag(cmd, "-E", regex);

        // （， hashcode）
        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }

        // 
        if (limit != null && limit > 0) {
            addParameter(cmd, "-n", String.valueOf(limit));
        }

        // （）
        addParameter(cmd, classPattern);

        // （）
        if (methodPattern != null && !methodPattern.trim().isEmpty()) {
            addParameter(cmd, methodPattern);
        }

        return executeSync(toolContext, cmd.toString());
    }
}
