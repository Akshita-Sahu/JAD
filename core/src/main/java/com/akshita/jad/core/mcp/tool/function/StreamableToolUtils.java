package com.akshita.jad.core.mcp.tool.function;

import com.akshita.jad.core.command.model.*;
import com.akshita.jad.mcp.server.session.JADCommandContext;
import com.akshita.jad.mcp.server.protocol.server.McpNettyServerExchange;
import com.akshita.jad.mcp.server.protocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * 
 * 
 * @author Yeaury
 */
public final class StreamableToolUtils {

    private static final Logger logger = LoggerFactory.getLogger(StreamableToolUtils.class);

    private static final int DEFAULT_POLL_INTERVAL_MS = 100;    // 100ms

    private static final int ERROR_RETRY_INTERVAL_MS = 500;     // 500ms

    public static final long DEFAULT_TIMEOUT_MS = 30000L;      // 30

    private static final int MAX_ERROR_RETRIES = 10;            // 

    public static final int MIN_ALLOW_INPUT_COUNT_TO_COMPLETE = 2;

    private StreamableToolUtils() {
    }

    /**
     * ，
     * 
     * @param exchange MCP，
     * @param commandContext 
     * @param expectedResultCount 
     * @param intervalMs 
     * @param timeoutMs ()
     * @param progressToken 
     * @return Map，null
     */
    public static Map<String, Object> executeAndCollectResults(McpNettyServerExchange exchange, 
                                                             JADCommandContext commandContext, 
                                                             Integer expectedResultCount, Integer intervalMs, 
                                                             Integer timeoutMs,
                                                             String progressToken) {
        List<Object> allResults = new ArrayList<>();
        int errorRetries = 0;
        int allowInputCount = 0;
        int totalResultCount = 0;
        
        //  1/10,
        // 200ms
        int pullIntervalMs = (intervalMs != null && intervalMs > 0) ? intervalMs : DEFAULT_POLL_INTERVAL_MS;
        
        // 
        // ，
        long executionTimeoutMs = (timeoutMs != null && timeoutMs > 0) ? timeoutMs : DEFAULT_TIMEOUT_MS;
        long deadline = System.currentTimeMillis() + executionTimeoutMs;
        boolean timedOut = false;

        try {
            while (System.currentTimeMillis() < deadline) {
                try {
                    Map<String, Object> results = commandContext.pullResults();
                    if (results == null) {
                        Thread.sleep(pullIntervalMs);
                        continue;
                    }
                    errorRetries = 0;

                    // 
                    String errorMessage = checkForErrorMessages(results);
                    if (errorMessage != null) {
                        logger.warn("Command execution failed with error: {}", errorMessage);
                        return createErrorResponseWithResults(errorMessage, allResults, totalResultCount);
                    }

                    Map<String, Object> filteredResults = filterCommandSpecificResults(results);
                    List<Object> currentBatchResults = getCommandSpecificResults(filteredResults);
                    
                    if (currentBatchResults != null && !currentBatchResults.isEmpty()) {
                        allResults.addAll(currentBatchResults);
                        totalResultCount += currentBatchResults.size();
                        logger.debug("Collected {} results, total: {}", currentBatchResults.size(), totalResultCount);

                        if (exchange != null) {
                            sendProgressNotification(exchange, totalResultCount, 
                                                    expectedResultCount != null ? expectedResultCount : totalResultCount, 
                                                    progressToken);
                        }
                    }

                    boolean commandCompleted = checkCommandCompletion(results, allowInputCount);
                    if (commandCompleted) {
                        allowInputCount++;
                    }

                    String jobStatus = (String) results.get("jobStatus");
                    
                    // 
                    // TERMINATED，2，
                    boolean hasExpectedResultCount = (expectedResultCount != null);
                    boolean reachedExpectedResultCount = hasExpectedResultCount && totalResultCount >= expectedResultCount;
                    boolean allowInputCompletion = !hasExpectedResultCount
                            && commandCompleted
                            && allowInputCount >= MIN_ALLOW_INPUT_COUNT_TO_COMPLETE;

                    if ("TERMINATED".equals(jobStatus) || allowInputCompletion || reachedExpectedResultCount) {
                        logger.info("Command completed. Total results collected: {}, Expected: {}", totalResultCount, expectedResultCount);
                        break;
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Command execution interrupted");
                    return null;
                } catch (Exception e) {
                    if (++errorRetries >= MAX_ERROR_RETRIES) {
                        logger.error("Maximum error retries exceeded", e);
                        return null;
                    }
                    
                    try {
                        Thread.sleep(ERROR_RETRY_INTERVAL_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return null;
                    }
                }
            }
            
            // 
            if (System.currentTimeMillis() >= deadline) {
                timedOut = true;
            }

            return createFinalResult(allResults, totalResultCount, timedOut, executionTimeoutMs);
            
        } catch (Exception e) {
            logger.error("Error in command execution", e);
            return null;
        }
    }

    private static boolean checkCommandCompletion(Map<String, Object> results, int currentAllowInputCount) {
        if (results == null) {
            return false;
        }
        
        @SuppressWarnings("unchecked")
        List<Object> resultList = (List<Object>) results.get("results");
        if (resultList == null || resultList.isEmpty()) {
            return false;
        }

        for (Object result : resultList) {
            // Direct type check instead of reflection
            if (result instanceof InputStatusModel) {
                InputStatusModel inputStatusModel = (InputStatusModel) result;
                InputStatus inputStatus = inputStatusModel.getInputStatus();
                if (inputStatus == InputStatus.ALLOW_INPUT) {
                    logger.debug("Command completion detected: ALLOW_INPUT (count: {})", currentAllowInputCount + 1);
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * 
     */
    private static String checkForErrorMessages(Map<String, Object> results) {
        if (results == null) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        List<Object> resultList = (List<Object>) results.get("results");
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }

        for (Object result : resultList) {
            String message = null;
            
            // Direct type checks instead of reflection
            if (result instanceof MessageModel) {
                message = ((MessageModel) result).getMessage();
            } else if (result instanceof EnhancerModel) {
                message = ((EnhancerModel) result).getMessage();
            } else if (result instanceof StatusModel) {
                message = ((StatusModel) result).getMessage();
            } else if (result instanceof CommandRequestModel) {
                message = ((CommandRequestModel) result).getMessage();
            }
            
            if (message != null && isErrorMessage(message)) {
                return message;
            }
        }
        
        return null;
    }
    
    private static boolean isErrorMessage(String message) {
        return message.matches(".*\\b(failed|error|exception)\\b.*") || 
               message.contains("Malformed OGNL expression") || 
               message.contains("ParseException") || 
               message.contains("ExpressionSyntaxException") ||
               message.matches(".*Exception.*") ||
               message.matches(".*Error.*");
    }

    private static Map<String, Object> filterCommandSpecificResults(Map<String, Object> results) {
        if (results == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> filteredResults = new HashMap<>(results);
        @SuppressWarnings("unchecked")
        List<Object> resultList = (List<Object>) results.get("results");
        
        if (resultList == null || resultList.isEmpty()) {
            return filteredResults;
        }
        
        // Filter out auxiliary model types using direct type checks
        List<Object> filteredResultList = resultList.stream()
            .filter(result -> !isAuxiliaryModel(result))
            .collect(Collectors.toList());
        
        filteredResults.put("results", filteredResultList);
        filteredResults.put("resultCount", filteredResultList.size());
        
        return filteredResults;
    }
    
    /**
     * Check if the result is an auxiliary model type that should be filtered out
     */
    private static boolean isAuxiliaryModel(Object result) {
        return result instanceof InputStatusModel
            || result instanceof StatusModel
            || result instanceof WelcomeModel
            || result instanceof MessageModel
            || result instanceof CommandRequestModel
            || result instanceof SessionModel
            || result instanceof EnhancerModel;
    }

    private static List<Object> getCommandSpecificResults(Map<String, Object> filteredResults) {
        if (filteredResults == null) {
            return new ArrayList<>();
        }
        
        @SuppressWarnings("unchecked")
        List<Object> resultList = (List<Object>) filteredResults.get("results");
        return resultList != null ? resultList : new ArrayList<>();
    }

    /**
     * 
     */
    private static void sendProgressNotification(McpNettyServerExchange exchange, int currentResultCount, 
                                               int totalExpected, String progressToken) {
        try {
            if (progressToken != null && !progressToken.trim().isEmpty()) {
                exchange.progressNotification(new McpSchema.ProgressNotification(
                        progressToken,
                        currentResultCount,
                        (double) totalExpected
                )).join();
            }
            
        } catch (Exception e) {
            logger.error("Error sending progress notification", e);
        }
    }

    public static Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", message);
        response.put("status", "error");
        response.put("stage", "final");
        return response;
    }

    public static Map<String, Object> createErrorResponseWithResults(String message, List<Object> collectedResults, int resultCount) {
        Map<String, Object> response = createErrorResponse(message);
        response.put("results", collectedResults != null ? collectedResults : new ArrayList<>());
        response.put("resultCount", resultCount);
        return response;
    }

    private static Map<String, Object> createFinalResult(List<Object> allResults, int totalResultCount, boolean timedOut, long timeoutMs) {
        Map<String, Object> finalResult = new HashMap<>();
        finalResult.put("results", allResults);
        finalResult.put("resultCount", totalResultCount);
        finalResult.put("status", "completed");
        finalResult.put("stage", "final");
        finalResult.put("timedOut", timedOut);
        
        if (timedOut) {
            logger.warn("Command execution timed out after {} ms", timeoutMs);
            finalResult.put("warning", "Command execution timed out after " + timeoutMs + " ms.");
        }
        
        return finalResult;
    }

    public static Map<String, Object> createCompletedResponse(String message, Map<String, Object> results) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "completed");
        response.put("message", message);
        response.put("stage", "final");
        
        if (results != null) {
            response.putAll(results);
        }
        
        return response;
    }
}
