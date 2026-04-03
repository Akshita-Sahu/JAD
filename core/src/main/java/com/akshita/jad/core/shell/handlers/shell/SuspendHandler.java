package com.akshita.jad.core.shell.handlers.shell;

import com.akshita.jad.core.shell.impl.ShellImpl;
import com.akshita.jad.core.shell.system.ExecStatus;
import com.akshita.jad.core.shell.system.Job;
import com.akshita.jad.core.shell.term.SignalHandler;
import com.akshita.jad.core.shell.term.Term;

/**
 * @author beiwei30 on 23/11/2016.
 */
public class SuspendHandler implements SignalHandler {

    private ShellImpl shell;

    public SuspendHandler(ShellImpl shell) {
        this.shell = shell;
    }

    @Override
    public boolean deliver(int key) {
        Term term = shell.term();

        Job job = shell.getForegroundJob();
        if (job != null) {
            term.echo(shell.statusLine(job, ExecStatus.STOPPED));
            job.suspend();
        }

        return true;
    }
}
