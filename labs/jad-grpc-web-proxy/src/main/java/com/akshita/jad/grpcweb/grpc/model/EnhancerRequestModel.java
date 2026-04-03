package com.akshita.jad.grpcweb.grpc.model;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.advisor.AdviceListener;
import com.akshita.jad.core.advisor.InvokeTraceable;
import com.akshita.jad.core.command.model.EnhancerModel;
import com.akshita.jad.core.command.monitor200.AbstractTraceAdviceListener;
import com.akshita.jad.core.util.LogUtil;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.util.affect.EnhancerAffect;
import com.akshita.jad.core.util.matcher.Matcher;
import com.akshita.jad.core.view.Ansi;
import com.akshita.jad.grpcweb.grpc.observer.JADStreamObserver;
import com.akshita.jad.grpcweb.grpc.service.advisor.Enhancer;

import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.List;

public abstract class EnhancerRequestModel {

    private static final Logger logger = LoggerFactory.getLogger(EnhancerRequestModel.class);
    protected static final List<String> EMPTY = Collections.emptyList();
    public static final String[] EXPRESS_EXAMPLES = { "params", "returnObj", "throwExp", "target", "clazz", "method",
            "{params,returnObj}", "params[0]" };
    protected String excludeClassPattern;

    protected Matcher classNameMatcher;
    protected Matcher classNameExcludeMatcher;
    protected Matcher methodNameMatcher;

    protected long jobId;
    protected long listenerId;

    protected boolean verbose;

    protected int maxNumOfMatchedClass;

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
    protected abstract AdviceListener getAdviceListener(JADStreamObserver jadStreamObserver);


    public void enhance(JADStreamObserver jadStreamObserver) {
        EnhancerAffect effect = null;
        try {
            Instrumentation inst = jadStreamObserver.getInstrumentation();
            AdviceListener listener = getAdviceListener(jadStreamObserver);
            if (listener == null) {
                logger.error("advice listener is null");
                String msg = "advice listener is null, check jad log";
//                jadStreamObserver.appendResult(new EnhancerModel(effect, false, msg));
                jadStreamObserver.end(-1, msg);
                return;
            }
            boolean skipJDKTrace = false;
            if(listener instanceof AbstractTraceAdviceListener) {
                skipJDKTrace = ((AbstractTraceAdviceListener) listener).getCommand().isSkipJDKTrace();
            }

            Enhancer enhancer = new Enhancer(listener, listener instanceof InvokeTraceable, skipJDKTrace, getClassNameMatcher(), getClassNameExcludeMatcher(), getMethodNameMatcher());
            // 
            jadStreamObserver.register(listener, enhancer);
            effect = enhancer.enhance(inst, this.maxNumOfMatchedClass);
            if (effect.getThrowable() != null) {
                String msg = "error happens when enhancing class: "+effect.getThrowable().getMessage();
//                jadStreamObserver.appendResult(new EnhancerModel(effect, false, msg));
                jadStreamObserver.end(-1, msg + ", check jad log: " + LogUtil.loggingFile());
                return;
            }

            if (effect.cCnt() == 0 || effect.mCnt() == 0) {
                // no class effected
                if (!StringUtils.isEmpty(effect.getOverLimitMsg())) {
                    String msg = "no class effected";
//                    jadStreamObserver.appendResult(new EnhancerModel(effect, false));
                    jadStreamObserver.end(-1, msg);
                    return;
                }
                // might be method code too large
//                jadStreamObserver.appendResult(new EnhancerModel(effect, false, "No class or method is affected"));

                String smCommand = Ansi.ansi().fg(Ansi.Color.GREEN).a("sm CLASS_NAME METHOD_NAME").reset().toString();
                String optionsCommand = Ansi.ansi().fg(Ansi.Color.GREEN).a("options unsafe true").reset().toString();
                String javaPackage = Ansi.ansi().fg(Ansi.Color.GREEN).a("java.*").reset().toString();
                String resetCommand = Ansi.ansi().fg(Ansi.Color.GREEN).a("reset CLASS_NAME").reset().toString();
                String logStr = Ansi.ansi().fg(Ansi.Color.GREEN).a(LogUtil.loggingFile()).reset().toString();
                String issueStr = Ansi.ansi().fg(Ansi.Color.GREEN).a("https://github.com/akshita-sahu/jad/issues/47").reset().toString();
                String msg = "No class or method is affected, try:\n"
                        + "1. Execute `" + smCommand + "` to make sure the method you are tracing actually exists (it might be in your parent class).\n"
                        + "2. Execute `" + optionsCommand + "`, if you want to enhance the classes under the `" + javaPackage + "` package.\n"
                        + "3. Execute `" + resetCommand + "` and try again, your method body might be too large.\n"
                        + "4. Match the constructor, use `<init>`, for example: `watch demo.MathGame <init>`\n"
                        + "5. Check jad log: " + logStr + "\n"
                        + "6. Visit " + issueStr + " for more details.";
                jadStreamObserver.end(-1, msg);
                return;
            }
            jadStreamObserver.appendResult(new EnhancerModel(effect, true));

            //，RpcAdviceListener
        } catch (Throwable e) {
            String msg = "error happens when enhancing class: "+e.getMessage();
            logger.error(msg, e);
//            jadStreamObserver.appendResult(new EnhancerModel(effect, false, msg));
            jadStreamObserver.end(-1, msg);
        }
    }

}
