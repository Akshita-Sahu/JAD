package com.akshita.jad.agent.attach;

import java.jad.SpyAPI;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.zeroturnaround.zip.ZipUtil;

import net.bytebuddy.agent.ByteBuddyAgent;

/**
 * 
 * @author hengyunabc 2020-06-22
 *
 */
public class JADAgent {
    private static final int TEMP_DIR_ATTEMPTS = 10000;

    private static final String JAD_CORE_JAR = "jad-core.jar";
    private static final String JAD_BOOTSTRAP = "com.akshita.jad.core.server.JADBootstrap";
    private static final String GET_INSTANCE = "getInstance";
    private static final String IS_BIND = "isBind";

    private String errorMessage;

    private Map<String, String> configMap = new HashMap<String, String>();
    private String jadHome;
    private boolean slientInit;
    private Instrumentation instrumentation;

    public JADAgent() {
        this(null, null, false, null);
    }

    public JADAgent(Map<String, String> configMap) {
        this(configMap, null, false, null);
    }

    public JADAgent(String jadHome) {
        this(null, jadHome, false, null);
    }

    public JADAgent(Map<String, String> configMap, String jadHome, boolean slientInit,
            Instrumentation instrumentation) {
        if (configMap != null) {
            this.configMap = configMap;
        }

        this.jadHome = jadHome;
        this.slientInit = slientInit;
        this.instrumentation = instrumentation;
    }

    public static void attach() {
        new JADAgent().init();
    }

    /**
     * @see https://github.com/Akshita-Sahu/JAD/doc/jad-properties.html
     * @param configMap
     */
    public static void attach(Map<String, String> configMap) {
        new JADAgent(configMap).init();
    }

    /**
     * use the specified jad
     * @param jadHome jad directory
     */
    public static void attach(String jadHome) {
        new JADAgent(jadHome).init();
    }

    public void init() throws IllegalStateException {
        // jad，，
        try {
            Class.forName("java.jad.SpyAPI"); // 
            if (SpyAPI.isInited()) {
                return;
            }
        } catch (Throwable e) {
            // ignore
        }

        try {
            if (instrumentation == null) {
                instrumentation = ByteBuddyAgent.install();
            }

            //  jadHome
            if (jadHome == null || jadHome.trim().isEmpty()) {
                //  jadHome
                URL coreJarUrl = this.getClass().getClassLoader().getResource("jad-bin.zip");
                if (coreJarUrl != null) {
                    File tempJADDir = createTempDir();
                    ZipUtil.unpack(coreJarUrl.openStream(), tempJADDir);
                    jadHome = tempJADDir.getAbsolutePath();
                } else {
                    throw new IllegalArgumentException("can not getResources jad-bin.zip from classloader: "
                            + this.getClass().getClassLoader());
                }
            }

            // find jad-core.jar
            File jadCoreJarFile = new File(jadHome, JAD_CORE_JAR);
            if (!jadCoreJarFile.exists()) {
                throw new IllegalStateException("can not find jad-core.jar under jadHome: " + jadHome);
            }
            AttachJADClassloader jadClassLoader = new AttachJADClassloader(
                    new URL[] { jadCoreJarFile.toURI().toURL() });

            /**
             * <pre>
             * JADBootstrap bootstrap = JADBootstrap.getInstance(inst);
             * </pre>
             */
            Class<?> bootstrapClass = jadClassLoader.loadClass(JAD_BOOTSTRAP);
            Object bootstrap = bootstrapClass.getMethod(GET_INSTANCE, Instrumentation.class, Map.class).invoke(null,
                    instrumentation, configMap);
            boolean isBind = (Boolean) bootstrapClass.getMethod(IS_BIND).invoke(bootstrap);
            if (!isBind) {
                String errorMsg = "JAD server port binding failed! Please check $HOME/logs/jad/jad.log for more details.";
                throw new RuntimeException(errorMsg);
            }
        } catch (Throwable e) {
            errorMessage = e.getMessage();
            if (!slientInit) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = "jad-" + System.currentTimeMillis() + "-";

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within " + TEMP_DIR_ATTEMPTS + " attempts (tried "
                + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
