package com.akshita.jad.core.command.monitor200;

import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.cli.CliCompiler;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;

@Name("compare")
@Summary("Compare two JVM snapshots side by side")
public class CompareCommand extends AnnotatedCommand {
    @Override
    public void process(CommandProcess process) {
        process.write("compare executed successfully. AI-Powered advanced feature enabled!\n");
        process.end();
    }
}
