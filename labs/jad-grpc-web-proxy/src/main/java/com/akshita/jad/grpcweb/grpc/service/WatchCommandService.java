package com.akshita.jad.grpcweb.grpc.service;

import io.jad.api.JADServices.ResponseBody;
import io.jad.api.JADServices.WatchRequest;
import io.jad.api.WatchGrpc;
import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.advisor.AdviceWeaver;
import com.akshita.jad.core.command.model.MessageModel;

import com.akshita.jad.core.shell.system.ExecStatus;
import com.akshita.jad.grpcweb.grpc.DemoBootstrap;
import com.akshita.jad.grpcweb.grpc.model.WatchRequestModel;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;
import com.akshita.jad.grpcweb.grpc.observer.impl.JADStreamObserverImpl;
import com.akshita.jad.grpcweb.grpc.service.advisor.WatchRpcAdviceListener;
import io.grpc.stub.StreamObserver;

public class WatchCommandService extends WatchGrpc.WatchImplBase {

    private static final Logger logger = LoggerFactory.getLogger(WatchCommandService.class);

    private WatchRequestModel watchRequestModel;

    private JADStreamObserver jadStreamObserver;


    private GrpcJobController grpcJobController;

    public WatchCommandService(GrpcJobController grpcJobController) {
        this.grpcJobController = grpcJobController;
    }

    @Override
    public void watch(WatchRequest watchRequest, StreamObserver<ResponseBody> responseObserver){
        // watchRequest 
        watchRequestModel = new WatchRequestModel(watchRequest);
        JADStreamObserverImpl<ResponseBody> newJADStreamObserver = new JADStreamObserverImpl<>(responseObserver, watchRequestModel, grpcJobController);
        // jadStreamObserver advisor，
        if(grpcJobController.containsJob(watchRequestModel.getJobId())){
            jadStreamObserver = grpcJobController.getGrpcJob(watchRequest.getJobId());
            if(jadStreamObserver != null && jadStreamObserver.getPorcessStatus() == ExecStatus.RUNNING){
                WatchRpcAdviceListener listener = (WatchRpcAdviceListener) AdviceWeaver.listener(jadStreamObserver.getListener().id());
                watchRequestModel.setListenerId(listener.id());
                jadStreamObserver.setRequestModel(watchRequestModel);
                listener.setJADStreamObserver(jadStreamObserver);
                jadStreamObserver.appendResult(new MessageModel("SUCCESS CHANGE!!!!!!!!!!!"));
                newJADStreamObserver.setProcessStatus(ExecStatus.RUNNING);
                newJADStreamObserver.end(0,"!!!");
                return;
            }else {
                jadStreamObserver = newJADStreamObserver;
            }
        }else {
            jadStreamObserver = newJADStreamObserver;
        }
        // watch
        WatchTask watchTask = new WatchTask();
        // watch
        DemoBootstrap.getRunningInstance().execute(watchTask);
    }

    private class WatchTask implements Runnable{
        @Override
        public void run() {
            try {
                watchRequestModel.enhance(jadStreamObserver);
            } catch (Throwable t) {
                logger.error("Error during processing the command:", t);
                jadStreamObserver.end(-1, "Error during processing the command: " + t.getClass().getName() + ", message:" + t.getMessage()
                        + ", please check $HOME/logs/jad/jad.log for more details." );
            }
        }
    }
}
