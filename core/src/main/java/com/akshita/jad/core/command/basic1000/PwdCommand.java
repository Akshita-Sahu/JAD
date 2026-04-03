package com.akshita.jad.core.command.basic1000;

import java.io.File;

import com.akshita.jad.core.command.model.PwdModel;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;

@Name("pwd")
@Summary("Return working directory name")
public class PwdCommand extends AnnotatedCommand {
    @Override
    public void process(CommandProcess process) {
        String path = new File("").getAbsolutePath();
        process.appendResult(new PwdModel(path));
        process.end();
    }
}
