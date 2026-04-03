package com.akshita.jad.core.shell.impl;

import com.akshita.jad.core.shell.command.Command;
import com.akshita.jad.core.shell.command.CommandBuilder;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.shell.command.CommandResolver;
import com.akshita.jad.core.shell.command.internal.GrepHandler;
import com.akshita.jad.core.shell.command.internal.PlainTextHandler;
import com.akshita.jad.core.shell.command.internal.WordCountHandler;
import com.akshita.jad.core.shell.handlers.Handler;
import com.akshita.jad.core.shell.handlers.NoOpHandler;

import java.util.Arrays;
import java.util.List;

/**
 * @author beiwei30 on 23/11/2016.
 */
class BuiltinCommandResolver implements CommandResolver {

    private Handler<CommandProcess> handler;

    public BuiltinCommandResolver() {
        this.handler = new NoOpHandler<CommandProcess>();
    }

    @Override
    public List<Command> commands() {
        return Arrays.asList(CommandBuilder.command("exit").processHandler(handler).build(),
                             CommandBuilder.command("quit").processHandler(handler).build(),
                             CommandBuilder.command("jobs").processHandler(handler).build(),
                             CommandBuilder.command("fg").processHandler(handler).build(),
                             CommandBuilder.command("bg").processHandler(handler).build(),
                             CommandBuilder.command("kill").processHandler(handler).build(),
                             CommandBuilder.command(PlainTextHandler.NAME).processHandler(handler).build(),
                             CommandBuilder.command(GrepHandler.NAME).processHandler(handler).build(),
                             CommandBuilder.command(WordCountHandler.NAME).processHandler(handler).build());
    }
}
