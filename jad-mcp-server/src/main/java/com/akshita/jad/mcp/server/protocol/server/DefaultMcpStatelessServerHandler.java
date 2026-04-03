/*
 * Copyright 2024-2024 the original author or authors.
 */

package com.akshita.jad.mcp.server.protocol.server;

import com.akshita.jad.mcp.server.CommandExecutor;
import com.akshita.jad.mcp.server.protocol.spec.McpError;
import com.akshita.jad.mcp.server.protocol.spec.McpSchema;
import com.akshita.jad.mcp.server.session.JADCommandContext;
import com.akshita.jad.mcp.server.session.JADCommandSessionManager;
import com.akshita.jad.mcp.server.util.McpAuthExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

class DefaultMcpStatelessServerHandler implements McpStatelessServerHandler {

	private static final Logger logger = LoggerFactory.getLogger(DefaultMcpStatelessServerHandler.class);

	Map<String, McpStatelessRequestHandler<?>> requestHandlers;

	Map<String, McpStatelessNotificationHandler> notificationHandlers;

	private final CommandExecutor commandExecutor;

	private final JADCommandSessionManager commandSessionManager;

	public DefaultMcpStatelessServerHandler(Map<String, McpStatelessRequestHandler<?>> requestHandlers,
                                            Map<String, McpStatelessNotificationHandler> notificationHandlers,
                                            CommandExecutor commandExecutor) {
		this.requestHandlers = requestHandlers;
		this.notificationHandlers = notificationHandlers;
		this.commandExecutor = commandExecutor;
		this.commandSessionManager = new JADCommandSessionManager(commandExecutor);
	}

	@Override
	public CompletableFuture<McpSchema.JSONRPCResponse> handleRequest(McpTransportContext ctx, McpSchema.JSONRPCRequest req) {
		// Create a temporary session for this request
		String tempSessionId = UUID.randomUUID().toString();
		JADCommandSessionManager.CommandSessionBinding binding = commandSessionManager.createCommandSession(tempSessionId);
		JADCommandContext commandContext = new JADCommandContext(commandExecutor, binding);

		// Extract auth subject from transport context and apply to session
		Object authSubject = ctx.get(McpAuthExtractor.MCP_AUTH_SUBJECT_KEY);
		if (authSubject != null) {
			commandExecutor.setSessionAuth(binding.getJADSessionId(), authSubject);
			logger.debug("Applied auth subject to stateless session: {}", binding.getJADSessionId());
		}

		// Extract userId from transport context and apply to session
		String userId = (String) ctx.get(McpAuthExtractor.MCP_USER_ID_KEY);
		if (userId != null) {
			commandExecutor.setSessionUserId(binding.getJADSessionId(), userId);
			logger.debug("Applied userId to stateless session: {}", binding.getJADSessionId());
		}

		McpStatelessRequestHandler<?> handler = requestHandlers.get(req.getMethod());
		if (handler == null) {
			// Clean up session if handler not found
			closeSession(binding);
			CompletableFuture<McpSchema.JSONRPCResponse> f = new CompletableFuture<>();
			f.completeExceptionally(new McpError("Missing handler for request type: " + req.getMethod()));
			return f;
		}
		try {
			@SuppressWarnings("unchecked")
			CompletableFuture<Object> result = (CompletableFuture<Object>) handler
					.handle(ctx, commandContext, req.getParams());
			return result.handle((r, ex) -> {
				// Clean up session after execution
				closeSession(binding);

				if (ex != null) {
					Throwable cause = ex instanceof CompletionException ? ex.getCause() : ex;
					return new McpSchema.JSONRPCResponse(McpSchema.JSONRPC_VERSION, req.getId(), null,
							new McpSchema.JSONRPCResponse.JSONRPCError(McpSchema.ErrorCodes.INTERNAL_ERROR, cause.getMessage(), null));
				}
				return new McpSchema.JSONRPCResponse(McpSchema.JSONRPC_VERSION, req.getId(), r, null);
			});
		} catch (Throwable t) {
			// Clean up session on error
			closeSession(binding);

			CompletableFuture<McpSchema.JSONRPCResponse> f = new CompletableFuture<>();
			f.completeExceptionally(t);
			return f;
		}
	}

	private void closeSession(JADCommandSessionManager.CommandSessionBinding binding) {
		try {
			commandExecutor.closeSession(binding.getJADSessionId());
		} catch (Exception e) {
			logger.warn("Failed to close temporary session: {}", binding.getJADSessionId(), e);
		}
	}

	@Override
	public CompletableFuture<Void> handleNotification(McpTransportContext ctx,
													  McpSchema.JSONRPCNotification note) {
		McpStatelessNotificationHandler handler = notificationHandlers.get(note.getMethod());
		if (handler == null) {
			logger.warn("Missing handler for notification: {}", note.getMethod());
			return CompletableFuture.completedFuture(null);
		}
		try {
			return handler.handle(ctx, note.getParams());
		} catch (Throwable t) {
			CompletableFuture<Void> f = new CompletableFuture<>();
			f.completeExceptionally(t);
			return f;
		}
	}

}
