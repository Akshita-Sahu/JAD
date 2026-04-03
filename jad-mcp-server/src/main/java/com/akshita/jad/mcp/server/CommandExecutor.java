package com.akshita.jad.mcp.server;

import java.util.Map;

/**
 * 
 *
 * @author Yeaury 2025/5/26
 */
public interface CommandExecutor {

    default Map<String, Object> executeSync(String commandLine, long timeout) {
        return executeSync(commandLine, timeout, null, null, null);
    }

    default Map<String, Object> executeSync(String commandLine, Object authSubject) {
        return executeSync(commandLine, 30000L, null, authSubject, null);
    }

    /**
     * ， userId
     *
     * @param commandLine 
     * @param authSubject 
     * @param userId  ID，
     * @return 
     */
    default Map<String, Object> executeSync(String commandLine, Object authSubject, String userId) {
        return executeSync(commandLine, 30000L, null, authSubject, userId);
    }

    /**
     * 
     *
     * @param commandLine 
     * @param timeout 
     * @param sessionId session ID，nullsession
     * @param authSubject ，nullsession
     * @param userId  ID，
     * @return 
     */
    Map<String, Object> executeSync(String commandLine, long timeout, String sessionId, Object authSubject, String userId);

    Map<String, Object> executeAsync(String commandLine, String sessionId);

    Map<String, Object> pullResults(String sessionId, String consumerId);

    Map<String, Object> interruptJob(String sessionId);

    Map<String, Object> createSession();

    Map<String, Object> closeSession(String sessionId);

    void setSessionAuth(String sessionId, Object authSubject);

    /**
     *  session  userId
     *
     * @param sessionId session ID
     * @param userId  ID
     */
    void setSessionUserId(String sessionId, String userId);
}

