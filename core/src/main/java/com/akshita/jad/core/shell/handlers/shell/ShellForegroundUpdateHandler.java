package com.akshita.jad.core.shell.handlers.shell;

import com.akshita.jad.core.shell.handlers.Handler;
import com.akshita.jad.core.shell.impl.ShellImpl;
import com.akshita.jad.core.shell.system.Job;

/**
 * @author beiwei30 on 23/11/2016.
 */
public class ShellForegroundUpdateHandler implements Handler<Job> {
    private ShellImpl shell;

    public ShellForegroundUpdateHandler(ShellImpl shell) {
        this.shell = shell;
    }

    @Override
    public void handle(Job job) {
        if (job == null) {
            shell.readline();
        }
    }
}
