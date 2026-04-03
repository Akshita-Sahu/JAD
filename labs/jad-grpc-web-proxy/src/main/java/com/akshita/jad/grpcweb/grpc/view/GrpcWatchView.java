package com.akshita.jad.grpcweb.grpc.view;

import com.akshita.jad.core.view.ObjectView;
import com.akshita.jad.grpcweb.grpc.model.WatchRequestModel;
import io.jad.api.JADServices.JavaObject;
import io.jad.api.JADServices.ResponseBody;
import io.jad.api.JADServices.WatchResponse;
import com.akshita.jad.core.command.model.ObjectVO;
import com.akshita.jad.core.util.DateUtils;
import com.akshita.jad.grpcweb.grpc.model.WatchResponseModel;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;

import static com.akshita.jad.grpcweb.grpc.objectUtils.JavaObjectConverter.toJavaObjectWithExpand;

/**
 * Term view for WatchModel
 *
 * @author xuyang 2023/8/15
 */
public class GrpcWatchView extends GrpcResultView<WatchResponseModel> {

    @Override
    public void draw(JADStreamObserver jadStreamObserver, WatchResponseModel model) {
        ObjectVO objectVO = model.getValue();
//        Object obj = objectVO.needExpand() ? new ObjectView(model.getSizeLimit(), objectVO).draw() : objectVO.getObject();
        JavaObject javaObject = toJavaObjectWithExpand(objectVO.getObject(), objectVO.getExpand());
        WatchResponse watchResponse = WatchResponse.newBuilder()
                .setAccessPoint(model.getAccessPoint())
                .setClassName(model.getClassName())
                .setCost(model.getCost())
                .setMethodName(model.getMethodName())
                .setSizeLimit(model.getSizeLimit())
                .setTs(DateUtils.formatDateTime(model.getTs()))
                .setValue(javaObject)
                .build();
        ResponseBody responseBody  = ResponseBody.newBuilder()
                .setJobId(model.getJobId())
                .setResultId(model.getResultId())
                .setType(model.getType())
                .setWatchResponse(watchResponse)
                .build();
        jadStreamObserver.onNext(responseBody);
    }
}
