package com.akshita.jad.core.util;

import java.io.File;
import java.util.Iterator;

import com.akshita.jad.deps.ch.qos.logback.classic.LoggerContext;
import com.akshita.jad.deps.ch.qos.logback.classic.joran.JoranConfigurator;
import com.akshita.jad.deps.ch.qos.logback.classic.spi.ILoggingEvent;
import com.akshita.jad.deps.ch.qos.logback.core.Appender;
import com.akshita.jad.deps.ch.qos.logback.core.rolling.RollingFileAppender;
import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.common.AnsiLog;
import com.akshita.jad.core.env.JADEnvironment;

/**
 * 
 * @author hengyunabc
 *
 */
public class LogUtil {

    public static final String LOGGING_CONFIG_PROPERTY = "jad.logging.config";
    public static final String LOGGING_CONFIG = "${jad.logging.config:${jad.home}/logback.xml}";

    /**
     * The name of the property that contains the name of the log file. Names can be
     * an exact location or relative to the current directory.
     */
    public static final String FILE_NAME_PROPERTY = "jad.logging.file.name";
    public static final String JAD_LOG_FILE = "JAD_LOG_FILE";

    /**
     * The name of the property that contains the directory where log files are
     * written.
     */
    public static final String FILE_PATH_PROPERTY = "jad.logging.file.path";
    public static final String JAD_LOG_PATH = "JAD_LOG_PATH";

    private static String logFile = "";

    /**
     * <pre>
     * 1.  jad.logging.config  logback.xml
     * 2.  jad.home  logback.xml
     * 
     *  jad.logging.file.name jad.log
     *  jad.logging.file.path jad.log
     * 
     * </pre>
     * 
     * @param env
     */
    public static LoggerContext initLogger(JADEnvironment env) {
        String loggingConfig = env.resolvePlaceholders(LOGGING_CONFIG);
        if (loggingConfig == null || loggingConfig.trim().isEmpty()) {
            return null;
        }
        AnsiLog.debug("jad logging file: " + loggingConfig);
        File configFile = new File(loggingConfig);
        if (!configFile.isFile()) {
            AnsiLog.error("can not find jad logging config: " + loggingConfig);
            return null;
        }

        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.reset();

            String fileName = env.getProperty(FILE_NAME_PROPERTY);
            if (fileName != null) {
                loggerContext.putProperty(JAD_LOG_FILE, fileName);
            }
            String filePath = env.getProperty(FILE_PATH_PROPERTY);
            if (filePath != null) {
                loggerContext.putProperty(JAD_LOG_PATH, filePath);
            }

            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            configurator.doConfigure(configFile.toURI().toURL()); // load logback xml file

            //  jad.log appender
            Iterator<Appender<ILoggingEvent>> appenders = loggerContext.getLogger("root").iteratorForAppenders();

            while (appenders.hasNext()) {
                Appender<ILoggingEvent> appender = appenders.next();
                if (appender instanceof RollingFileAppender) {
                    RollingFileAppender fileAppender = (RollingFileAppender) appender;
                    if ("JAD".equalsIgnoreCase(fileAppender.getName())) {
                        logFile = new File(fileAppender.getFile()).getCanonicalPath();
                    }
                }
            }

            return loggerContext;
        } catch (Throwable e) {
            AnsiLog.error("try to load jad logging config file error: " + configFile, e);
        }
        return null;
    }

    public static String loggingFile() {
        if (logFile == null || logFile.trim().isEmpty()) {
            return "jad.log";
        }
        return logFile;
    }

    public static String loggingDir() {
        if (logFile != null && !logFile.isEmpty()) {
            String parent = new File(logFile).getParent();
            if (parent != null) {
                return parent;
            }
        }
        return new File("").getAbsolutePath();
    }

    public static String cacheDir() {
        File logsDir = new File(loggingDir()).getParentFile();
        if (logsDir.exists()) {
            File jadCacheDir = new File(logsDir, "jad-cache");
            jadCacheDir.mkdirs();
            return jadCacheDir.getAbsolutePath();
        } else {
            File jadCacheDir = new File("jad-cache");
            jadCacheDir.mkdirs();
            return jadCacheDir.getAbsolutePath();
        }
    }

    public static Logger getResultLogger() {
        return LoggerFactory.getLogger("result");
    }
}
