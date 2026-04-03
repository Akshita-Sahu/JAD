package com.akshita.jad.core.command.basic1000;

import com.akshita.jad.core.command.Constants;
import com.akshita.jad.core.command.model.EchoModel;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita_sahu.middleware.cli.annotations.Argument;
import com.akshita_sahu.middleware.cli.annotations.Description;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;

/**
 * 
 * @author hengyunabc
 *
 */
@Name("echo")
@Summary("write arguments to the standard output")
@Description("\nExamples:\n" +
        "  echo 'abc'\n" +
        Constants.WIKI + Constants.WIKI_HOME + "echo")
public class EchoCommand extends AnnotatedCommand {
    private String message;

    @Argument(argName = "message", index = 0, required = false)
    @Description("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void process(CommandProcess process) {
        if (message != null) {
            process.appendResult(new EchoModel(message));
        }

        process.end();
    }

}
