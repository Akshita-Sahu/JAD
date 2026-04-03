package com.akshita.jad.core.shell;

import com.akshita.jad.core.shell.command.CommandResolver;
import com.akshita.jad.core.shell.future.Future;
import com.akshita.jad.core.shell.handlers.Handler;
import com.akshita.jad.core.shell.handlers.NoOpHandler;
import com.akshita.jad.core.shell.impl.ShellServerImpl;
import com.akshita.jad.core.shell.system.impl.InternalCommandManager;
import com.akshita.jad.core.shell.system.impl.JobControllerImpl;
import com.akshita.jad.core.shell.term.Term;
import com.akshita.jad.core.shell.term.TermServer;

/**
 * The shell server.<p/>
 * <p>
 * A shell server is associated with a collection of {@link TermServer term servers}: the {@link #registerTermServer(TermServer)}
 * method registers a term server. Term servers life cycle are managed by this server.<p/>
 * <p>
 * When a {@link TermServer term server} receives an incoming connection, a {@link com.akshita.jad.core.shell.system.JobController} instance is created and
 * associated with this connection.<p/>
 * <p>
 * The {@link #createShell()} method can be used to create {@link com.akshita.jad.core.shell.system.JobController} instance for testing purposes.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class ShellServer {

    /**
     * Create a new shell server with default options.
     *
     * @param options the options
     * @return the created shell server
     */
    public static ShellServer create(ShellServerOptions options) {
        return new ShellServerImpl(options);
    }

    /**
     * Create a new shell server with specific options.
     *
     * @return the created shell server
     */
    public static ShellServer create() {
        return new ShellServerImpl(new ShellServerOptions());
    }

    /**
     * Register a command resolver for this server.
     *
     * @param resolver the resolver
     * @return a reference to this, so the API can be used fluently
     */
    public abstract ShellServer registerCommandResolver(CommandResolver resolver);

    /**
     * Register a term server to this shell server, the term server lifecycle methods are managed by this shell server.
     *
     * @param termServer the term server to add
     * @return a reference to this, so the API can be used fluently
     */
    public abstract ShellServer registerTermServer(TermServer termServer);

    /**
     * Create a new shell, the returned shell should be closed explicitly.
     *
     * @param term the shell associated terminal
     * @return the created shell
     */
    public abstract Shell createShell(Term term);

    /**
     * Create a new shell, the returned shell should be closed explicitly.
     *
     * @return the created shell
     */
    public abstract Shell createShell();

    /**
     * Start the shell service, this is an asynchronous start.
     */
    public ShellServer listen() {
        return listen(new NoOpHandler<Future<Void>>());
    }

    /**
     * Start the shell service, this is an asynchronous start.
     *
     * @param listenHandler handler for getting notified when service is started
     */
    public abstract ShellServer listen(Handler<Future<Void>> listenHandler);

    /**
     * Close the shell server, this is an asynchronous close.
     */
    public void close() {
        close(new NoOpHandler<Future<Void>>());
    }

    /**
     * Close the shell server, this is an asynchronous close.
     *
     * @param completionHandler handler for getting notified when service is stopped
     */
    public abstract void close(Handler<Future<Void>> completionHandler);

    /**
     * @return global job controller instance
     */
    public abstract JobControllerImpl getJobController();

    /**
     * @return get command manager instance
     */
    public abstract InternalCommandManager getCommandManager();
}
