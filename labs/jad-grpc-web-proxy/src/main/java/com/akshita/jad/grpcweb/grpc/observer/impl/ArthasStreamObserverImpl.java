package com.akshita.jad.grpcweb.grpc.observer.impl;

import io.jad.api.JADServices.ResponseBody;
import com.akshita.jad.core.advisor.AdviceListener;
import com.akshita.jad.core.advisor.AdviceWeaver;
import com.akshita.jad.core.command.model.ResultModel;
import com.akshita.jad.core.command.model.StatusModel;
import com.akshita.jad.core.distribution.ResultDistributor;

import com.akshita.jad.core.shell.system.ExecStatus;
import com.akshita.jad.core.shell.system.ProcessAware;
import com.akshita.jad.grpcweb.grpc.DemoBootstrap;
import com.akshita.jad.grpcweb.grpc.distribution.GrpcResultDistributorImpl;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;
import com.akshita.jad.grpcweb.grpc.service.GrpcJobController;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.atomic.AtomicInteger;

public class JADStreamObserverImpl<T> implements JADStreamObserver<T> {

    private StreamObserver<T> streamObserver;

    private AtomicInteger times = new AtomicInteger();

    private GrpcProcess process;

    private Object requestModel;
    private AdviceListener listener;

    private ClassFileTransformer transformer;

    private final int jobId;


    private ResultDistributor resultDistributor;

    private GrpcJobController grpcJobController;

    private Instrumentation instrumentation;


    public JADStreamObserverImpl(StreamObserver<T> streamObserver, Object requestModel, GrpcJobController grpcJobController){
        this.streamObserver = streamObserver;
        this.jobId = grpcJobController.generateGrpcJobId();
        this.instrumentation = grpcJobController.getInstrumentation();
        if (resultDistributor == null) {
            resultDistributor = new GrpcResultDistributorImpl(this, grpcJobController.getResultViewResolver());
        }
        this.process = new GrpcProcess();
        this.process.setProcessStatus(ExecStatus.READY);
        // 
        this.requestModel = requestModel;
        // 
        this.setOnCancelHandler();
        this.grpcJobController = grpcJobController;
        this.grpcJobController.registerGrpcJob(jobId, this);
    }


    @Override
    public void onNext(T value) {
        streamObserver.onNext(value);
    }

    @Override
    public void onError(Throwable t) {
        streamObserver.onError(t);
    }

    @Override
    public void onCompleted() {
        this.process.setProcessStatus(ExecStatus.TERMINATED);
//        grpcJobController.unRegisterGrpcJob(this.jobId);
        streamObserver.onCompleted();
    }

    @Override
    public AtomicInteger times() {
        return times;
    }


    @Override
    public void register(AdviceListener adviceListener, ClassFileTransformer transformer) {
        if (adviceListener instanceof ProcessAware) {
            ProcessAware processAware = (ProcessAware) adviceListener;
            // listener  command 
            if(processAware.getProcess() == null) {
                this.process.setProcessStatus(ExecStatus.RUNNING);
                processAware.setProcess(this.process);
            }
        }
        this.listener = adviceListener;
        AdviceWeaver.reg(listener);

        this.transformer = transformer;
    }

    @Override
    public void unregister() {
        if (transformer != null) {
            DemoBootstrap.getRunningInstance().getTransformerManager().removeTransformer(transformer);
        }
        this.process.setProcessStatus(ExecStatus.TERMINATED);
        if (listener instanceof ProcessAware) {
            // listener command ，unRge
            if (this.process.equals(((ProcessAware) listener).getProcess())) {
                AdviceWeaver.unReg(listener);
            }
        } else {
            AdviceWeaver.unReg(listener);
        }
    }

    @Override
    public void end() {
        end(0);
    }

    @Override
    public ExecStatus getPorcessStatus() {
        return this.process.status();
    }
    @Override
    public void setProcessStatus(ExecStatus execStatus){
        this.process.setProcessStatus(execStatus);
    }

    @Override
    public void end(int statusCode) {
        end(statusCode, null);
    }

    @Override
    public void end(int statusCode, String message) {
        terminate(statusCode, message);
    }


    @Override
    public JADStreamObserver write(String msg) {
        ResponseBody result = ResponseBody.newBuilder().setStringValue(msg).build();
        onNext((T) result);
        return this;
    }

    @Override
    public void appendResult(ResultModel result) {
        if (process.status() != ExecStatus.RUNNING) {
            throw new IllegalStateException(
                    "Cannot write to standard output when " + process.status().name().toLowerCase());
        }
        result.setJobId(jobId);
        if (resultDistributor != null) {
            resultDistributor.appendResult(result);
        }
    }
    @Override
    public int getJobId() {
        return jobId;
    }

    @Override
    public Object getRequestModel() {
        return requestModel;
    }

    @Override
    public void setRequestModel(Object requestModel) {
        this.requestModel = requestModel;
    }

    public void setOnCancelHandler() {
        ServerCallStreamObserver<T> observer = (ServerCallStreamObserver<T>) this.streamObserver;
        observer.setOnCancelHandler(() -> {
            this.end();
        });
    }


    private synchronized boolean terminate(int exitCode, String message) {
        boolean flag;
        if (process.status() != ExecStatus.TERMINATED) {
            //add status message
            this.appendResult(new StatusModel(exitCode, message));
            if (process != null) {
                this.unregister();
            }
            flag = true;
        } else {
            flag = false;
        }
        this.onCompleted();
        return flag;
    }


    public AdviceListener getListener() {
        return listener;
    }

    @Override
    public Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
