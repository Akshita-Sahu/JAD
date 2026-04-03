package com.akshita.jad.core.util;

import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.shell.command.ExitStatus;

/**
 * Command Process util
 */
public class CommandUtils {

    /**
     * check exit status and end command processing
     * @param process CommandProcess instance
     * @param status ExitStatus of command
     */
    public static void end(CommandProcess process, ExitStatus status) {
        if (status != null) {
            process.end(status.getStatusCode(), status.getMessage());
        } else {
            process.end(-1, "process error, exit status is null");
        }
    }

}
