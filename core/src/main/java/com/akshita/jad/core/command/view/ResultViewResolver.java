package com.akshita.jad.core.command.view;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.command.model.ResultModel;
import com.akshita.jad.core.shell.command.CommandProcess;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Result view resolver for term
 *
 * @author gongdewei 2020/3/27
 */
public class ResultViewResolver {
    private static final Logger logger = LoggerFactory.getLogger(ResultViewResolver.class);

    // modelClass -> view
    private Map<Class, ResultView> resultViewMap = new ConcurrentHashMap<Class, ResultView>();

    public ResultViewResolver() {
        initResultViews();
    }

    /**
     * ResultView
     */
    private void initResultViews() {
        try {
            registerView(RowAffectView.class);

            //basic1000
            registerView(StatusView.class);
            registerView(VersionView.class);
            registerView(MessageView.class);
            registerView(HelpView.class);
            //registerView(HistoryView.class);
            registerView(EchoView.class);
            registerView(CatView.class);
            registerView(Base64View.class);
            registerView(OptionsView.class);
            registerView(SystemPropertyView.class);
            registerView(SystemEnvView.class);
            registerView(PwdView.class);
            registerView(VMOptionView.class);
            registerView(SessionView.class);
            registerView(ResetView.class);
            registerView(ShutdownView.class);

            //klass100
            registerView(ClassLoaderView.class);
            registerView(DumpClassView.class);
            registerView(GetStaticView.class);
            registerView(JadView.class);
            registerView(MemoryCompilerView.class);
            registerView(OgnlView.class);
            registerView(RedefineView.class);
            registerView(RetransformView.class);
            registerView(SearchClassView.class);
            registerView(SearchMethodView.class);

            //logger
            registerView(LoggerView.class);

            //monitor2000
            registerView(DashboardView.class);
            registerView(JvmView.class);
            registerView(MemoryView.class);
            registerView(MBeanView.class);
            registerView(PerfCounterView.class);
            registerView(ThreadView.class);
            registerView(ProfilerView.class);
            registerView(EnhancerView.class);
            registerView(MonitorView.class);
            registerView(StackView.class);
            registerView(TimeTunnelView.class);
            registerView(TraceView.class);
            registerView(WatchView.class);
            registerView(VmToolView.class);
            registerView(JFRView.class);

        } catch (Throwable e) {
            logger.error("register result view failed", e);
        }
    }

    public ResultView getResultView(ResultModel model) {
        return resultViewMap.get(model.getClass());
    }

    public ResultViewResolver registerView(Class modelClass, ResultView view) {
        //TODO modeltype，bug
        this.resultViewMap.put(modelClass, view);
        return this;
    }

    public ResultViewResolver registerView(ResultView view) {
        Class modelClass = getModelClass(view);
        if (modelClass == null) {
            throw new NullPointerException("model class is null");
        }
        return this.registerView(modelClass, view);
    }

    public void registerView(Class<? extends ResultView> viewClass) {
        ResultView view = null;
        try {
            view = viewClass.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException("create view instance failure, viewClass:" + viewClass, e);
        }
        this.registerView(view);
    }

    /**
     * Get model class of result view
     *
     * @return
     */
    public static <V extends ResultView> Class getModelClass(V view) {
        //drawResultModel
        Class<? extends ResultView> viewClass = view.getClass();
        Method[] declaredMethods = viewClass.getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            Method method = declaredMethods[i];
            if (method.getName().equals("draw")) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 2
                        && parameterTypes[0] == CommandProcess.class
                        && parameterTypes[1] != ResultModel.class
                        && ResultModel.class.isAssignableFrom(parameterTypes[1])) {
                    return parameterTypes[1];
                }
            }
        }
        return null;
    }
}
