package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.MessageModel;
import com.akshita.jad.core.shell.command.CommandProcess;

/**
 * @author gongdewei 2020/4/2
 */
public class MessageView extends ResultView<MessageModel> {
    @Override
    public void draw(CommandProcess process, MessageModel result) {
        writeln(process, result.getMessage());
    }
}
