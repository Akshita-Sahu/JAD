package com.akshita.jad.core.mcp.tool.function.klass100;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

/**
 * ， JAD  sc 
 *  JVM ，
 */
public class SearchClassTool extends AbstractJADTool {

    @Tool(
            name = "sc",
            description = " JVM 。(*)，（、、、）"
    )
    public String sc(
            @ToolParam(description = "，。 *StringUtils  org.apache.commons.lang.*， '.'  '/'")
            String classPattern,

            @ToolParam(description = "，、、、、。 true", required = false)
            Boolean detail,

            @ToolParam(description = "（）。 detail  true ", required = false)
            Boolean field,

            @ToolParam(description = "。 false（）", required = false)
            Boolean regex,

            @ToolParam(description = " ClassLoader  hashcode（16）， ClassLoader ", required = false)
            String classLoaderHash,

            @ToolParam(description = " ClassLoader ， sun.misc.Launcher$AppClassLoader， hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = " ClassLoader  toString() ，", required = false)
            String classLoaderStr,

            @ToolParam(description = "，。 0", required = false)
            Integer expand,

            @ToolParam(description = "（）。 100，", required = false)
            Integer limit,

            ToolContext toolContext) {

        StringBuilder cmd = buildCommand("sc");

        // 
        boolean showDetail = (detail == null || detail);
        addFlag(cmd, "-d", showDetail);

        // 
        addFlag(cmd, "-f", field);

        // 
        addFlag(cmd, "-E", regex);

        // （， hashcode）
        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        } else if (classLoaderStr != null && !classLoaderStr.trim().isEmpty()) {
            addParameter(cmd, "-cs", classLoaderStr);
        }

        // 
        if (expand != null && expand > 0) {
            addParameter(cmd, "-x", String.valueOf(expand));
        }

        // 
        if (limit != null && limit > 0) {
            addParameter(cmd, "-n", String.valueOf(limit));
        }

        // （）
        addParameter(cmd, classPattern);

        return executeSync(toolContext, cmd.toString());
    }
}
