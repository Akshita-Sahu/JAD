package com.akshita.jad.mcp.server.session;

import com.akshita.jad.mcp.server.CommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for MCP-to-Command session bindings.
 * Handles the lifecycle of command sessions associated with MCP sessions.
 */
public class JADCommandSessionManager {
    private static final Logger logger = LoggerFactory.getLogger(JADCommandSessionManager.class);
    
    // JAD  session  30 ，
    // ， session ，
    private static final long SESSION_EXPIRY_THRESHOLD_MS = 25 * 60 * 1000; // 25 
    
    private final CommandExecutor commandExecutor;
    private final ConcurrentHashMap<String, CommandSessionBinding> sessionBindings = new ConcurrentHashMap<>();

    public JADCommandSessionManager(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public static class CommandSessionBinding {
        private final String mcpSessionId;
        private final String jadSessionId;
        private final String consumerId;
        private final long createdTime;
        private volatile long lastAccessTime;
        
        public CommandSessionBinding(String mcpSessionId, String jadSessionId, String consumerId) {
            this.mcpSessionId = mcpSessionId;
            this.jadSessionId = jadSessionId;
            this.consumerId = consumerId;
            this.createdTime = System.currentTimeMillis();
            this.lastAccessTime = this.createdTime;
        }
        
        public String getMcpSessionId() {
            return mcpSessionId;
        }
        
        public String getJADSessionId() {
            return jadSessionId;
        }
        
        public String getConsumerId() {
            return consumerId;
        }
        
        public long getCreatedTime() {
            return createdTime;
        }
        
        public long getLastAccessTime() {
            return lastAccessTime;
        }
        
        public void updateAccessTime() {
            this.lastAccessTime = System.currentTimeMillis();
        }
    }

    public CommandSessionBinding createCommandSession(String mcpSessionId) {
        Map<String, Object> result = commandExecutor.createSession();
        
        CommandSessionBinding binding = new CommandSessionBinding(
            mcpSessionId,
            (String) result.get("sessionId"),
            (String) result.get("consumerId")
        );

        return binding;
    }

    /**
     * session，
     * 
     * @param mcpSessionId MCP session ID
     * @param authSubject ，null
     * @return CommandSessionBinding
     */
    public CommandSessionBinding getCommandSession(String mcpSessionId, Object authSubject) {
        CommandSessionBinding binding = sessionBindings.get(mcpSessionId);

        if (binding == null) {
            binding = createCommandSession(mcpSessionId);
            sessionBindings.put(mcpSessionId, binding);
            logger.debug("Created new command session: MCP={}, JAD={}", mcpSessionId, binding.getJADSessionId());
        } else if (!isSessionValid(binding)) {
            logger.info("Session expired, recreating: MCP={}, JAD={}", mcpSessionId, binding.getJADSessionId());

            try {
                commandExecutor.closeSession(binding.getJADSessionId());
            } catch (Exception e) {
                logger.debug("Failed to close expired session (may already be cleaned up): {}", e.getMessage());
            }

            CommandSessionBinding newBinding = createCommandSession(mcpSessionId);
            sessionBindings.put(mcpSessionId, newBinding);
            logger.info("Recreated command session: MCP={}, Old JAD={}, New JAD={}", 
                       mcpSessionId, binding.getJADSessionId(), newBinding.getJADSessionId());
            binding = newBinding;
        } else {
            logger.debug("Using existing valid session: MCP={}, JAD={}", mcpSessionId, binding.getJADSessionId());
        }

        binding.updateAccessTime();

        if (authSubject != null) {
            try {
                commandExecutor.setSessionAuth(binding.getJADSessionId(), authSubject);
                logger.debug("Applied auth to JAD session: MCP={}, JAD={}", 
                           mcpSessionId, binding.getJADSessionId());
            } catch (Exception e) {
                logger.warn("Failed to apply auth to session: MCP={}, JAD={}, error={}", 
                          mcpSessionId, binding.getJADSessionId(), e.getMessage());
            }
        }
        
        return binding;
    }
    
    /**
     * session
     * sessionconsumer
     */
    private boolean isSessionValid(CommandSessionBinding binding) {
        long timeSinceLastAccess = System.currentTimeMillis() - binding.getLastAccessTime();
        
        if (timeSinceLastAccess > SESSION_EXPIRY_THRESHOLD_MS) {
            logger.debug("Session possibly expired (inactive for {} ms): MCP={}, JAD={}", 
                       timeSinceLastAccess, binding.getMcpSessionId(), binding.getJADSessionId());
            return false;
        }
        
        return true;
    }

    public void closeCommandSession(String mcpSessionId) {
        CommandSessionBinding binding = sessionBindings.remove(mcpSessionId);
        if (binding != null) {
            commandExecutor.closeSession(binding.getJADSessionId());
            logger.debug("Closed command session: MCP={}, JAD={}", mcpSessionId, binding.getJADSessionId());
        }
    }

    public void closeAllSessions() {
        sessionBindings.keySet().forEach(this::closeCommandSession);
    }
}
