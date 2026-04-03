package com.akshita.jad.core.distribution;

import com.akshita.jad.core.command.model.ResultModel;

import java.util.List;

public interface PackingResultDistributor extends ResultDistributor {

    /**
     * Get results of command
     */
    List<ResultModel> getResults();

}
