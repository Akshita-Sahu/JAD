package com.akshita.jad.grpcweb.grpc.view;

import io.jad.api.JADServices.ResponseBody;
import com.akshita.jad.core.command.model.EnhancerModel;
import com.akshita.jad.core.command.view.ViewRenderUtil;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;

/**
 * Term grpc view for EnhancerModel
 * @author xuyang 2023/8/15
 */
public class GrpcEnhancerView extends GrpcResultView<EnhancerModel> {
    @Override
    public void draw(JADStreamObserver jadStreamObserver, EnhancerModel result) {
        if (result.getEffect() != null) {
            String msg = ViewRenderUtil.renderEnhancerAffect(result.getEffect());
            ResponseBody responseBody  = ResponseBody.newBuilder()
                    .setJobId(result.getJobId())
                    .setType(result.getType())
                    .setStringValue(msg)
                    .build();
            jadStreamObserver.onNext(responseBody);
        }
    }
}
