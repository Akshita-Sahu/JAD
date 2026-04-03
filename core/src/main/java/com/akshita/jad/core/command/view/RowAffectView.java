package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.RowAffectModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author gongdewei 2020/4/8
 */
public class RowAffectView extends ResultView<RowAffectModel> {
    @Override
    public void draw(CommandProcess process, RowAffectModel result) {
        process.write(result.affect() + "\n");
    }
}
