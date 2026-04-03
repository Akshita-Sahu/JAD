package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class VMToolTool extends AbstractJADTool {

    public static final String ACTION_GET_INSTANCES = "getInstances";
    public static final String ACTION_INTERRUPT_THREAD = "interruptThread";

    @Tool(
            name = "vmtool",
            description = ": 、 GC、， JAD  vmtool 。"
    )
    public String vmtool(
            @ToolParam(description = ": getInstances/forceGc/interruptThread ")
            String action,

            @ToolParam(description = "ClassLoaderhashcode（16），ClassLoader", required = false)
            String classLoaderHash,

            @ToolParam(description = "ClassLoader，sun.misc.Launcher$AppClassLoader，hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = "，（getInstances ）", required = false)
            String className,

            @ToolParam(description = " (-l)，getInstances ， 10；<=0 ", required = false)
            Integer limit,

            @ToolParam(description = " (-x)， 1", required = false)
            Integer expandLevel,

            @ToolParam(description = "OGNL ， getInstances  instances  (--express)", required = false)
            String express,

            @ToolParam(description = " ID (-t)，interruptThread ", required = false)
            Long threadId,

            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("vmtool");

        if (action == null || action.trim().isEmpty()) {
            throw new IllegalArgumentException("vmtool: action ");
        }
        cmd.append(" --action ").append(action.trim());

        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }

        if (ACTION_GET_INSTANCES.equals(action.trim())) {
            if (className != null && !className.trim().isEmpty()) {
                addParameter(cmd, "--className", className);
            }
            if (limit != null) {
                cmd.append(" --limit ").append(limit);
            }
            if (expandLevel != null && expandLevel > 0) {
                cmd.append(" -x ").append(expandLevel);
            }
            if (express != null && !express.trim().isEmpty()) {
                addParameter(cmd, "--express", express);
            }
        }

        if (ACTION_INTERRUPT_THREAD.equals(action.trim())) {
            if (threadId != null && threadId > 0) {
                cmd.append(" -t ").append(threadId);
            } else {
                throw new IllegalArgumentException("vmtool interruptThread  ID (threadId)");
            }
        }

        return executeSync(toolContext, cmd.toString());
    }


}
