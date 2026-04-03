package com.akshita.jad.core.command.monitor200;

import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.advisor.AdviceListener;
import com.akshita.jad.core.advisor.AdviceWeaver;
import com.akshita.jad.core.advisor.Enhancer;
import com.akshita.jad.core.advisor.InvokeTraceable;
import com.akshita.jad.core.command.model.EnhancerModel;
import com.akshita.jad.core.command.model.EnhancerModelFactory;
import com.akshita.jad.core.server.JADBootstrap;
import com.akshita.jad.core.shell.cli.Completion;
import com.akshita.jad.core.shell.cli.CompletionUtils;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.shell.handlers.command.CommandInterruptHandler;
import com.akshita.jad.core.shell.handlers.shell.QExitHandler;
import com.akshita.jad.core.shell.session.Session;
import com.akshita.jad.core.util.Constants;
import com.akshita.jad.core.util.LogUtil;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.util.affect.EnhancerAffect;
import com.akshita.jad.core.util.matcher.Matcher;
import com.akshita.jad.core.view.Ansi;
import com.akshita_sahu.middleware.cli.annotations.DefaultValue;
import com.akshita_sahu.middleware.cli.annotations.Description;
import com.akshita_sahu.middleware.cli.annotations.Option;

/**
 * @author beiwei30 on 29/11/2016.
 */
public abstract class EnhancerCommand extends AnnotatedCommand {

    private static final Logger logger = LoggerFactory.getLogger(EnhancerCommand.class);
    protected static final List<String> EMPTY = Collections.emptyList();
    public static final String[] EXPRESS_EXAMPLES = { "params", "returnObj", "throwExp", "target", "clazz", "method",
                                                       "{params,returnObj}", "params[0]" };
    private String excludeClassPattern;

    protected Matcher classNameMatcher;
    protected Matcher classNameExcludeMatcher;
    protected Matcher methodNameMatcher;

    protected long listenerId;

    protected boolean verbose;

    protected int maxNumOfMatchedClass;

    protected Long timeout;

    protected boolean lazy = false;

    /**
     *  classloader hash， classloader 。
     */
    protected String hashCode;

    @Option(longName = "exclude-class-pattern")
    @Description("exclude class name pattern, use either '.' or '/' as separator")
    public void setExcludeClassPattern(String excludeClassPattern) {
        this.excludeClassPattern = excludeClassPattern;
    }

    @Option(longName = "classloader")
    @Description("The hash code of the special class's classLoader")
    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return hashCode;
    }

    @Option(longName = "listenerId")
    @Description("The special listenerId")
    public void setListenerId(long listenerId) {
        this.listenerId = listenerId;
    }

    @Option(shortName = "v", longName = "verbose", flag = true)
    @Description("Enables print verbose information, default value false.")
    public void setVerbosee(boolean verbose) {
        this.verbose = verbose;
    }

    @Option(shortName = "m", longName = "maxMatch")
    @DefaultValue("50")
    @Description("The maximum of matched class.")
    public void setMaxNumOfMatchedClass(int maxNumOfMatchedClass) {
        this.maxNumOfMatchedClass = maxNumOfMatchedClass;
    }

    @Option(longName = "timeout")
    @Description("Timeout value in seconds for the command to exit automatically.")
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Long getTimeout() {
        return timeout;
    }

    @Option(shortName = "L", longName = "lazy", flag = true)
    @Description("Enable lazy mode to enhance classes when they are loaded. Useful when the class is not loaded yet.")
    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public boolean isLazy() {
        return lazy;
    }

    /**
     * 
     *
     * @return 
     */
    protected abstract Matcher getClassNameMatcher();

    /**
     * 
     */
    protected abstract Matcher getClassNameExcludeMatcher();

    /**
     * 
     *
     * @return 
     */
    protected abstract Matcher getMethodNameMatcher();

    /**
     * 
     *
     * @return 
     */
    protected abstract AdviceListener getAdviceListener(CommandProcess process);

    AdviceListener getAdviceListenerWithId(CommandProcess process) {
        if (listenerId != 0) {
            AdviceListener listener = AdviceWeaver.listener(listenerId);
            if (listener != null) {
                return listener;
            }
        }
        return getAdviceListener(process);
    }
    @Override
    public void process(final CommandProcess process) {
        // ctrl-C support
        process.interruptHandler(new CommandInterruptHandler(process));
        // q exit support
        process.stdinHandler(new QExitHandler(process));

        // start to enhance
        enhance(process);
    }

    @Override
    public void complete(Completion completion) {
        int argumentIndex = CompletionUtils.detectArgumentIndex(completion);

        if (argumentIndex == 1) { // class name
            if (!CompletionUtils.completeClassName(completion)) {
                super.complete(completion);
            }
            return;
        } else if (argumentIndex == 2) { // method name
            if (!CompletionUtils.completeMethodName(completion)) {
                super.complete(completion);
            }
            return;
        } else if (argumentIndex == 3) { // watch express
            completeArgument3(completion);
            return;
        }

        super.complete(completion);
    }

    protected void enhance(CommandProcess process) {
        Session session = process.session();
        if (!session.tryLock()) {
            String msg = "someone else is enhancing classes, pls. wait.";
            process.appendResult(EnhancerModelFactory.create(null, false, msg));
            process.end(-1, msg);
            return;
        }
        EnhancerAffect effect = null;
        int lock = session.getLock();
        try {
            Instrumentation inst = session.getInstrumentation();
            AdviceListener listener = getAdviceListenerWithId(process);
            if (listener == null) {
                logger.error("advice listener is null");
                String msg = "advice listener is null, check jad log";
                process.appendResult(EnhancerModelFactory.create(effect, false, msg));
                process.end(-1, msg);
                return;
            }
            boolean skipJDKTrace = false;
            if(listener instanceof AbstractTraceAdviceListener) {
                skipJDKTrace = ((AbstractTraceAdviceListener) listener).getCommand().isSkipJDKTrace();
            }

            Enhancer enhancer = new Enhancer(listener, listener instanceof InvokeTraceable, skipJDKTrace,
                    getClassNameMatcher(), getClassNameExcludeMatcher(), getMethodNameMatcher(), this.lazy, this.hashCode);
            // 
            process.register(listener, enhancer);
            effect = enhancer.enhance(inst, this.maxNumOfMatchedClass);

            if (effect.getThrowable() != null) {
                String msg = "error happens when enhancing class: "+effect.getThrowable().getMessage();
                process.appendResult(EnhancerModelFactory.create(effect, false, msg));
                process.end(1, msg + ", check jad log: " + LogUtil.loggingFile());
                return;
            }

            if (effect.cCnt() == 0 || effect.mCnt() == 0) {
                // no class effected
                if (!StringUtils.isEmpty(effect.getOverLimitMsg())) {
                    process.appendResult(EnhancerModelFactory.create(effect, false));
                    process.end(-1);
                    return;
                }
                
                // ：，
                if (this.lazy) {
                    String lazyMsg = "Lazy mode is enabled, waiting for class to be loaded. Press Q or Ctrl+C to abort.\n"
                            + "When the target class is loaded, it will be automatically enhanced.";
                    process.write(lazyMsg + "\n");
                } else {
                    // might be method code too large
                    process.appendResult(EnhancerModelFactory.create(effect, false, "No class or method is affected"));

                    String smCommand = Ansi.ansi().fg(Ansi.Color.GREEN).a("sm CLASS_NAME METHOD_NAME").reset().toString();
                    String optionsCommand = Ansi.ansi().fg(Ansi.Color.GREEN).a("options unsafe true").reset().toString();
                    String javaPackage = Ansi.ansi().fg(Ansi.Color.GREEN).a("java.*").reset().toString();
                    String resetCommand = Ansi.ansi().fg(Ansi.Color.GREEN).a("reset CLASS_NAME").reset().toString();
                    String logStr = Ansi.ansi().fg(Ansi.Color.GREEN).a(LogUtil.loggingFile()).reset().toString();
                    String issueStr = Ansi.ansi().fg(Ansi.Color.GREEN).a("https://github.com/akshita_sahu/jad/issues/47").reset().toString();
                    String msg = "No class or method is affected, try:\n"
                            + "1. Execute `" + smCommand + "` to make sure the method you are tracing actually exists (it might be in your parent class).\n"
                            + "2. Execute `" + optionsCommand + "`, if you want to enhance the classes under the `" + javaPackage + "` package.\n"
                            + "3. Execute `" + resetCommand + "` and try again, your method body might be too large.\n"
                            + "4. Match the constructor, use `<init>`, for example: `watch demo.MathGame <init>`\n"
                            + "5. Check jad log: " + logStr + "\n"
                            + "6. Visit " + issueStr + " for more details.\n"
                            + "7. If the class is not loaded yet, try to use `--lazy` or `-L` option to enable lazy mode.";
                    process.end(-1, msg);
                    return;
                }
            }

            // ,enhance,unLock,
            if (session.getLock() == lock) {
                if (process.isForeground()) {
                    process.echoTips(Constants.Q_OR_CTRL_C_ABORT_MSG + "\n");
                }
            }

            process.appendResult(EnhancerModelFactory.create(effect, true));

            // 
            scheduleTimeoutTask(process);

            //，AdviceListener
        } catch (Throwable e) {
            String msg = "error happens when enhancing class: "+e.getMessage();
            logger.error(msg, e);
            process.appendResult(EnhancerModelFactory.create(effect, false, msg));
            process.end(-1, msg);
        } finally {
            if (session.getLock() == lock) {
                // enhance
                process.session().unLock();
            }
        }
    }

    protected void completeArgument3(Completion completion) {
        super.complete(completion);
    }

    public String getExcludeClassPattern() {
        return excludeClassPattern;
    }

    /**
     * Schedule a timeout task to end the command after the specified timeout.
     *
     * @param process the command process
     */
    private void scheduleTimeoutTask(final CommandProcess process) {
        if (timeout == null || timeout <= 0) {
            return;
        }

        final ScheduledFuture<?> timeoutFuture = JADBootstrap.getInstance().getScheduledExecutorService()
                .schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (process.isRunning()) {
                            process.write("Command execution timeout after " + timeout + " seconds.\n");
                            process.end();
                        }
                    }
                }, timeout, TimeUnit.SECONDS);

        // Cancel the timeout task if the process ends normally
        process.endHandler(new com.akshita.jad.core.shell.handlers.Handler<Void>() {
            @Override
            public void handle(Void event) {
                timeoutFuture.cancel(false);
            }
        });
    }
}
