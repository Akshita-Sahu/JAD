package com.akshita.jad.core.command.basic1000;

import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;
import com.akshita_sahu.text.util.RenderUtil;

@Name("cls")
@Summary("Clear the screen")
public class ClsCommand extends AnnotatedCommand {
    @Override
    public void process(CommandProcess process) {
        if (!process.session().isTty()) {
            process.end(-1, "Command 'cls' is only support tty session.");
            return;
        }
        process.write(RenderUtil.cls()).write("\n");
        process.end();
    }
}
