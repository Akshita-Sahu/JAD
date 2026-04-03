package com.akshita.jad.core.mcp.tool.function.basic1000;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.util.JsonParser;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.Map;

import static com.akshita.jad.core.mcp.tool.function.StreamableToolUtils.createCompletedResponse;
import static com.akshita.jad.core.mcp.tool.function.StreamableToolUtils.createErrorResponse;

public class StopTool extends AbstractJADTool {

    public static final int DEFAULT_SHUTDOWN_DELAY_MS = 1000;

    @Tool(
        name = "stop",
        description = " JAD。 tool 。 MCP client ， tool ， stop。"
    )
    public String stop(
            @ToolParam(description = " stop ， 1000ms。 MCP client 。", required = false)
            Integer delayMs,
            ToolContext toolContext) {
        try {
            int shutdownDelayMs = getDefaultValue(delayMs, DEFAULT_SHUTDOWN_DELAY_MS);

            ToolExecutionContext execContext = new ToolExecutionContext(toolContext, false);
            scheduleStop(execContext, shutdownDelayMs);

            Map<String, Object> result = new HashMap<>();
            result.put("command", "stop");
            result.put("scheduled", true);
            result.put("delayMs", shutdownDelayMs);
            result.put("note", "JAD ，MCP 。");
            return JsonParser.toJson(createCompletedResponse("Stop scheduled", result));
        } catch (Exception e) {
            logger.error("Error scheduling stop", e);
            return JsonParser.toJson(createErrorResponse("Error scheduling stop: " + e.getMessage()));
        }
    }

    private void scheduleStop(ToolExecutionContext execContext, int delayMs) {
        Object authSubject = execContext.getAuthSubject();
        String userId = execContext.getUserId();

        Thread shutdownThread = new Thread(() -> {
            try {
                if (delayMs > 0) {
                    Thread.sleep(delayMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            try {
                execContext.getCommandContext().getCommandExecutor()
                        .executeSync("stop", 300000L, null, authSubject, userId);
            } catch (Throwable t) {
                logger.error("Error executing stop command in background thread", t);
            }
        }, "jad-mcp-stop");
        shutdownThread.setDaemon(true);
        shutdownThread.start();
    }
}
