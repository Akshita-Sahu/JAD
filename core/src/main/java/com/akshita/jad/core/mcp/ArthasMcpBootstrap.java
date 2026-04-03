package com.akshita.jad.core.mcp;

import com.akshita.jad.mcp.server.CommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAD MCP Bootstrap class
 *
 * @author Yeaury
 */
public class JADMcpBootstrap {
    private static final Logger logger = LoggerFactory.getLogger(JADMcpBootstrap.class);
    
    private JADMcpServer mcpServer;
    private final CommandExecutor commandExecutor;
    private final String mcpEndpoint;
    private final String protocol;
    private static JADMcpBootstrap instance;

    public JADMcpBootstrap(CommandExecutor commandExecutor, String mcpEndpoint, String protocol) {
        this.commandExecutor = commandExecutor;
        this.mcpEndpoint = mcpEndpoint;
        this.protocol = protocol;
        instance = this;
    }

    public static JADMcpBootstrap getInstance() {
        return instance;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public JADMcpServer start() {
        logger.info("Initializing JAD MCP Bootstrap...");
        try {
            logger.debug("Creating MCP server instance with command executor: {}", 
                    commandExecutor.getClass().getSimpleName());
            
            // Create and start MCP server with CommandExecutor and custom endpoint
            mcpServer = new JADMcpServer(mcpEndpoint, commandExecutor, protocol);
            logger.debug("MCP server instance created successfully");
            
            mcpServer.start();
            logger.info("JAD MCP server initialized successfully");
            logger.info("Bootstrap ready - server is operational");
            return mcpServer;
        } catch (Exception e) {
            logger.error("Failed to initialize JAD MCP server", e);
            throw new RuntimeException("Failed to initialize JAD MCP server", e);
        }
    }

    public void shutdown() {
        logger.info("Initiating JAD MCP Bootstrap shutdown...");
        if (mcpServer != null) {
            logger.debug("Stopping MCP server...");
            mcpServer.stop();
            logger.info("MCP server stopped");
        } else {
            logger.warn("MCP server was null during shutdown - may not have been properly initialized");
        }
        logger.info("JAD MCP Bootstrap shutdown completed");
    }
}
