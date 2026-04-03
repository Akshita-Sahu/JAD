package com.akshita.jad.core.shell.handlers;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.shell.future.Future;

/**
 * @author beiwei30 on 22/11/2016.
 */
public class NoOpHandler<E> implements Handler<E> {

    private static final Logger logger = LoggerFactory.getLogger(NoOpHandler.class);

    @Override
    public void handle(E event) {
        if (event instanceof Future && ((Future) event).failed()) {
            logger.error("Error listening term server:", ((Future) event).cause());
        }
    }
}
