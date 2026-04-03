package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class ThreadTool extends AbstractJADTool {

    /**
     * thread : 
     * :
     * - threadId:  ID，required=false
     * - topN:  N  (-n)，required=false
     * - blocking:  (-b)，required=false
     * - all:  (--all)，required=false
     */
    @Tool(
        name = "thread",
        description = "Thread : ， JAD  thread 。。"
    )
    public String thread(
            @ToolParam(description = " ID", required = false)
            Long threadId,

            @ToolParam(description = " N  (-n)", required = false)
            Integer topN,

            @ToolParam(description = " (-b)", required = false)
            Boolean blocking,

            @ToolParam(description = " (--all)", required = false)
            Boolean all,

            ToolContext toolContext
    ) {
        StringBuilder cmd = buildCommand("thread");

        addFlag(cmd, "-b", blocking);
        if (topN != null && topN > 0) {
            cmd.append(" -n ").append(topN);
        }
        addFlag(cmd, "--all", all);
        if (threadId != null && threadId > 0) {
            cmd.append(" ").append(threadId);
        }

        logger.info("Executing thread command: {}", cmd.toString());
        return executeSync(toolContext, cmd.toString());
    }
}
