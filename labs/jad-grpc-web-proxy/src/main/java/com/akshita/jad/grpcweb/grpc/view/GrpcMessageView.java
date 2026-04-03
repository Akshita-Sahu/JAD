package com.akshita.jad.grpcweb.grpc.view;

import io.jad.api.JADServices.ResponseBody;
import com.akshita.jad.core.command.model.MessageModel;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;

public class GrpcMessageView extends GrpcResultView<MessageModel> {
    @Override
    public void draw(JADStreamObserver jadStreamObserver, MessageModel result) {
        ResponseBody responseBody  = ResponseBody.newBuilder()
                .setJobId(result.getJobId())
                .setType(result.getType())
                .setStringValue(result.getMessage())
                .build();
        jadStreamObserver.onNext(responseBody);
    }
}
