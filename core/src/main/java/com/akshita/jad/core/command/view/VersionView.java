package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.VersionModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author gongdewei 2020/3/27
 */
public class VersionView extends ResultView<VersionModel> {

    @Override
    public void draw(CommandProcess process, VersionModel result) {
        writeln(process, result.getVersion());
    }

}
