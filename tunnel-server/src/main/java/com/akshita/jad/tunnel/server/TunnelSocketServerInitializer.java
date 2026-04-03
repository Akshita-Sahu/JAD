package com.akshita.jad.tunnel.server;

import com.akshita.jad.common.JADConstants;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * @author hengyunabc 2019-08-27
 *
 */
public class TunnelSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    private TunnelServer tunnelServer;

    public TunnelSocketServerInitializer(TunnelServer tunnelServer, SslContext sslCtx) {
        this.sslCtx = sslCtx;
        this.tunnelServer = tunnelServer;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(JADConstants.MAX_HTTP_CONTENT_LENGTH));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(tunnelServer.getPath(), null, true, JADConstants.MAX_HTTP_CONTENT_LENGTH, false, true, 10000L));
        pipeline.addLast(new IdleStateHandler(0, 0, JADConstants.WEBSOCKET_IDLE_SECONDS));
        pipeline.addLast(new TunnelSocketFrameHandler(tunnelServer));
    }
}
