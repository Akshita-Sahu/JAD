package com.akshita.jad.core.command.basic1000;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.command.model.MessageModel;
import com.akshita.jad.core.command.model.ResetModel;
import com.akshita.jad.core.command.model.ShutdownModel;
import com.akshita.jad.core.server.JADBootstrap;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.affect.EnhancerAffect;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;

/**
 * @author hengyunabc 2019-07-05
 */
@Name("stop")
@Summary("Stop/Shutdown JAD server and exit the console.")
public class StopCommand extends AnnotatedCommand {
    private static final Logger logger = LoggerFactory.getLogger(StopCommand.class);
    @Override
    public void process(CommandProcess process) {
        shutdown(process);
    }
    private static void shutdown(CommandProcess process) {
        JADBootstrap jadBootstrap = JADBootstrap.getInstance();
        try {
            // 
            process.appendResult(new MessageModel("Resetting all enhanced classes ..."));
            EnhancerAffect enhancerAffect = jadBootstrap.reset();
            process.appendResult(new ResetModel(enhancerAffect));
            process.appendResult(new ShutdownModel(true, "JAD Server is going to shutdown..."));
        } catch (Throwable e) {
            logger.error("An error occurred when stopping jad server.", e);
            process.appendResult(new ShutdownModel(false, "An error occurred when stopping jad server."));
        } finally {
            process.end();
            jadBootstrap.destroy();
        }
    }
}
