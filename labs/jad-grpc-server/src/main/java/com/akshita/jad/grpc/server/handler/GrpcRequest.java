package com.akshita.jad.grpc.server.handler;

import com.akshita.jad.grpc.server.handler.constant.GrpcInvokeTypeEnum;
import com.akshita.jad.grpc.server.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http2.Http2Headers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author: FengYe
 * @date: 2024/9/4 23:07
 * @description: GrpcRequest grpc 
 */
public class GrpcRequest<T> {

    /**
     *  streamId
     */
    private Integer streamId;

    /**
     *  service
     */
    private String service;

    /**
     *  method
     */
    private String method;

    /**
     * ， grpc body， body  5  byte ， boolean compressed - int length
     */
    private ByteBuf byteData;

    /**
     * 
     */
    private int length;

    /**
     * class
     */
    private Class<?> clazz;

    /**
     *  grpc 
     */
    private boolean stream;

    /**
     *  grpc data
     */
    private boolean streamFirstData;

    /**
     * http2 headers
     */
    private Http2Headers headers;

    /**
     * grpc 
     */
    private GrpcInvokeTypeEnum grpcType;


    public GrpcRequest(Integer streamId, String path, String method) {
        this.streamId = streamId;
        this.service = path;
        this.method = method;
        this.byteData = ByteUtil.newByteBuf();
    }

    public void writeData(ByteBuf byteBuf) {
        byte[] bytes = ByteUtil.getBytes(byteBuf);
        if (bytes.length == 0) {
            return;
        }
        byte[] decompressedData = decompressGzip(bytes);
        if (decompressedData == null) {
            return;
        }
        byteData.writeBytes(ByteUtil.newByteBuf(decompressedData));
    }

    /**
     * 
     *
     * @return
     */
    public synchronized byte[] readData() {
        if (byteData.readableBytes() == 0) {
            return null;
        }
        boolean compressed = byteData.readBoolean();
        int length = byteData.readInt();
        byte[] bytes = new byte[length];
        byteData.readBytes(bytes);
        return bytes;
    }

    public void clearData() {
        byteData.clear();
    }

    private byte[] decompressGzip(byte[] compressedData) {
        boolean isGzip = (compressedData.length > 2 && (compressedData[0] & 0xff) == 0x1f && (compressedData[1] & 0xff) == 0x8b);
        if (isGzip) {
            try (InputStream byteStream = new ByteArrayInputStream(compressedData);
                 GZIPInputStream gzipStream = new GZIPInputStream(byteStream);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzipStream.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                return out.toByteArray();
            } catch (IOException e) {
                System.err.println("Failed to decompress GZIP data: " + e.getMessage());
                // Optionally rethrow the exception or return an Optional<byte[]>
                return null; // or throw new RuntimeException(e);
            }
        } else {
            return compressedData;
        }
    }

    public String getGrpcMethodKey() {
        return service + "." + method;
    }

    public Integer getStreamId() {
        return streamId;
    }

    public String getService() {
        return service;
    }

    public String getMethod() {
        return method;
    }

    public ByteBuf getByteData() {
        return byteData;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public boolean isStreamFirstData() {
        return streamFirstData;
    }

    public void setStreamFirstData(boolean streamFirstData) {
        this.streamFirstData = streamFirstData;
    }

    public Http2Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Http2Headers headers) {
        this.headers = headers;
    }

    public GrpcInvokeTypeEnum getGrpcType() {
        return grpcType;
    }

    public void setGrpcType(GrpcInvokeTypeEnum grpcType) {
        this.grpcType = grpcType;
    }
}
