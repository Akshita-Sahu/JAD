package com.akshita.jad.grpcweb.grpc.view;

import io.jad.api.JADServices.ResponseBody;
import com.akshita.jad.core.command.model.StatusModel;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;

/**
 * @author xuyang 2023/8/15
 */
public class GrpcStatusView extends GrpcResultView<StatusModel> {

    @Override
    public void draw(JADStreamObserver jadStreamObserver, StatusModel result) {
        if (result.getMessage() != null) {
            ResponseBody responseBody  = ResponseBody.newBuilder()
                    .setJobId(result.getJobId())
                    .setType(result.getType())
                    .setStringValue(result.getMessage())
                    .build();
            jadStreamObserver.onNext(responseBody);
        }
    }
}
