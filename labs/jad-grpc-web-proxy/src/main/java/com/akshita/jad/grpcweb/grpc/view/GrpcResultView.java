package com.akshita.jad.grpcweb.grpc.view;

import com.akshita.jad.core.command.model.ResultModel;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;

/**
 * Command result view for grpc client.
 * Note: Result view is a reusable and stateless instance
 *
 * @author xuyang 2023/8/15
 */
public abstract class GrpcResultView<T extends ResultModel> {

    /**
     * formatted printing data to grpc client
     *
     * @param jadStreamObserver
     */
    public abstract void draw(JADStreamObserver jadStreamObserver, T result);

}
