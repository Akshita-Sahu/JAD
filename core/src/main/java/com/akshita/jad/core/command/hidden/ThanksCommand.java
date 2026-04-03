package com.akshita.jad.core.command.hidden;

import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.JADBanner;
import com.akshita_sahu.middleware.cli.annotations.Hidden;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Summary;

/**
 * <br/>
 * 
 *
 * @author vlinux on 15/9/1.
 */
@Name("thanks")
@Summary("Credits to all personnel and organization who either contribute or help to this product. Thanks you all!")
@Hidden
public class ThanksCommand extends AnnotatedCommand {
    @Override
    public void process(CommandProcess process) {
        process.write(JADBanner.credit()).write("\n").end();
    }
}
