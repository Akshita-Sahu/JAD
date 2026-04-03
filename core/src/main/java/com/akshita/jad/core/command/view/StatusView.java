package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.StatusModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author gongdewei 2020/3/27
 */
public class StatusView extends ResultView<StatusModel> {

    @Override
    public void draw(CommandProcess process, StatusModel result) {
        if (result.getMessage() != null) {
            writeln(process, result.getMessage());
        }
    }

}
