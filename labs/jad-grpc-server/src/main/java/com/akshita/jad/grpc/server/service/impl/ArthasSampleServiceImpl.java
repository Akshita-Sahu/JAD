package com.akshita.jad.grpc.server.service.impl;

import jad.grpc.unittest.JADUnittest;
import com.google.protobuf.InvalidProtocolBufferException;
import com.akshita.jad.grpc.server.handler.GrpcRequest;
import com.akshita.jad.grpc.server.handler.GrpcResponse;
import com.akshita.jad.grpc.server.handler.StreamObserver;
import com.akshita.jad.grpc.server.handler.annotation.GrpcMethod;
import com.akshita.jad.grpc.server.handler.annotation.GrpcService;
import com.akshita.jad.grpc.server.handler.constant.GrpcInvokeTypeEnum;
import com.akshita.jad.grpc.server.service.JADSampleService;
import com.akshita.jad.grpc.server.utils.ByteUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: FengYe
 * @date: 2024/6/30 11:43
 * @description: JADSampleServiceImpl
 */
@GrpcService("jad.grpc.unittest.JADUnittestService")
public class JADSampleServiceImpl implements JADSampleService {

    private ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();

    @Override
    @GrpcMethod(value = "unary")
    public JADUnittest.JADUnittestResponse unary(JADUnittest.JADUnittestRequest command) {
        JADUnittest.JADUnittestResponse.Builder builder = JADUnittest.JADUnittestResponse.newBuilder();
        builder.setMessage(command.getMessage());
        return builder.build();
    }

    @Override
    @GrpcMethod(value = "unaryAddSum")
    public JADUnittest.JADUnittestResponse unaryAddSum(JADUnittest.JADUnittestRequest command) {
        JADUnittest.JADUnittestResponse.Builder builder = JADUnittest.JADUnittestResponse.newBuilder();
        builder.setMessage(command.getMessage());
        map.merge(command.getId(), command.getNum(), Integer::sum);
        return builder.build();
    }

    @Override
    @GrpcMethod(value = "unaryGetSum")
    public JADUnittest.JADUnittestResponse unaryGetSum(JADUnittest.JADUnittestRequest command) {
        JADUnittest.JADUnittestResponse.Builder builder = JADUnittest.JADUnittestResponse.newBuilder();
        builder.setMessage(command.getMessage());
        Integer sum = map.getOrDefault(command.getId(), 0);
        builder.setNum(sum);
        return builder.build();
    }

    @Override
    @GrpcMethod(value = "clientStreamSum", grpcType = GrpcInvokeTypeEnum.CLIENT_STREAM)
    public StreamObserver<GrpcRequest<JADUnittest.JADUnittestRequest>> clientStreamSum(StreamObserver<GrpcResponse<JADUnittest.JADUnittestResponse>> observer) {
        return new StreamObserver<GrpcRequest<JADUnittest.JADUnittestRequest>>() {
            AtomicInteger sum = new AtomicInteger(0);

            @Override
            public void onNext(GrpcRequest<JADUnittest.JADUnittestRequest> req) {
                try {
                    byte[] bytes = req.readData();
                    while (bytes != null && bytes.length != 0) {
                        JADUnittest.JADUnittestRequest request = JADUnittest.JADUnittestRequest.parseFrom(bytes);
                        sum.addAndGet(request.getNum());
                        bytes = req.readData();
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onCompleted() {
                JADUnittest.JADUnittestResponse response = JADUnittest.JADUnittestResponse.newBuilder()
                        .setNum(sum.get())
                        .build();
                GrpcResponse<JADUnittest.JADUnittestResponse> grpcResponse = new GrpcResponse<>();
                grpcResponse.setService("jad.grpc.unittest.JADUnittestService");
                grpcResponse.setMethod("clientStreamSum");
                grpcResponse.writeResponseData(response);
                observer.onNext(grpcResponse);
                observer.onCompleted();
            }
        };
    }

    @Override
    @GrpcMethod(value = "serverStream", grpcType = GrpcInvokeTypeEnum.SERVER_STREAM)
    public void serverStream(JADUnittest.JADUnittestRequest request, StreamObserver<GrpcResponse<JADUnittest.JADUnittestResponse>> observer) {

        for (int i = 0; i < 5; i++) {
            JADUnittest.JADUnittestResponse response = JADUnittest.JADUnittestResponse.newBuilder()
                    .setMessage("Server response " + i + " to " + request.getMessage())
                    .build();
            GrpcResponse<JADUnittest.JADUnittestResponse> grpcResponse = new GrpcResponse<>();
            grpcResponse.setService("jad.grpc.unittest.JADUnittestService");
            grpcResponse.setMethod("serverStream");
            grpcResponse.writeResponseData(response);
            observer.onNext(grpcResponse);
        }
        observer.onCompleted();
    }

    @Override
    @GrpcMethod(value = "biStream", grpcType = GrpcInvokeTypeEnum.BI_STREAM)
    public StreamObserver<GrpcRequest<JADUnittest.JADUnittestRequest>> biStream(StreamObserver<GrpcResponse<JADUnittest.JADUnittestResponse>> observer) {
        return new StreamObserver<GrpcRequest<JADUnittest.JADUnittestRequest>>() {
            @Override
            public void onNext(GrpcRequest<JADUnittest.JADUnittestRequest> req) {
                try {
                    byte[] bytes = req.readData();
                    while (bytes != null && bytes.length != 0) {
                        GrpcResponse<JADUnittest.JADUnittestResponse> grpcResponse = new GrpcResponse<>();
                        grpcResponse.setService("jad.grpc.unittest.JADUnittestService");
                        grpcResponse.setMethod("biStream");
                        grpcResponse.writeResponseData(JADUnittest.JADUnittestResponse.parseFrom(bytes));
                        observer.onNext(grpcResponse);
                        bytes = req.readData();
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onCompleted() {
                observer.onCompleted();
            }
        };
    }
}