package com.akshita.jad.core.shell.term.impl;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.shell.cli.CliToken;
import com.akshita.jad.core.shell.cli.CliTokens;
import com.akshita.jad.core.shell.handlers.Handler;
import com.akshita.jad.core.shell.session.Session;

import io.termd.core.function.Consumer;
import io.termd.core.readline.Completion;

import java.util.Collections;
import java.util.List;

/**
 * @author beiwei30 on 23/11/2016.
 */
class CompletionHandler implements Consumer<Completion> {
    private static final Logger logger = LoggerFactory.getLogger(CompletionHandler.class);
    private final Handler<com.akshita.jad.core.shell.cli.Completion> completionHandler;
    private final Session session;

    public CompletionHandler(Handler<com.akshita.jad.core.shell.cli.Completion> completionHandler, Session session) {
        this.completionHandler = completionHandler;
        this.session = session;
    }

    @Override
    public void accept(final Completion completion) {
        try {
            final String line = io.termd.core.util.Helper.fromCodePoints(completion.line());
            final List<CliToken> tokens = Collections.unmodifiableList(CliTokens.tokenize(line));
            com.akshita.jad.core.shell.cli.Completion comp = new CompletionAdaptor(line, tokens, completion, session);
            completionHandler.handle(comp);
        } catch (Throwable t) {
            // t.printStackTrace();
            logger.error("completion error", t);
        }
    }
}
