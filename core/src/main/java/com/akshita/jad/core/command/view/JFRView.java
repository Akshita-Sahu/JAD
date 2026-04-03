package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.JFRModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author longxu 2022/7/25
 */
public class JFRView extends ResultView<JFRModel>{
    @Override
    public void draw(CommandProcess process, JFRModel result) {
        writeln(process, result.getJfrOutput());
    }
}
