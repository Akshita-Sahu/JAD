/*
 * Copyright 2024-2024 the original author or authors.
 */

package com.akshita.jad.mcp.server.protocol.server;

import com.akshita.jad.mcp.server.session.JADCommandContext;

import java.util.concurrent.CompletableFuture;

/**
 * Handler for MCP requests in a stateless server.
 */
public interface McpStatelessRequestHandler<R> {

	/**
	 * Handle the request and complete with a result.
	 * @param transportContext {@link McpTransportContext} associated with the transport
	 * @param params the payload of the MCP request
	 * @return Mono which completes with the response object
	 */
	CompletableFuture<R> handle(McpTransportContext transportContext, JADCommandContext jadCommandContext, Object params);

}
