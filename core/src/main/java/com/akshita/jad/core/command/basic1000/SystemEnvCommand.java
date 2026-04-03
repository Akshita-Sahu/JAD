package com.akshita.jad.core.command.basic1000;

import com.akshita.jad.core.command.Constants;
import com.akshita.jad.core.command.model.SystemEnvModel;
import com.akshita.jad.core.shell.cli.Completion;
import com.akshita.jad.core.shell.cli.CompletionUtils;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.StringUtils;
import com.akshita_sahu.middleware.cli.annotations.Argument;
import com.akshita_sahu.middleware.cli.annotations.Description;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;

/**
 * @author hengyunabc 2018-11-09
 *
 */
@Name("sysenv")
@Summary("Display the system env.")
@Description(Constants.EXAMPLE + "  sysenv\n" + "  sysenv USER\n" + Constants.WIKI + Constants.WIKI_HOME + "sysenv")
public class SystemEnvCommand extends AnnotatedCommand {

    private String envName;

    @Argument(index = 0, argName = "env-name", required = false)
    @Description("env name")
    public void setOptionName(String envName) {
        this.envName = envName;
    }

    @Override
    public void process(CommandProcess process) {
        try {
            SystemEnvModel result = new SystemEnvModel();
            if (StringUtils.isBlank(envName)) {
                // show all system env
                result.putAll(System.getenv());
            } else {
                // view the specified system env
                String value = System.getenv(envName);
                result.put(envName, value);
            }
            process.appendResult(result);
            process.end();
        } catch (Throwable t) {
            process.end(-1, "Error during setting system env: " + t.getMessage());
        }
    }

    /**
     * First, try to complete with the sysenv command scope. If completion is
     * failed, delegates to super class.
     *
     * @param completion
     *            the completion object
     */
    @Override
    public void complete(Completion completion) {
        CompletionUtils.complete(completion, System.getenv().keySet());
    }

}
