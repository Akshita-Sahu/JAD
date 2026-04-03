package com.akshita.jad.grpc.server.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * @author: FengYe
 * @date: 2024/9/5 00:51
 * @description: ByteUtil
 */
public class ByteUtil {

    public static ByteBuf newByteBuf() {
        return PooledByteBufAllocator.DEFAULT.buffer();
    }

    public static ByteBuf newByteBuf(byte[] bytes) {
        return PooledByteBufAllocator.DEFAULT.buffer(bytes.length).writeBytes(bytes);
    }

    public static byte[] getBytes(ByteBuf buf) {
        if (buf.hasArray()) {
            //  ByteBuf ，
            return buf.array();
        } else {
            //  byte 
            byte[] bytes = new byte[buf.readableBytes()];
            //  ByteBuf  byte 
            buf.getBytes(buf.readerIndex(), bytes);
            return bytes;
        }
    }
}
