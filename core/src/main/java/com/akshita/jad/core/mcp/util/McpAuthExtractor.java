package com.akshita.jad.core.mcp.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MCP
 *
 * @author Yeaury
 */
public class McpAuthExtractor {

    private static final Logger logger = LoggerFactory.getLogger(McpAuthExtractor.class);

    public static final String MCP_AUTH_SUBJECT_KEY = "mcp.auth.subject";

    /**
     * User ID  McpTransportContext  key
     */
    public static final String MCP_USER_ID_KEY = "mcp.user.id";

    /**
     *  HTTP Header  User ID  header 
     */
    public static final String USER_ID_HEADER = "X-User-Id";

    public static final AttributeKey<Object> SUBJECT_ATTRIBUTE_KEY =
            AttributeKey.valueOf("jad.auth.subject");

    /**
     * ChannelHandlerContext
     *
     * @param ctx Netty ChannelHandlerContext
     * @return ，null
     */
    public static Object extractAuthSubjectFromContext(ChannelHandlerContext ctx) {
        if (ctx == null || ctx.channel() == null) {
            return null;
        }

        try {
            Object subject = ctx.channel().attr(SUBJECT_ATTRIBUTE_KEY).get();
            if (subject != null) {
                logger.debug("Extracted auth subject from channel context: {}", subject.getClass().getSimpleName());
                return subject;
            }
        } catch (Exception e) {
            logger.debug("Failed to extract auth subject from context: {}", e.getMessage());
        }

        return null;
    }

    /**
     *  HTTP  User ID
     *
     * @param request HTTP 
     * @return User ID， null
     */
    public static String extractUserIdFromRequest(FullHttpRequest request) {
        if (request == null) {
            return null;
        }

        String userId = request.headers().get(USER_ID_HEADER);
        if (userId != null && !userId.trim().isEmpty()) {
            logger.debug("Extracted userId from HTTP header {}: {}", USER_ID_HEADER, userId);
            return userId.trim();
        }

        return null;
    }

}
