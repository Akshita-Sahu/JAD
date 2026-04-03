package com.akshita.jad.core.distribution.impl;

import com.akshita.jad.core.command.model.ResultModel;
import com.akshita.jad.core.command.view.ResultView;
import com.akshita.jad.core.command.view.ResultViewResolver;
import com.akshita.jad.core.distribution.ResultDistributor;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * Term/Tty Result Distributor
 *
 * @author gongdewei 2020-03-26
 */
public class TermResultDistributorImpl implements ResultDistributor {

    private final CommandProcess commandProcess;
    private final ResultViewResolver resultViewResolver;

    private final Object outputLock = new Object();

    public TermResultDistributorImpl(CommandProcess commandProcess, ResultViewResolver resultViewResolver) {
        this.commandProcess = commandProcess;
        this.resultViewResolver = resultViewResolver;
    }

    @Override
    public void appendResult(ResultModel model) {
        ResultView resultView = resultViewResolver.getResultView(model);
        if (resultView != null) {
            synchronized (outputLock) {
                resultView.draw(commandProcess, model);
            }
        }
    }

    @Override
    public void close() {
    }

}
