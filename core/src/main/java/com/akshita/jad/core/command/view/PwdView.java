package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.PwdModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author gongdewei 2020/5/11
 */
public class PwdView extends ResultView<PwdModel> {
    @Override
    public void draw(CommandProcess process, PwdModel result) {
        process.write(result.getWorkingDir()).write("\n");
    }
}
