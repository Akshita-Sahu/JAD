package com.akshita.jad.grpcweb.grpc.service.advisor;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.common.concurrent.ConcurrentWeakKeyHashMap;
import com.akshita.jad.core.advisor.AdviceListener;
import com.akshita.jad.core.shell.system.ExecStatus;
import com.akshita.jad.core.shell.system.Process;
import com.akshita.jad.core.shell.system.ProcessAware;
import com.akshita.jad.grpcweb.grpc.DemoBootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 
 * TODO line  listener？ stringkey， classname|method|desc|num ？
 * ，， SpyAPI invoke?
 * 
 * TODO trace？ trace  classname|method|desc|trace ?  trace 
 * SPYinvoke ？？
 * 
 * TODO trace ？ Listener。
 * 
 * TODO SPY Object，？  Listener。 。 
 *  Listener？ ， Binding，， ？
 * ClassLoader ， ClassLoader？
 * 
 * ，，， ID ， trace ？？
 * 
 *  watch ，，watch TransForm，，。
 * watch，。 reset 。
 * 
 * ，！  ！ ， watchinmethod
 * ，  ，
 * 
 * TODO trace ，，。 ？  linenumber binding？
 * mehtodNode，？
 * 
 * TODO ， annotation，， invoke ！ 
 * transform！  annotation ？ annotation url ?key/value！
 * 
 * 
 * TODO  trace /watch， Listener ID， ，
 * trace/watchID，，。 ，，trace ！
 * 
 * 
 * @author hengyunabc 2020-04-24
 *
 */
public class AdviceListenerManager {
    private static final Logger logger = LoggerFactory.getLogger(AdviceListenerManager.class);
    private static final FakeBootstrapClassLoader FAKEBOOTSTRAPCLASSLOADER = new FakeBootstrapClassLoader();

    static {
        //  AdviceListener
        DemoBootstrap.getRunningInstance().getScheduledExecutorService().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Entry<ClassLoader, ClassLoaderAdviceListenerManager> entry : adviceListenerMap.entrySet()) {
                        ClassLoaderAdviceListenerManager adviceListenerManager = entry.getValue();
                        synchronized (adviceListenerManager) {
                            for (Entry<String, List<AdviceListener>> eee : adviceListenerManager.map.entrySet()) {
                                List<AdviceListener> listeners = eee.getValue();
                                List<AdviceListener> newResult = new ArrayList<AdviceListener>();
                                for (AdviceListener listener : listeners) {
                                    if (listener instanceof ProcessAware) {
                                        ProcessAware processAware = (ProcessAware) listener;
                                        Process process = processAware.getProcess();
                                        if (process == null) {
                                            continue;
                                        }
                                        ExecStatus status = process.status();
                                        if (!status.equals(ExecStatus.TERMINATED)) {
                                            newResult.add(listener);
                                        }
                                    }
                                }

                                if (newResult.size() != listeners.size()) {
                                    adviceListenerManager.map.put(eee.getKey(), newResult);
                                }

                            }
                        }
                    }
                } catch (Throwable e) {
                    try {
                        logger.error("clean AdviceListener error", e);
                    } catch (Throwable t) {
                        // ignore
                    }
                }
            }
        }, 3, 3, TimeUnit.SECONDS);
    }

    private static final ConcurrentWeakKeyHashMap<ClassLoader, ClassLoaderAdviceListenerManager> adviceListenerMap = new ConcurrentWeakKeyHashMap<ClassLoader, ClassLoaderAdviceListenerManager>();

    static class ClassLoaderAdviceListenerManager {
        private ConcurrentHashMap<String, List<AdviceListener>> map = new ConcurrentHashMap<String, List<AdviceListener>>();

        private String key(String className, String methodName, String methodDesc) {
            return className + methodName + methodDesc;
        }

        private String keyForTrace(String className, String owner, String methodName, String methodDesc) {
            return className + owner + methodName + methodDesc;
        }

        public void registerAdviceListener(String className, String methodName, String methodDesc,
                AdviceListener listener) {
            synchronized (this) {
                className = className.replace('/', '.');
                String key = key(className, methodName, methodDesc);

                List<AdviceListener> listeners = map.get(key);
                if (listeners == null) {
                    listeners = new ArrayList<AdviceListener>();
                    map.put(key, listeners);
                }
                if (!listeners.contains(listener)) {
                    listeners.add(listener);
                }
            }
        }

        public List<AdviceListener> queryAdviceListeners(String className, String methodName, String methodDesc) {
            className = className.replace('/', '.');
            String key = key(className, methodName, methodDesc);

            List<AdviceListener> listeners = map.get(key);

            return listeners;
        }

        public void registerTraceAdviceListener(String className, String owner, String methodName, String methodDesc,
                AdviceListener listener) {

            className = className.replace('/', '.');
            String key = keyForTrace(className, owner, methodName, methodDesc);

            List<AdviceListener> listeners = map.get(key);
            if (listeners == null) {
                listeners = new ArrayList<AdviceListener>();
                map.put(key, listeners);
            }
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        public List<AdviceListener> queryTraceAdviceListeners(String className, String owner, String methodName,
                String methodDesc) {
            className = className.replace('/', '.');
            String key = keyForTrace(className, owner, methodName, methodDesc);

            List<AdviceListener> listeners = map.get(key);

            return listeners;
        }
    }

    public static void registerAdviceListener(ClassLoader classLoader, String className, String methodName,
            String methodDesc, AdviceListener listener) {
        classLoader = wrap(classLoader);
        className = className.replace('/', '.');

        ClassLoaderAdviceListenerManager manager = adviceListenerMap.get(classLoader);

        if (manager == null) {
            manager = new ClassLoaderAdviceListenerManager();
            adviceListenerMap.put(classLoader, manager);
        }
        manager.registerAdviceListener(className, methodName, methodDesc, listener);
    }

    public static void updateAdviceListeners() {

    }

    public static List<AdviceListener> queryAdviceListeners(ClassLoader classLoader, String className,
            String methodName, String methodDesc) {
        classLoader = wrap(classLoader);
        className = className.replace('/', '.');
        ClassLoaderAdviceListenerManager manager = adviceListenerMap.get(classLoader);

        if (manager != null) {
            return manager.queryAdviceListeners(className, methodName, methodDesc);
        }

        return null;
    }

    public static void registerTraceAdviceListener(ClassLoader classLoader, String className, String owner,
            String methodName, String methodDesc, AdviceListener listener) {
        classLoader = wrap(classLoader);
        className = className.replace('/', '.');

        ClassLoaderAdviceListenerManager manager = adviceListenerMap.get(classLoader);

        if (manager == null) {
            manager = new ClassLoaderAdviceListenerManager();
            adviceListenerMap.put(classLoader, manager);
        }
        manager.registerTraceAdviceListener(className, owner, methodName, methodDesc, listener);
    }

    public static List<AdviceListener> queryTraceAdviceListeners(ClassLoader classLoader, String className,
            String owner, String methodName, String methodDesc) {
        classLoader = wrap(classLoader);
        className = className.replace('/', '.');
        ClassLoaderAdviceListenerManager manager = adviceListenerMap.get(classLoader);

        if (manager != null) {
            return manager.queryTraceAdviceListeners(className, owner, methodName, methodDesc);
        }

        return null;
    }

    private static ClassLoader wrap(ClassLoader classLoader) {
        if (classLoader != null) {
            return classLoader;
        }
        return FAKEBOOTSTRAPCLASSLOADER;
    }

    private static class FakeBootstrapClassLoader extends ClassLoader {

    }
}
