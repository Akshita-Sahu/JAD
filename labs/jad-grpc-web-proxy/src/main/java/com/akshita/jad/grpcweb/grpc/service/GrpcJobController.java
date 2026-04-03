package com.akshita.jad.grpcweb.grpc.service;


import com.akshita.jad.core.advisor.TransformerManager;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;
import com.akshita.jad.grpcweb.grpc.view.GrpcResultViewResolver;

import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GrpcJobController{

    private Map<Long/*JOB_ID*/, JADStreamObserver> jobs
            = new ConcurrentHashMap<Long, JADStreamObserver>();
//    private Map<Long/*JOB_ID*/, JADStreamObserver> jobs
//            = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    private GrpcResultViewResolver resultViewResolver;

    private Instrumentation instrumentation;

    private TransformerManager transformerManager;

    public GrpcJobController(Instrumentation instrumentation, TransformerManager transformerManager, GrpcResultViewResolver resultViewResolver){
        this.instrumentation = instrumentation;
        this.transformerManager = transformerManager;
        this.resultViewResolver = resultViewResolver;
    }


    public Set<Long> getJobIds(){
        return jobs.keySet();
    }

    public void registerGrpcJob(long jobId,JADStreamObserver jadStreamObserver){
        jobs.put(jobId, jadStreamObserver);
    }

    public void unRegisterGrpcJob(long jobId){
        if(jobs.containsKey(jobId)){
            jobs.remove(jobId);
        }
    }
    public boolean containsJob(long jobId){
        return jobs.containsKey(jobId);
    }

    public JADStreamObserver getGrpcJob(long jobId){
        if(this.containsJob(jobId)){
            return jobs.get(jobId);
        }else {
            return null;
        }
    }

    public int generateGrpcJobId(){
        int jobId = idGenerator.incrementAndGet();
        return jobId;
    }

    public GrpcResultViewResolver getResultViewResolver() {
        return resultViewResolver;
    }

    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public TransformerManager getTransformerManager() {
        return transformerManager;
    }

    public void setTransformerManager(TransformerManager transformerManager) {
        this.transformerManager = transformerManager;
    }
}
