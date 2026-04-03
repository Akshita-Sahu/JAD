package com.akshita.jad.core.shell.term.impl.http.session;

import java.util.Collections;
import java.util.Set;

import com.akshita.jad.common.JADConstants;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * <pre>
 * nettyhttp session。cookie，。
 * </pre>
 * 
 * @author hengyunabc 2021-03-03
 *
 */
public class HttpSessionManager {
    public static AttributeKey<HttpSession> SESSION_KEY = AttributeKey.valueOf("session");

    private LRUCache<String, HttpSession> sessions = new LRUCache<String, HttpSession>(1024);

    public HttpSessionManager() {

    }

    private HttpSession getSession(HttpRequest httpRequest) {
        // TODO  url session id ？

        Set<Cookie> cookies;
        String value = httpRequest.headers().get(HttpHeaderNames.COOKIE);
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            cookies = ServerCookieDecoder.STRICT.decode(value);
        }
        for (Cookie cookie : cookies) {
            if (JADConstants.ASESSION_KEY.equals(cookie.name())) {
                String sessionId = cookie.value();
                return sessions.get(sessionId);
            }
        }
        return null;
    }

    public static HttpSession getHttpSessionFromContext(ChannelHandlerContext ctx) {
        return ctx.channel().attr(SESSION_KEY).get();
    }

    public HttpSession getOrCreateHttpSession(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        //  ctx  cookie session
        Attribute<HttpSession> attribute = ctx.channel().attr(SESSION_KEY);
        HttpSession httpSession = attribute.get();
        if (httpSession != null) {
            return httpSession;
        }
        httpSession = getSession(httpRequest);
        if (httpSession != null) {
            attribute.set(httpSession);
            return httpSession;
        }
        // session，ctx
        httpSession = newHttpSession();
        attribute.set(httpSession);
        return httpSession;
    }

    private HttpSession newHttpSession() {
        SimpleHttpSession session = new SimpleHttpSession();
        this.sessions.put(session.getId(), session);
        return session;
    }

    public static void setSessionCookie(HttpResponse response, HttpSession session) {
        response.headers().add(HttpHeaderNames.SET_COOKIE,
                ServerCookieEncoder.STRICT.encode(JADConstants.ASESSION_KEY, session.getId()));
    }

    public void start() {

    }

    public void stop() {
        sessions.clear();
    }

}
