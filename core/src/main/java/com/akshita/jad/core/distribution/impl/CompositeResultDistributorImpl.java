package com.akshita.jad.core.distribution.impl;

import com.akshita.jad.core.command.model.ResultModel;
import com.akshita.jad.core.distribution.CompositeResultDistributor;
import com.akshita.jad.core.distribution.ResultDistributor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ，
 *
 * @author gongdewei 2020/4/30
 */
public class CompositeResultDistributorImpl implements CompositeResultDistributor {

    private List<ResultDistributor> distributors = Collections.synchronizedList(new ArrayList<ResultDistributor>());

    public CompositeResultDistributorImpl() {
    }

    public CompositeResultDistributorImpl(ResultDistributor ... distributors) {
        for (ResultDistributor distributor : distributors) {
            this.addDistributor(distributor);
        }
    }

    @Override
    public void addDistributor(ResultDistributor distributor) {
        distributors.add(distributor);
    }

    @Override
    public void removeDistributor(ResultDistributor distributor) {
        distributors.remove(distributor);
    }

    @Override
    public void appendResult(ResultModel result) {
        for (ResultDistributor distributor : distributors) {
            distributor.appendResult(result);
        }
    }

    @Override
    public void close() {
        for (ResultDistributor distributor : distributors) {
            distributor.close();
        }
    }
}
