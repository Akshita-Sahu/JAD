package com.akshita.jad.grpc.server.service;

import jad.grpc.unittest.JADUnittest;
import com.akshita.jad.grpc.server.handler.GrpcRequest;
import com.akshita.jad.grpc.server.handler.GrpcResponse;
import com.akshita.jad.grpc.server.handler.StreamObserver;


/**
 * @author: FengYe
 * @date: 2024/6/30 11:42
 * @description: JADSampleService
 */
public interface JADSampleService {
    JADUnittest.JADUnittestResponse unary(JADUnittest.JADUnittestRequest command);

    JADUnittest.JADUnittestResponse unaryAddSum(JADUnittest.JADUnittestRequest command);

    JADUnittest.JADUnittestResponse unaryGetSum(JADUnittest.JADUnittestRequest command);

    StreamObserver<GrpcRequest<JADUnittest.JADUnittestRequest>> clientStreamSum(StreamObserver<GrpcResponse<JADUnittest.JADUnittestResponse>> observer);

    void serverStream(JADUnittest.JADUnittestRequest request, StreamObserver<GrpcResponse<JADUnittest.JADUnittestResponse>> observer);

    StreamObserver<GrpcRequest<JADUnittest.JADUnittestRequest>> biStream(StreamObserver<GrpcResponse<JADUnittest.JADUnittestResponse>> observer);
}
