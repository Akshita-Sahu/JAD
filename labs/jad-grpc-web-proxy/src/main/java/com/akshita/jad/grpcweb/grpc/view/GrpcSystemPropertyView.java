package com.akshita.jad.grpcweb.grpc.view;

import io.jad.api.JADServices.ResponseBody;
import io.jad.api.JADServices.StringStringMapValue;
import com.akshita.jad.core.command.model.SystemPropertyModel;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;

public class GrpcSystemPropertyView extends GrpcResultView<SystemPropertyModel>{

    @Override
    public void draw(JADStreamObserver jadStreamObserver, SystemPropertyModel result) {
        StringStringMapValue stringStringMapValue = StringStringMapValue.newBuilder()
                .putAllStringStringMap(result.getProps()).build();
        ResponseBody responseBody  = ResponseBody.newBuilder()
                .setJobId(result.getJobId())
                .setType(result.getType())
                .setStringStringMapValue(stringStringMapValue)
                .build();
        jadStreamObserver.onNext(responseBody);
    }
}
