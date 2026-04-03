package com.akshita.jad.core.command.basic1000;


import com.akshita.jad.core.command.model.VersionModel;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.JADBanner;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;

/**
 * 
 *
 * @author vlinux
 */
@Name("version")
@Summary("Display JAD version")
public class VersionCommand extends AnnotatedCommand {

    @Override
    public void process(CommandProcess process) {
        VersionModel result = new VersionModel();
        result.setVersion(JADBanner.version());
        process.appendResult(result);
        process.end();
    }

}
