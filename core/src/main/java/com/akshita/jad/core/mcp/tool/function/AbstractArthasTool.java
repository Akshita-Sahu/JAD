package com.akshita.jad.core.mcp.tool.function;

import com.akshita.jad.mcp.server.session.JADCommandContext;
import com.akshita.jad.core.mcp.util.McpAuthExtractor;
import com.akshita.jad.mcp.server.protocol.server.McpNettyServerExchange;
import com.akshita.jad.mcp.server.protocol.server.McpTransportContext;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.util.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.akshita.jad.core.mcp.tool.util.McpToolUtils.*;
import static com.akshita.jad.core.mcp.tool.function.StreamableToolUtils.*;

/**
 * JAD
 */
public abstract class AbstractJADTool {
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final int DEFAULT_TIMEOUT_SECONDS = (int) (StreamableToolUtils.DEFAULT_TIMEOUT_MS / 1000);

    private static final long DEFAULT_ASYNC_START_RETRY_INTERVAL_MS = 100L;
    private static final long DEFAULT_ASYNC_START_MAX_WAIT_MS = 3000L;
    
    /**
     * ，
     */
    protected static class ToolExecutionContext {
        private final JADCommandContext commandContext;
        private final McpTransportContext mcpTransportContext;
        private final Object authSubject;
        private final String userId;
        private final McpNettyServerExchange exchange;
        private final String progressToken;
        private final boolean isStreamable;
        
        public ToolExecutionContext(ToolContext toolContext, boolean isStreamable) {
            this.commandContext = (JADCommandContext) toolContext.getContext().get(TOOL_CONTEXT_COMMAND_CONTEXT_KEY);
            this.isStreamable = isStreamable;
            
            //  Exchange ( Stateless  null)
            this.exchange = (McpNettyServerExchange) toolContext.getContext().get(TOOL_CONTEXT_MCP_EXCHANGE_KEY);
            
            //  Progress Token
            Object progressTokenObj = toolContext.getContext().get(PROGRESS_TOKEN);
            this.progressToken = progressTokenObj != null ? String.valueOf(progressTokenObj) : null;
            
            //  Transport Context ( Stateless  null)
            this.mcpTransportContext = (McpTransportContext) toolContext.getContext().get(MCP_TRANSPORT_CONTEXT);
            
            //  Transport Context 
            if (this.mcpTransportContext != null) {
                this.authSubject = mcpTransportContext.get(McpAuthExtractor.MCP_AUTH_SUBJECT_KEY);
                this.userId = (String) mcpTransportContext.get(McpAuthExtractor.MCP_USER_ID_KEY);
            } else {
                this.authSubject = null;
                this.userId = null;
            }
        }
        
        public JADCommandContext getCommandContext() {
            return commandContext;
        }
        
        public McpTransportContext getMcpTransportContext() {
            return mcpTransportContext;
        }
        
        public Object getAuthSubject() {
            return authSubject;
        }
        
        /**
         *  ID
         * @return  ID， null
         */
        public String getUserId() {
            return userId;
        }
        
        public McpNettyServerExchange getExchange() {
            return exchange;
        }
        
        public String getProgressToken() {
            return progressToken;
        }
        
        public boolean isStreamable() {
            return isStreamable;
        }
    }

    protected String executeSync(ToolContext toolContext, String commandStr) {
        try {
            ToolExecutionContext execContext = new ToolExecutionContext(toolContext, false);
            //  userId  executeSync 
            Object result = execContext.getCommandContext().executeSync(
                    commandStr, 
                    execContext.getAuthSubject(),
                    execContext.getUserId()
            );
            return JsonParser.toJson(result);
        } catch (Exception e) {
            logger.error("Error executing sync command: {}", commandStr, e);
            return JsonParser.toJson(createErrorResponse("Error executing command: " + e.getMessage()));
        }
    }

    protected String executeStreamable(ToolContext toolContext, String commandStr, 
                                     Integer expectedResultCount, Integer pollIntervalMs, 
                                     Integer timeoutMs,
                                     String successMessage) {
        ToolExecutionContext execContext = null;
        try {
            execContext = new ToolExecutionContext(toolContext, true);
            
            logger.info("Starting streamable execution: {}", commandStr);

            // Set userId to session before async execution for stat reporting
            if (execContext.getUserId() != null) {
                execContext.getCommandContext().setSessionUserId(execContext.getUserId());
            }

            Map<String, Object> asyncResult = executeAsyncWithRetry(execContext, commandStr, timeoutMs);
            if (!isAsyncExecutionStarted(asyncResult)) {
                String errorMessage = asyncResult != null ? String.valueOf(asyncResult.get("error")) : "unknown error";
                return JsonParser.toJson(createErrorResponse("Failed to start command: " + errorMessage));
            }
            logger.debug("Async execution started: {}", asyncResult);

            Map<String, Object> results = executeAndCollectResults(
                execContext.getExchange(), 
                execContext.getCommandContext(), 
                expectedResultCount, 
                pollIntervalMs, 
                timeoutMs,
                execContext.getProgressToken()
            );
            
            if (results != null) {
                String message = successMessage != null ? successMessage : "Command execution completed successfully";

                if (Boolean.TRUE.equals(results.get("timedOut"))) {
                    Integer count = (Integer) results.get("resultCount");
                    if (count != null && count > 0) {
                        message = "Command execution ended (Timed out). Captured " + count + " results.";
                    } else {
                        message = "Command execution ended (Timed out). No results captured within the time limit.";
                    }
                }
                
                return JsonParser.toJson(createCompletedResponse(message, results));
            } else {
                return JsonParser.toJson(createErrorResponse("Command execution failed due to timeout or error limits exceeded"));
            }
            
        } catch (Exception e) {
            logger.error("Error executing streamable command: {}", commandStr, e);
            return JsonParser.toJson(createErrorResponse("Error executing command: " + e.getMessage()));
        } finally {
            if (execContext != null) {
                try {
                    // ， session  streamable 
                    execContext.getCommandContext().interruptJob();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static boolean isAsyncExecutionStarted(Map<String, Object> asyncResult) {
        if (asyncResult == null) {
            return false;
        }
        Object success = asyncResult.get("success");
        return Boolean.TRUE.equals(success);
    }

    private static boolean isRetryableAsyncStartError(Map<String, Object> asyncResult) {
        if (asyncResult == null) {
            return false;
        }
        Object success = asyncResult.get("success");
        if (Boolean.TRUE.equals(success)) {
            return false;
        }
        Object error = asyncResult.get("error");
        if (error == null) {
            return false;
        }
        String message = String.valueOf(error);
        return message.contains("Another job is running") || message.contains("Another command is executing");
    }

    private static Map<String, Object> executeAsyncWithRetry(ToolExecutionContext execContext, String commandStr, Integer timeoutMs) {
        long maxWaitMs = DEFAULT_ASYNC_START_MAX_WAIT_MS;
        if (timeoutMs != null && timeoutMs > 0) {
            maxWaitMs = Math.min(maxWaitMs, timeoutMs);
        }

        long deadline = System.currentTimeMillis() + maxWaitMs;
        Map<String, Object> asyncResult = null;

        while (System.currentTimeMillis() < deadline) {
            asyncResult = execContext.getCommandContext().executeAsync(commandStr);
            if (isAsyncExecutionStarted(asyncResult)) {
                return asyncResult;
            }

            if (isRetryableAsyncStartError(asyncResult)) {
                try {
                    execContext.getCommandContext().interruptJob();
                } catch (Exception ignored) {
                }
                try {
                    Thread.sleep(DEFAULT_ASYNC_START_RETRY_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return asyncResult;
                }
                continue;
            }

            return asyncResult;
        }

        return asyncResult;
    }

    protected StringBuilder buildCommand(String baseCommand) {
        return new StringBuilder(baseCommand);
    }

    protected void addParameter(StringBuilder cmd, String flag, String value) {
        if (value != null && !value.trim().isEmpty()) {
            cmd.append(" ").append(flag).append(" ").append(value.trim());
        }
    }

    protected void addParameter(StringBuilder cmd, String value) {
        if (value != null && !value.trim().isEmpty()) {
            // Safely quote the value to prevent command injection
            cmd.append(" '").append(value.trim().replace("'", "'\\''")).append("'");
        }
    }

    protected void addFlag(StringBuilder cmd, String flag, Boolean condition) {
        if (Boolean.TRUE.equals(condition)) {
            cmd.append(" ").append(flag);
        }
    }
    
    /**
     * （）
     */
    protected void addQuotedParameter(StringBuilder cmd, String value) {
        if (value != null && !value.trim().isEmpty()) {
            cmd.append(" '").append(value.trim()).append("'");
        }
    }

    protected int getDefaultValue(Integer value, int defaultValue) {
        return (value != null && value > 0) ? value : defaultValue;
    }

    protected String getDefaultValue(String value, String defaultValue) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : defaultValue;
    }
}
