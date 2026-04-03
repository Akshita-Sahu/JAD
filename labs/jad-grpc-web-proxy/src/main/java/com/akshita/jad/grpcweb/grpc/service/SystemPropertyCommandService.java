package com.akshita.jad.grpcweb.grpc.service;

import com.akshita.jad.core.shell.system.ExecStatus;
import io.jad.api.JADServices.ResponseBody;
import io.jad.api.JADServices.StringKey;
import io.jad.api.JADServices.StringStringMapValue;
import io.jad.api.SystemPropertyGrpc;
import com.google.protobuf.Empty;
import com.akshita.jad.core.command.model.SystemPropertyModel;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;
import com.akshita.jad.grpcweb.grpc.observer.impl.JADStreamObserverImpl;
import io.grpc.stub.StreamObserver;

import java.util.Map;

public class SystemPropertyCommandService extends SystemPropertyGrpc.SystemPropertyImplBase{

    private GrpcJobController grpcJobController;

    public SystemPropertyCommandService(GrpcJobController grpcJobController) {
        this.grpcJobController = grpcJobController;
    }

    @Override
    public void get(Empty empty, StreamObserver<ResponseBody> responseObserver){
        JADStreamObserver<ResponseBody> jadStreamObserver = new JADStreamObserverImpl<>(responseObserver, null, grpcJobController);
        jadStreamObserver.setProcessStatus(ExecStatus.RUNNING);
        jadStreamObserver.appendResult(new SystemPropertyModel(System.getProperties()));
        jadStreamObserver.end();
    }

    @Override
    public void getByKey(StringKey request, StreamObserver<ResponseBody> responseObserver){
        String propertyName = request.getKey();
        JADStreamObserver<ResponseBody> jadStreamObserver = new JADStreamObserverImpl<>(responseObserver,null, grpcJobController);
        jadStreamObserver.setProcessStatus(ExecStatus.RUNNING);
        // view the specified system property
        String value = System.getProperty(propertyName);
        if (value == null) {
            jadStreamObserver.end(-1, "There is no property with the key " + propertyName);
            return;
        } else {
            jadStreamObserver.appendResult(new SystemPropertyModel(propertyName, value));
            jadStreamObserver.end();
        }
    }

    @Override
    public void update(StringStringMapValue request, StreamObserver<ResponseBody> responseObserver){
        // get properties from client
        Map<String, String> properties = request.getStringStringMapMap();
        String propertyName = "";
        String propertyValue = "";
        // change system property
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            propertyName = entry.getKey();
            propertyValue = entry.getValue();
        }
        JADStreamObserver<ResponseBody> jadStreamObserver = new JADStreamObserverImpl<>(responseObserver,null, grpcJobController);
        jadStreamObserver.setProcessStatus(ExecStatus.RUNNING);
        try {
            System.setProperty(propertyName, propertyValue);
            jadStreamObserver.appendResult(new SystemPropertyModel(propertyName, System.getProperty(propertyName)));
            jadStreamObserver.onCompleted();
        }catch (Throwable t) {
            jadStreamObserver.end(-1, "Error during setting system property: " + t.getMessage());
        }
    }
}
