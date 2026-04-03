package unittest.grpc.service;

import jad.grpc.unittest.JADUnittest.JADUnittestRequest;
import jad.grpc.unittest.JADUnittest.JADUnittestResponse;
import com.akshita.jad.grpc.server.handler.GrpcRequest;
import com.akshita.jad.grpc.server.handler.GrpcResponse;
import com.akshita.jad.grpc.server.handler.StreamObserver;

/**
 * @author: FengYe
 * @date: 2024/6/30 11:42
 * @description: JADSampleService
 */
public interface JADUnittestService {
    JADUnittestResponse unary(JADUnittestRequest command);

    JADUnittestResponse unaryAddSum(JADUnittestRequest command);

    JADUnittestResponse unaryGetSum(JADUnittestRequest command);

    StreamObserver<GrpcRequest<JADUnittestRequest>> clientStreamSum(StreamObserver<GrpcResponse<JADUnittestResponse>> observer);

    void serverStream(JADUnittestRequest request, StreamObserver<GrpcResponse<JADUnittestResponse>> observer);

    StreamObserver<GrpcRequest<JADUnittestRequest>> biStream(StreamObserver<GrpcResponse<JADUnittestResponse>> observer);
}
