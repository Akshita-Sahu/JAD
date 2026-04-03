package com.akshita.jad.core.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.akshita.jad.core.mcp.tool.util.McpToolUtils;
import com.akshita.jad.mcp.server.CommandExecutor;
import com.akshita.jad.mcp.server.protocol.config.McpServerProperties;
import com.akshita.jad.mcp.server.protocol.config.McpServerProperties.ServerProtocol;
import com.akshita.jad.mcp.server.protocol.server.McpNettyServer;
import com.akshita.jad.mcp.server.protocol.server.McpServer;
import com.akshita.jad.mcp.server.protocol.server.McpStatelessNettyServer;
import com.akshita.jad.mcp.server.protocol.server.handler.McpHttpRequestHandler;
import com.akshita.jad.mcp.server.protocol.server.handler.McpStatelessHttpRequestHandler;
import com.akshita.jad.mcp.server.protocol.server.handler.McpStreamableHttpRequestHandler;
import com.akshita.jad.mcp.server.protocol.server.transport.NettyStatelessServerTransport;
import com.akshita.jad.mcp.server.protocol.server.transport.NettyStreamableServerTransportProvider;
import com.akshita.jad.mcp.server.protocol.spec.McpSchema.Implementation;
import com.akshita.jad.mcp.server.protocol.spec.McpSchema.ServerCapabilities;
import com.akshita.jad.mcp.server.protocol.spec.McpStreamableServerTransportProvider;
import com.akshita.jad.mcp.server.tool.DefaultToolCallbackProvider;
import com.akshita.jad.mcp.server.tool.ToolCallback;
import com.akshita.jad.mcp.server.tool.ToolCallbackProvider;
import com.akshita.jad.mcp.server.util.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JAD MCP Server
 * Used to expose HTTP service after JAD startup
 */
public class JADMcpServer {
    private static final Logger logger = LoggerFactory.getLogger(JADMcpServer.class);

    /**
     * JAD tool base package in core module
     */
    public static final String JAD_TOOL_BASE_PACKAGE = "com.akshita.jad.core.mcp.tool.function";

    private McpNettyServer streamableServer;
    private McpStatelessNettyServer statelessServer;

    private final String mcpEndpoint;
    private final ServerProtocol protocol;

    private final CommandExecutor commandExecutor;

    private McpHttpRequestHandler unifiedMcpHandler;

    private McpStreamableHttpRequestHandler streamableHandler;

    private McpStatelessHttpRequestHandler statelessHandler;

    public static final String DEFAULT_MCP_ENDPOINT = "/mcp";
    
    public JADMcpServer(String mcpEndpoint, CommandExecutor commandExecutor, String protocol) {
        this.mcpEndpoint = mcpEndpoint != null ? mcpEndpoint : DEFAULT_MCP_ENDPOINT;
        this.commandExecutor = commandExecutor;
        
        ServerProtocol resolvedProtocol = ServerProtocol.STREAMABLE;
        if (protocol != null && !protocol.trim().isEmpty()) {
            try {
                resolvedProtocol = ServerProtocol.valueOf(protocol.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid MCP protocol: {}. Using default: STREAMABLE", protocol);
            }
        }
        this.protocol = resolvedProtocol;
    }

    public McpHttpRequestHandler getMcpRequestHandler() {
        return unifiedMcpHandler;
    }

    /**
     * Start MCP server
     */
    public void start() {
        try {
            // Register JAD-specific JSON filter
            com.akshita.jad.core.mcp.util.McpObjectVOFilter.register();
            
            McpServerProperties properties = new McpServerProperties.Builder()
                    .name("jad-mcp-server")
                    .version("4.1.8")
                    .mcpEndpoint(mcpEndpoint)
                    .toolChangeNotification(true)
                    .resourceChangeNotification(true)
                    .promptChangeNotification(true)
                    .objectMapper(JsonParser.getObjectMapper())
                    .protocol(this.protocol)
                    .build();

            // Use JAD tool base package from core module
            DefaultToolCallbackProvider toolCallbackProvider = new DefaultToolCallbackProvider();
            toolCallbackProvider.setToolBasePackage(JAD_TOOL_BASE_PACKAGE);
            
            ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();
            List<ToolCallback> providerToolCallbacks = Arrays.stream(callbacks)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            unifiedMcpHandler = McpHttpRequestHandler.builder()
                    .mcpEndpoint(properties.getMcpEndpoint())
                    .objectMapper(properties.getObjectMapper())
                    .protocol(properties.getProtocol())
                    .build();

            if (properties.getProtocol() == ServerProtocol.STREAMABLE) {
                McpStreamableServerTransportProvider transportProvider = createStreamableHttpTransportProvider(properties);
                streamableHandler = transportProvider.getMcpRequestHandler();
                unifiedMcpHandler.setStreamableHandler(streamableHandler);

                McpServer.StreamableServerNettySpecification streamableServerNettySpecification = McpServer.netty(transportProvider)
                        .serverInfo(new Implementation(properties.getName(), properties.getVersion()))
                        .capabilities(buildServerCapabilities(properties))
                        .instructions(properties.getInstructions())
                        .requestTimeout(properties.getRequestTimeout())
                        .commandExecutor(commandExecutor)
                        .objectMapper(properties.getObjectMapper() != null ? properties.getObjectMapper() : JsonParser.getObjectMapper());

                streamableServerNettySpecification.tools(
                        McpToolUtils.toStreamableToolSpecifications(providerToolCallbacks));

                streamableServer = streamableServerNettySpecification.build();
            } else {
                NettyStatelessServerTransport statelessTransport = createStatelessHttpTransport(properties);
                statelessHandler = statelessTransport.getMcpRequestHandler();
                unifiedMcpHandler.setStatelessHandler(statelessHandler);

                McpServer.StatelessServerNettySpecification statelessServerNettySpecification = McpServer.netty(statelessTransport)
                        .serverInfo(new Implementation(properties.getName(), properties.getVersion()))
                        .capabilities(buildServerCapabilities(properties))
                        .instructions(properties.getInstructions())
                        .requestTimeout(properties.getRequestTimeout())
                        .commandExecutor(commandExecutor)
                        .objectMapper(properties.getObjectMapper() != null ? properties.getObjectMapper() : JsonParser.getObjectMapper());

                statelessServerNettySpecification.tools(
                        McpToolUtils.toStatelessToolSpecifications(providerToolCallbacks));

                statelessServer = statelessServerNettySpecification.build();
            }

            logger.info("JAD MCP server started successfully");
            logger.info("- MCP Endpoint: {}", properties.getMcpEndpoint());
            logger.info("- Transport mode: {}", properties.getProtocol());
            logger.info("- Available tools: {}", providerToolCallbacks.size());
            logger.info("- Server ready to accept connections");
        } catch (Exception e) {
            logger.error("Failed to start JAD MCP server", e);
            throw new RuntimeException("Failed to start JAD MCP server", e);
        }
    }
    
    /**
     * Default keep-alive interval for MCP server (15 seconds)
     */
    public static final Duration DEFAULT_KEEP_ALIVE_INTERVAL = Duration.ofSeconds(15);
    
    /**
     * Create HTTP transport provider
     */
    private NettyStreamableServerTransportProvider createStreamableHttpTransportProvider(McpServerProperties properties) {
        return NettyStreamableServerTransportProvider.builder()
                .mcpEndpoint(properties.getMcpEndpoint())
                .objectMapper(properties.getObjectMapper() != null ? properties.getObjectMapper() : new ObjectMapper())
                .keepAliveInterval(DEFAULT_KEEP_ALIVE_INTERVAL)
                .build();
    }

    private NettyStatelessServerTransport createStatelessHttpTransport(McpServerProperties properties) {
        return NettyStatelessServerTransport.builder()
                .mcpEndpoint(properties.getMcpEndpoint())
                .objectMapper(properties.getObjectMapper() != null ? properties.getObjectMapper() : new ObjectMapper())
                .build();
    }

    private ServerCapabilities buildServerCapabilities(McpServerProperties properties) {
        return ServerCapabilities.builder()
                .prompts(new ServerCapabilities.PromptCapabilities(properties.isPromptChangeNotification()))
                .resources(new ServerCapabilities.ResourceCapabilities(properties.isResourceSubscribe(), properties.isResourceChangeNotification()))
                .tools(new ServerCapabilities.ToolCapabilities(properties.isToolChangeNotification()))
                .build();
    }

    public void stop() {
        logger.info("Stopping JAD MCP server...");
        try {
            if (unifiedMcpHandler != null) {
                logger.debug("Shutting down unified MCP handler");
                unifiedMcpHandler.closeGracefully().get();
                logger.info("Unified MCP handler stopped successfully");
            }

            if (streamableServer != null) {
                logger.debug("Shutting down streamable server");
                streamableServer.closeGracefully().get();
                logger.info("Streamable server stopped successfully");
            }

            if (statelessServer != null) {
                logger.debug("Shutting down stateless server");
                statelessServer.closeGracefully().get();
                logger.info("Stateless server stopped successfully");
            }
            
            logger.info("JAD MCP server stopped completely");
        } catch (Exception e) {
            logger.error("Failed to stop JAD MCP server gracefully", e);
        }
    }
}
