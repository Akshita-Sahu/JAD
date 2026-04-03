package com.akshita.jad.core.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Properties;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.akshita.jad.deps.ch.qos.logback.classic.Level;
import com.akshita.jad.deps.ch.qos.logback.classic.Logger;
import com.akshita.jad.deps.ch.qos.logback.classic.LoggerContext;
import com.akshita.jad.deps.ch.qos.logback.classic.spi.ILoggingEvent;
import com.akshita.jad.deps.ch.qos.logback.core.Appender;
import com.akshita.jad.deps.ch.qos.logback.core.rolling.RollingFileAppender;
import com.akshita.jad.core.env.JADEnvironment;
import com.akshita.jad.core.env.PropertiesPropertySource;

/**
 * 
 * @author hengyunabc
 *
 */
public class LogUtilTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    JADEnvironment jadEnvironment;
    String testResourcesDir;

    @Before
    public void before() throws URISyntaxException {
        ClassLoader classLoader = LogUtilTest.class.getClassLoader();
        String logbakXmlPath = classLoader.getResource("logback-test.xml").toURI().getPath();

        testResourcesDir = new File(logbakXmlPath).getParent();

        jadEnvironment = new JADEnvironment();
    }

    @Test
    public void testJADHome() throws URISyntaxException {
        Properties properties1 = new Properties();
        properties1.put("jad.home", testResourcesDir);
        jadEnvironment.addLast(new PropertiesPropertySource("test1", properties1));

        LoggerContext loggerContext = LogUtil.initLogger(jadEnvironment);

        Logger logger = loggerContext.getLogger("root");
        Level level = logger.getLevel();

        Assertions.assertThat(level).isEqualTo(Level.ERROR);
    }

    @Test
    public void testLogConfig() throws URISyntaxException {
        Properties properties1 = new Properties();
        properties1.put("jad.home", testResourcesDir);
        properties1.put(LogUtil.LOGGING_CONFIG_PROPERTY, testResourcesDir + "/logback-test.xml");
        jadEnvironment.addLast(new PropertiesPropertySource("test1", properties1));

        LoggerContext loggerContext = LogUtil.initLogger(jadEnvironment);

        Logger logger = loggerContext.getLogger("root");
        Level level = logger.getLevel();

        Assertions.assertThat(level).isEqualTo(Level.WARN);
    }

    @Test
    public void test_DefaultLogFile() throws URISyntaxException, IOException {
        Properties properties1 = new Properties();
        properties1.put("jad.home", testResourcesDir);
        String logFile = new File(System.getProperty("user.home"), "logs/jad/jad.log").getCanonicalPath();

        jadEnvironment.addLast(new PropertiesPropertySource("test1", properties1));

        LoggerContext loggerContext = LogUtil.initLogger(jadEnvironment);

        Logger logger = loggerContext.getLogger("root");
        Level level = logger.getLevel();

        Assertions.assertThat(level).isEqualTo(Level.ERROR);

        Iterator<Appender<ILoggingEvent>> appenders = logger.iteratorForAppenders();

        boolean foundFileAppender = false;
        while (appenders.hasNext()) {
            Appender<ILoggingEvent> appender = appenders.next();
            if (appender instanceof RollingFileAppender) {
                RollingFileAppender fileAppender = (RollingFileAppender) appender;
                String file = fileAppender.getFile();
                Assertions.assertThat(new File(file).getCanonicalPath()).isEqualTo(logFile);
                foundFileAppender = true;
            }
        }
        Assertions.assertThat(foundFileAppender).isEqualTo(true);
    }

    @Test
    public void test_JAD_LOG_FILE() throws URISyntaxException, IOException {
        Properties properties1 = new Properties();
        properties1.put("jad.home", testResourcesDir);

        String logFile = new File(tempFolder.getRoot().getAbsoluteFile(), "test.log").getCanonicalPath();

        properties1.put(LogUtil.FILE_NAME_PROPERTY, logFile);
        jadEnvironment.addLast(new PropertiesPropertySource("test1", properties1));

        LoggerContext loggerContext = LogUtil.initLogger(jadEnvironment);

        Logger logger = loggerContext.getLogger("root");
        Level level = logger.getLevel();

        Assertions.assertThat(level).isEqualTo(Level.ERROR);

        Iterator<Appender<ILoggingEvent>> appenders = logger.iteratorForAppenders();

        boolean foundFileAppender = false;
        while (appenders.hasNext()) {
            Appender<ILoggingEvent> appender = appenders.next();
            if (appender instanceof RollingFileAppender) {
                RollingFileAppender fileAppender = (RollingFileAppender) appender;
                String file = fileAppender.getFile();
                Assertions.assertThat(new File(file).getCanonicalPath()).isEqualTo(logFile);
                foundFileAppender = true;
            }
        }
        Assertions.assertThat(foundFileAppender).isEqualTo(true);
    }

    @Test
    public void test_JAD_LOG_PATH() throws URISyntaxException, IOException {
        Properties properties1 = new Properties();
        properties1.put("jad.home", testResourcesDir);

        String logFile = new File(tempFolder.getRoot().getAbsoluteFile(), "jad.log").getCanonicalPath();

        properties1.put(LogUtil.FILE_PATH_PROPERTY, tempFolder.getRoot().getAbsolutePath());
        jadEnvironment.addLast(new PropertiesPropertySource("test1", properties1));

        LoggerContext loggerContext = LogUtil.initLogger(jadEnvironment);

        Logger logger = loggerContext.getLogger("root");
        Level level = logger.getLevel();

        Assertions.assertThat(level).isEqualTo(Level.ERROR);

        Iterator<Appender<ILoggingEvent>> appenders = logger.iteratorForAppenders();

        boolean foundFileAppender = false;
        while (appenders.hasNext()) {
            Appender<ILoggingEvent> appender = appenders.next();
            if (appender instanceof RollingFileAppender) {
                RollingFileAppender fileAppender = (RollingFileAppender) appender;
                String file = fileAppender.getFile();
                Assertions.assertThat(new File(file).getCanonicalPath()).isEqualTo(logFile);
                foundFileAppender = true;
            }
        }
        Assertions.assertThat(foundFileAppender).isEqualTo(true);
    }
}
