package com.akshita.jad.core.distribution.impl;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita_sahu.fastjson2.JSON;
import com.akshita.jad.core.command.model.ResultModel;
import com.akshita.jad.core.distribution.PackingResultDistributor;
import com.akshita.jad.core.shell.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PackingResultDistributorImpl implements PackingResultDistributor {
    private static final Logger logger = LoggerFactory.getLogger(PackingResultDistributorImpl.class);

    private BlockingQueue<ResultModel> resultQueue = new ArrayBlockingQueue<ResultModel>(500);
    private final Session session;

    public PackingResultDistributorImpl(Session session) {
        this.session = session;
    }

    @Override
    public void appendResult(ResultModel result) {
        if (!resultQueue.offer(result)) {
            logger.warn("result queue is full: {}, discard later result: {}", resultQueue.size(), JSON.toJSONString(result));
        }
    }

    @Override
    public void close() {
    }

    @Override
    public List<ResultModel> getResults() {
        ArrayList<ResultModel> results = new ArrayList<ResultModel>(resultQueue.size());
        resultQueue.drainTo(results);
        return results;
    }

}
