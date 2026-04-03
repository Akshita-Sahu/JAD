package com.akshita.jad.grpc.server.handler.executor;

import com.akshita.jad.grpc.server.handler.GrpcDispatcher;
import com.akshita.jad.grpc.server.handler.GrpcRequest;
import com.akshita.jad.grpc.server.handler.GrpcResponse;
import com.akshita.jad.grpc.server.handler.constant.GrpcInvokeTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.DefaultHttp2DataFrame;
import io.netty.handler.codec.http2.DefaultHttp2HeadersFrame;
import io.netty.handler.codec.http2.Http2DataFrame;

/**
 * @author: FengYe
 * @date: 2024/10/24 01:51
 * @description: UnaryProcessor
 */
public class UnaryExecutor extends AbstractGrpcExecutor {

    public UnaryExecutor(GrpcDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public GrpcInvokeTypeEnum supportGrpcType() {
        return GrpcInvokeTypeEnum.UNARY;
    }

    @Override
    public void execute(GrpcRequest request, Http2DataFrame frame, ChannelHandlerContext context) throws Throwable {
        // ， endStream 
        if (frame.isEndStream()) {
            GrpcResponse response = dispatcher.unaryExecute(request);
            context.writeAndFlush(new DefaultHttp2HeadersFrame(response.getEndHeader()).stream(frame.stream()));
            context.writeAndFlush(new DefaultHttp2DataFrame(response.getResponseData()).stream(frame.stream()));
            context.writeAndFlush(new DefaultHttp2HeadersFrame(response.getEndStreamHeader(), true).stream(frame.stream()));
        }
    }
}
