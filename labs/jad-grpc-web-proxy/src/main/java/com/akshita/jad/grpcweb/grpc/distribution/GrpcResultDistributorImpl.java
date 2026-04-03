package com.akshita.jad.grpcweb.grpc.distribution;

import com.akshita.jad.core.command.model.ResultModel;
import com.akshita.jad.core.distribution.ResultDistributor;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;
import com.akshita.jad.grpcweb.grpc.view.GrpcResultView;
import com.akshita.jad.grpcweb.grpc.view.GrpcResultViewResolver;


public class GrpcResultDistributorImpl implements ResultDistributor {

    private final JADStreamObserver jadStreamObserver;

    private final GrpcResultViewResolver grpcResultViewResolver;

    public GrpcResultDistributorImpl(JADStreamObserver jadStreamObserver, GrpcResultViewResolver resultViewResolver) {
        this.jadStreamObserver = jadStreamObserver;
        this.grpcResultViewResolver = resultViewResolver;
    }

    @Override
    public void appendResult(ResultModel model) {
        GrpcResultView resultView = grpcResultViewResolver.getResultView(model);
        if (resultView != null) {
            resultView.draw(jadStreamObserver, model);
        }
    }

    @Override
    public void close() {

    }
}
