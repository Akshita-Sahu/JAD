package com.akshita.jad.grpcweb.grpc.view;

import io.jad.api.JADServices.ResponseBody;
import io.jad.api.JADServices.StringStringMapValue;
import com.akshita.jad.core.command.model.PwdModel;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;

/**
 * @author xuyang 2023/8/15
 */
public class GrpcPwdView extends GrpcResultView<PwdModel> {


    @Override
    public void draw(JADStreamObserver jadStreamObserver, PwdModel result) {
        StringStringMapValue stringStringMapValue = StringStringMapValue.newBuilder()
                .putStringStringMap("workingDir", result.getWorkingDir()).build();
        ResponseBody responseBody  = ResponseBody.newBuilder()
                .setJobId(result.getJobId())
                .setType(result.getType())
                .setStringStringMapValue(stringStringMapValue)
                .build();
        jadStreamObserver.onNext(responseBody);
    }
}
