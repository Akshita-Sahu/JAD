package com.akshita.jad.core.shell.term.impl.http;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.common.JADConstants;
import com.akshita.jad.core.security.AuthUtils;
import com.akshita.jad.core.security.BasicPrincipal;
import com.akshita.jad.core.security.BearerPrincipal;
import com.akshita.jad.core.security.SecurityAuthenticator;
import com.akshita.jad.core.server.JADBootstrap;
import com.akshita.jad.core.shell.term.impl.http.session.HttpSession;
import com.akshita.jad.core.shell.term.impl.http.session.HttpSessionManager;
import com.akshita.jad.core.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.http.*;
import io.netty.util.Attribute;

import javax.security.auth.Subject;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static com.akshita.jad.mcp.server.util.McpAuthExtractor.SUBJECT_ATTRIBUTE_KEY;


/**
 * 
 * @author hengyunabc 2021-03-03
 *
 */
public final class BasicHttpAuthenticatorHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(BasicHttpAuthenticatorHandler.class);

    private HttpSessionManager httpSessionManager;

    private SecurityAuthenticator securityAuthenticator = JADBootstrap.getInstance().getSecurityAuthenticator();

    public BasicHttpAuthenticatorHandler(HttpSessionManager httpSessionManager) {
        this.httpSessionManager = httpSessionManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //  HttpRequest 
        if (!(msg instanceof HttpRequest)) {
            ctx.fireChannelRead(msg);
            return;
        }

        HttpRequest httpRequest = (HttpRequest) msg;
        HttpSession session = httpSessionManager.getOrCreateHttpSession(ctx, httpRequest);

        // ， URL  userId
        extractAndSetUserIdFromUrl(httpRequest, session);

        if (!securityAuthenticator.needLogin()) {
            ctx.fireChannelRead(msg);
            return;
        }

        boolean authed = false;

        // session
        if (session != null) {
            Object subjectObj = session.getAttribute(JADConstants.SUBJECT_KEY);
            if (subjectObj != null) {
                authed =true;
                setAuthenticatedSubject(ctx, session, subjectObj);
            }
        }

        Principal principal = null;
        boolean isMcpRequest = isMcpRequest(httpRequest);

        if (!authed) {
            if (isMcpRequest) {
                principal = extractMcpAuthSubject(httpRequest);
            } else {
                principal = extractBasicAuthSubject(httpRequest);
                if (principal == null) {
                    principal = extractBasicAuthSubjectFromUrl(httpRequest);
                }
            }
            if (principal == null) {
                // 
                principal = AuthUtils.localPrincipal(ctx);
            }
            Subject subject = securityAuthenticator.login(principal);
            if (subject != null) {
                authed = true;
                setAuthenticatedSubject(ctx, session, subject);
            }
        }

        if (!authed) {
            // restricted resource, so send back 401 to require valid username/password
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);

            if (isMcpRequest) {
                response.headers()
                        .add(HttpHeaderNames.WWW_AUTHENTICATE, "Bearer realm=\"jad mcp\"")
                        .add(HttpHeaderNames.WWW_AUTHENTICATE, "Basic realm=\"jad mcp\"");
            } else {
                response.headers().set(HttpHeaderNames.WWW_AUTHENTICATE, "Basic realm=\"jad webconsole\"");
            }

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);

            ctx.writeAndFlush(response);
            // close the channel
            ctx.channel().close();
            return;
        }

        ctx.fireChannelRead(msg);
    }

    private void setAuthenticatedSubject(ChannelHandlerContext ctx, HttpSession session, Object subject) {
        ctx.channel().attr(SUBJECT_ATTRIBUTE_KEY).set(subject);
        if (session != null) {
            session.setAttribute(JADConstants.SUBJECT_KEY, subject);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse) {
            // write cookie
            HttpResponse response = (HttpResponse) msg;
            Attribute<HttpSession> attribute = ctx.channel().attr(HttpSessionManager.SESSION_KEY);
            HttpSession session = attribute.get();
            if (session != null) {
                HttpSessionManager.setSessionCookie(response, session);
            }
        }
        super.write(ctx, msg, promise);
    }

    /**
     *  url  userId  HttpSession
     * 
     * @param request
     * @param session
     */
    protected static void extractAndSetUserIdFromUrl(HttpRequest request, HttpSession session) {
        if (session == null) {
            return;
        }
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> parameters = queryDecoder.parameters();

        List<String> userIds = parameters.get(JADConstants.USER_ID_KEY);
        if (userIds != null && !userIds.isEmpty()) {
            String userId = userIds.get(0);
            if (!StringUtils.isBlank(userId)) {
                session.setAttribute(JADConstants.USER_ID_KEY, userId);
                logger.debug("Extracted userId from url: {}", userId);
            }
        }
    }

    /**
     *  url  ?username=hello&password=world
     * 
     * @param request
     * @return
     */
    protected static BasicPrincipal extractBasicAuthSubjectFromUrl(HttpRequest request) {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> parameters = queryDecoder.parameters();

        List<String> passwords = parameters.get(JADConstants.PASSWORD_KEY);
        if (passwords == null || passwords.size() == 0) {
            return null;
        }
        String password = passwords.get(0);

        String username = JADConstants.DEFAULT_USERNAME;
        List<String> usernames = parameters.get(JADConstants.USERNAME_KEY);
        if (usernames != null && !usernames.isEmpty()) {
            username = usernames.get(0);
        }
        BasicPrincipal principal = new BasicPrincipal(username, password);
        logger.debug("Extracted Basic Auth principal from url: {}", principal);
        return principal;
    }

    /**
     * Extracts the username and password details from the HTTP basic header
     * Authorization.
     * <p/>
     * This requires that the <tt>Authorization</tt> HTTP header is provided, and
     * its using Basic. Currently Digest is <b>not</b> supported.
     *
     * @return {@link HttpPrincipal} with username and password details, or
     *         <tt>null</tt> if not possible to extract
     */
    protected static BasicPrincipal extractBasicAuthSubject(HttpRequest request) {
        String auth = request.headers().get(HttpHeaderNames.AUTHORIZATION);
        if (auth != null) {
            String constraint = StringUtils.before(auth, " ");
            if (constraint != null) {
                if ("Basic".equalsIgnoreCase(constraint.trim())) {
                    String decoded = StringUtils.after(auth, " ");
                    if (decoded == null) {
                        logger.error("Extracted Basic Auth principal failed, bad auth String: {}", auth);
                        return null;
                    }
                    // the decoded part is base64 encoded, so we need to decode that
                    ByteBuf buf = Unpooled.wrappedBuffer(decoded.getBytes());
                    ByteBuf out = Base64.decode(buf);
                    String userAndPw = out.toString(Charset.defaultCharset());
                    String username = StringUtils.before(userAndPw, ":");
                    String password = StringUtils.after(userAndPw, ":");
                    BasicPrincipal principal = new BasicPrincipal(username, password);
                    logger.debug("Extracted Basic Auth principal from HTTP header: {}", principal);
                    return principal;
                }
            }
        }
        return null;
    }

    /**
     * MCP
     * 
     * @param request
     */
    protected static boolean isMcpRequest(HttpRequest request) {
        try {
            String path = new java.net.URI(request.uri()).getPath();
            if (path == null) {
                return false;
            }

            String mcpEndpoint = JADBootstrap.getInstance().getConfigure().getMcpEndpoint();
            if (mcpEndpoint == null || mcpEndpoint.trim().isEmpty()) {
                // MCP ， MCP 
                return false;
            }
            
            return mcpEndpoint.equals(path);
        } catch (Exception e) {
            logger.debug("Failed to parse request URI: {}", request.uri(), e);
            return false;
        }
    }

    /**
     * MCP，Bearer TokenBasic Auth
     * 
     * @param request
     */
    protected static Principal extractMcpAuthSubject(HttpRequest request) {
        // Bearer Token
        BearerPrincipal tokenPrincipal = extractBearerTokenSubject(request);
        if (tokenPrincipal != null) {
            return tokenPrincipal;
        }

        // Basic Auth
        BasicPrincipal basicPrincipal = extractBasicAuthSubject(request);
        if (basicPrincipal != null) {
            return basicPrincipal;
        }

        // URL
        return extractBasicAuthSubjectFromUrl(request);
    }

    /**
     * Authorization headerBearer Token
     * 
     * @param request
     */
    protected static BearerPrincipal extractBearerTokenSubject(HttpRequest request) {
        String auth = request.headers().get(HttpHeaderNames.AUTHORIZATION);
        if (auth != null) {
            String constraint = StringUtils.before(auth, " ");
            if (constraint != null) {
                if ("Bearer".equalsIgnoreCase(constraint.trim())) {
                    String token = StringUtils.after(auth, " ");
                    if (token == null || token.trim().isEmpty()) {
                        logger.error("Extracted Bearer Token failed, bad auth String: {}", auth);
                        return null;
                    }
                    BearerPrincipal principal = new BearerPrincipal(token.trim());
                    logger.debug("Extracted Bearer Token principal: {}", principal);
                    return principal;
                }
            }
        }
        return null;
    }

}
