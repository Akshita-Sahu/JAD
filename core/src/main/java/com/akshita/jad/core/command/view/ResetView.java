package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.ResetModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author gongdewei 2020/6/22
 */
public class ResetView extends ResultView<ResetModel> {

    @Override
    public void draw(CommandProcess process, ResetModel result) {
        process.write(ViewRenderUtil.renderEnhancerAffect(result.getAffect()));
    }

}
