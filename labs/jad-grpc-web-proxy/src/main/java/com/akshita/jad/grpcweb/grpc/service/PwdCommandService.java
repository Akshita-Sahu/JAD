package com.akshita.jad.grpcweb.grpc.service;

import com.akshita.jad.core.shell.system.ExecStatus;
import io.jad.api.JADServices.ResponseBody;
import io.jad.api.PwdGrpc;
import com.google.protobuf.Empty;
import com.akshita.jad.core.command.model.PwdModel;

import com.akshita.jad.core.shell.session.SessionManager;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;
import com.akshita.jad.grpcweb.grpc.observer.impl.JADStreamObserverImpl;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.lang.instrument.Instrumentation;


public class PwdCommandService extends PwdGrpc.PwdImplBase{

    private GrpcJobController grpcJobController;


    public PwdCommandService(GrpcJobController grpcJobController) {
        this.grpcJobController = grpcJobController;
    }

    @Override
    public void pwd(Empty empty, StreamObserver<ResponseBody> responseObserver){
        String path = new File("").getAbsolutePath();
        JADStreamObserver<ResponseBody> jadStreamObserver = new JADStreamObserverImpl<>(responseObserver, null,grpcJobController);
        jadStreamObserver.setProcessStatus(ExecStatus.RUNNING);
        jadStreamObserver.appendResult(new PwdModel(path));
        jadStreamObserver.onCompleted();
    }
}
