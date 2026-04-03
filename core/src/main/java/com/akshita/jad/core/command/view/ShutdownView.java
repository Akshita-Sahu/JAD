package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.ShutdownModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author gongdewei 2020/6/22
 */
public class ShutdownView extends ResultView<ShutdownModel> {
    @Override
    public void draw(CommandProcess process, ShutdownModel result) {
        process.write(result.getMessage()).write("\n");
    }
}
