package unittest.grpc.service.impl;

import jad.grpc.unittest.JADUnittest;
import jad.grpc.unittest.JADUnittest.JADUnittestRequest;
import jad.grpc.unittest.JADUnittest.JADUnittestResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.akshita.jad.grpc.server.handler.GrpcRequest;
import com.akshita.jad.grpc.server.handler.GrpcResponse;
import com.akshita.jad.grpc.server.handler.StreamObserver;
import com.akshita.jad.grpc.server.handler.annotation.GrpcMethod;
import com.akshita.jad.grpc.server.handler.annotation.GrpcService;
import com.akshita.jad.grpc.server.handler.constant.GrpcInvokeTypeEnum;
import unittest.grpc.service.JADUnittestService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: FengYe
 * @date: 2024/6/30 11:43
 * @description: JADSampleServiceImpl
 */
@GrpcService("jad.grpc.unittest.JADUnittestService")
public class JADUnittestServiceImpl implements JADUnittestService {

    private ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();

    @Override
    @GrpcMethod(value = "unary")
    public JADUnittestResponse unary(JADUnittestRequest command) {
        JADUnittestResponse.Builder builder = JADUnittestResponse.newBuilder();
        builder.setMessage(command.getMessage());
        return builder.build();
    }

    @Override
    @GrpcMethod(value = "unaryAddSum")
    public JADUnittestResponse unaryAddSum(JADUnittestRequest command) {
        JADUnittestResponse.Builder builder = JADUnittestResponse.newBuilder();
        builder.setMessage(command.getMessage());
        map.merge(command.getId(), command.getNum(), Integer::sum);
        return builder.build();
    }

    @Override
    @GrpcMethod(value = "unaryGetSum")
    public JADUnittestResponse unaryGetSum(JADUnittestRequest command) {
        JADUnittestResponse.Builder builder = JADUnittestResponse.newBuilder();
        builder.setMessage(command.getMessage());
        Integer sum = map.getOrDefault(command.getId(), 0);
        builder.setNum(sum);
        return builder.build();
    }

    @Override
    @GrpcMethod(value = "clientStreamSum", grpcType = GrpcInvokeTypeEnum.CLIENT_STREAM)
    public StreamObserver<GrpcRequest<JADUnittestRequest>> clientStreamSum(StreamObserver<GrpcResponse<JADUnittestResponse>> observer) {
        return new StreamObserver<GrpcRequest<JADUnittestRequest>>() {
            AtomicInteger sum = new AtomicInteger(0);

            @Override
            public void onNext(GrpcRequest<JADUnittestRequest> req) {
                try {
                    byte[] bytes = req.readData();
                    while (bytes != null && bytes.length != 0) {
                        JADUnittestRequest request = JADUnittestRequest.parseFrom(bytes);
                        sum.addAndGet(request.getNum());
                        bytes = req.readData();
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onCompleted() {
                JADUnittestResponse response = JADUnittestResponse.newBuilder()
                        .setNum(sum.get())
                        .build();
                GrpcResponse<JADUnittestResponse> grpcResponse = new GrpcResponse<>();
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
    public void serverStream(JADUnittestRequest request, StreamObserver<GrpcResponse<JADUnittestResponse>> observer) {

        for (int i = 0; i < 5; i++) {
            JADUnittest.JADUnittestResponse response = JADUnittest.JADUnittestResponse.newBuilder()
                    .setMessage("Server response " + i + " to " + request.getMessage())
                    .build();
            GrpcResponse<JADUnittestResponse> grpcResponse = new GrpcResponse<>();
            grpcResponse.setService("jad.grpc.unittest.JADUnittestService");
            grpcResponse.setMethod("serverStream");
            grpcResponse.writeResponseData(response);
            observer.onNext(grpcResponse);
        }
        observer.onCompleted();
    }

    @Override
    @GrpcMethod(value = "biStream", grpcType = GrpcInvokeTypeEnum.BI_STREAM)
    public StreamObserver<GrpcRequest<JADUnittestRequest>> biStream(StreamObserver<GrpcResponse<JADUnittestResponse>> observer) {
        return new StreamObserver<GrpcRequest<JADUnittestRequest>>() {
            @Override
            public void onNext(GrpcRequest<JADUnittestRequest> req) {
                try {
                    byte[] bytes = req.readData();
                    while (bytes != null && bytes.length != 0) {
                        GrpcResponse<JADUnittestResponse> grpcResponse = new GrpcResponse<>();
                        grpcResponse.setService("jad.grpc.unittest.JADUnittestService");
                        grpcResponse.setMethod("biStream");
                        grpcResponse.writeResponseData(JADUnittestResponse.parseFrom(bytes));
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