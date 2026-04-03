package com.akshita.jad.grpcweb.grpc.model;

import com.akshita.jad.core.command.model.WatchModel;

public class WatchResponseModel extends WatchModel {

    private long resultId;

    public long getResultId() {
        return resultId;
    }

    public void setResultId(long resultId) {
        this.resultId = resultId;
    }
}
