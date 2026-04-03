package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.EnhancerModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * Term view for EnhancerModel
 * @author gongdewei 2020/7/21
 */
public class EnhancerView extends ResultView<EnhancerModel> {
    @Override
    public void draw(CommandProcess process, EnhancerModel result) {
        // ignore enhance result status, judge by the following output
        if (result.getEffect() != null) {
            process.write(ViewRenderUtil.renderEnhancerAffect(result.getEffect()));
        }
    }
}
