package com.akshita.jad.core.shell.command.internal;

import com.akshita.jad.core.shell.term.Term;

/**
 * term
 * 
 * @author gehui 2017726 11:20:00
 */
public class TermHandler extends StdoutHandler {
    private Term term;

    public TermHandler(Term term) {
        this.term = term;
    }

    @Override
    public String apply(String data) {
        term.write(data);
        return data;
    }
}