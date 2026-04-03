package com.akshita.jad.agent334;

import java.jad.SpyAPI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;

import com.akshita.jad.agent.JADClassloader;

/**
 * 
 *
 * @author vlinux on 15/5/19.
 */
public class AgentBootstrap {
    private static final String JAD_CORE_JAR = "jad-core.jar";
    private static final String JAD_BOOTSTRAP = "com.akshita.jad.core.server.JADBootstrap";
    private static final String GET_INSTANCE = "getInstance";
    private static final String IS_BIND = "isBind";

    private static PrintStream ps = System.err;
    static {
        try {
            File jadLogDir = new File(System.getProperty("user.home") + File.separator + "logs" + File.separator
                    + "jad" + File.separator);
            if (!jadLogDir.exists()) {
                jadLogDir.mkdirs();
            }
            if (!jadLogDir.exists()) {
                // #572
                jadLogDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "logs" + File.separator
                        + "jad" + File.separator);
                if (!jadLogDir.exists()) {
                    jadLogDir.mkdirs();
                }
            }

            File log = new File(jadLogDir, "jad.log");

            if (!log.exists()) {
                log.createNewFile();
            }
            ps = new PrintStream(new FileOutputStream(log, true));
        } catch (Throwable t) {
            t.printStackTrace(ps);
        }
    }

    /**
     * <pre>
     * 1. classloader JAD ，attach
     * 2. ClassLoaderjadreset
     * 3. ClassLoader， com.akshita.jad.core.server.JADBootstrap#getInstance 
     * </pre>
     */
    private static volatile ClassLoader jadClassLoader;

    public static void premain(String args, Instrumentation inst) {
        main(args, inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        main(args, inst);
    }

    /**
     * 
     */
    public static void resetJADClassLoader() {
        jadClassLoader = null;
    }

    private static ClassLoader getClassLoader(Instrumentation inst, File jadCoreJarFile) throws Throwable {
        // ，JAD
        return loadOrDefineClassLoader(jadCoreJarFile);
    }

    private static ClassLoader loadOrDefineClassLoader(File jadCoreJarFile) throws Throwable {
        if (jadClassLoader == null) {
            jadClassLoader = new JADClassloader(new URL[]{jadCoreJarFile.toURI().toURL()});
        }
        return jadClassLoader;
    }

    private static synchronized void main(String args, final Instrumentation inst) {
        // jad，，
        try {
            Class.forName("java.jad.SpyAPI"); // 
            if (SpyAPI.isInited()) {
                ps.println("JAD server already stared, skip attach.");
                ps.flush();
                return;
            }
        } catch (Throwable e) {
            // ignore
        }
        try {
            ps.println("JAD server agent start...");
            // args:jadCoreJaragentArgs, AgentJAR
            if (args == null) {
                args = "";
            }
            args = decodeArg(args);

            String jadCoreJar;
            final String agentArgs;
            int index = args.indexOf(';');
            if (index != -1) {
                jadCoreJar = args.substring(0, index);
                agentArgs = args.substring(index);
            } else {
                jadCoreJar = "";
                agentArgs = args;
            }

            File jadCoreJarFile = new File(jadCoreJar);
            if (!jadCoreJarFile.exists()) {
                ps.println("Can not find jad-core jar file from args: " + jadCoreJarFile);
                // try to find from jad-agent.jar directory
                CodeSource codeSource = AgentBootstrap.class.getProtectionDomain().getCodeSource();
                if (codeSource != null) {
                    try {
                        File jadAgentJarFile = new File(codeSource.getLocation().toURI().getSchemeSpecificPart());
                        jadCoreJarFile = new File(jadAgentJarFile.getParentFile(), JAD_CORE_JAR);
                        if (!jadCoreJarFile.exists()) {
                            ps.println("Can not find jad-core jar file from agent jar directory: " + jadAgentJarFile);
                        }
                    } catch (Throwable e) {
                        ps.println("Can not find jad-core jar file from " + codeSource.getLocation());
                        e.printStackTrace(ps);
                    }
                }
            }
            if (!jadCoreJarFile.exists()) {
                return;
            }

            /**
             * Use a dedicated thread to run the binding logic to prevent possible memory leak. #195
             */
            final ClassLoader agentLoader = getClassLoader(inst, jadCoreJarFile);

            Thread bindingThread = new Thread() {
                @Override
                public void run() {
                    try {
                        bind(inst, agentLoader, agentArgs);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace(ps);
                    }
                }
            };

            bindingThread.setName("jad-binding-thread");
            bindingThread.start();
            bindingThread.join();
        } catch (Throwable t) {
            t.printStackTrace(ps);
            try {
                if (ps != System.err) {
                    ps.close();
                }
            } catch (Throwable tt) {
                // ignore
            }
            throw new RuntimeException(t);
        }
    }

    private static void bind(Instrumentation inst, ClassLoader agentLoader, String args) throws Throwable {
        /**
         * <pre>
         * JADBootstrap bootstrap = JADBootstrap.getInstance(inst);
         * </pre>
         */
        Class<?> bootstrapClass = agentLoader.loadClass(JAD_BOOTSTRAP);
        Object bootstrap = bootstrapClass.getMethod(GET_INSTANCE, Instrumentation.class, String.class).invoke(null, inst, args);
        boolean isBind = (Boolean) bootstrapClass.getMethod(IS_BIND).invoke(bootstrap);
        if (!isBind) {
            String errorMsg = "JAD server port binding failed! Please check $HOME/logs/jad/jad.log for more details.";
            ps.println(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        ps.println("JAD server already bind.");
    }

    private static String decodeArg(String arg) {
        try {
            return URLDecoder.decode(arg, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return arg;
        }
    }
}
