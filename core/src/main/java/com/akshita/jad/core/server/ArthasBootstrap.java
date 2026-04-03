package com.akshita.jad.core.server;

import java.jad.SpyAPI;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarFile;

import com.akshita.jad.deps.ch.qos.logback.classic.LoggerContext;
import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.tunnel.client.TunnelClient;
import com.akshita_sahu.bytekit.asm.instrument.InstrumentConfig;
import com.akshita_sahu.bytekit.asm.instrument.InstrumentParseResult;
import com.akshita_sahu.bytekit.asm.instrument.InstrumentTransformer;
import com.akshita_sahu.bytekit.asm.matcher.SimpleClassMatcher;
import com.akshita_sahu.bytekit.utils.AsmUtils;
import com.akshita_sahu.bytekit.utils.IOUtils;
import com.akshita_sahu.fastjson2.JSON;
import com.akshita_sahu.fastjson2.JSONWriter;
import com.akshita.jad.common.AnsiLog;
import com.akshita.jad.common.JADConstants;
import com.akshita.jad.common.PidUtils;
import com.akshita.jad.common.SocketUtils;
import com.akshita.jad.core.advisor.Enhancer;
import com.akshita.jad.core.advisor.TransformerManager;
import com.akshita.jad.core.command.BuiltinCommandPack;
import com.akshita.jad.core.command.CommandExecutorImpl;
import com.akshita.jad.core.command.view.ResultViewResolver;
import com.akshita.jad.core.config.BinderUtils;
import com.akshita.jad.core.config.Configure;
import com.akshita.jad.core.config.FeatureCodec;
import com.akshita.jad.core.env.JADEnvironment;
import com.akshita.jad.core.env.MapPropertySource;
import com.akshita.jad.core.env.PropertiesPropertySource;
import com.akshita.jad.core.env.PropertySource;
import com.akshita.jad.core.security.SecurityAuthenticator;
import com.akshita.jad.core.security.SecurityAuthenticatorImpl;
import com.akshita.jad.core.server.instrument.ClassLoader_Instrument;
import com.akshita.jad.core.shell.ShellServer;
import com.akshita.jad.core.shell.ShellServerOptions;
import com.akshita.jad.core.shell.command.CommandResolver;
import com.akshita.jad.core.shell.handlers.BindHandler;
import com.akshita.jad.core.shell.history.HistoryManager;
import com.akshita.jad.core.shell.history.impl.HistoryManagerImpl;
import com.akshita.jad.core.shell.impl.ShellServerImpl;
import com.akshita.jad.core.shell.session.SessionManager;
import com.akshita.jad.core.shell.session.impl.SessionManagerImpl;
import com.akshita.jad.core.shell.term.impl.HttpTermServer;
import com.akshita.jad.core.shell.term.impl.http.api.HttpApiHandler;
import com.akshita.jad.core.shell.term.impl.http.session.HttpSessionManager;
import com.akshita.jad.core.shell.term.impl.httptelnet.HttpTelnetTermServer;
import com.akshita.jad.core.util.JADBanner;
import com.akshita.jad.core.util.FileUtils;
import com.akshita.jad.core.util.IPUtils;
import com.akshita.jad.core.util.InstrumentationUtils;
import com.akshita.jad.core.util.LogUtil;
import com.akshita.jad.core.util.StringUtils;
import com.akshita.jad.core.util.UserStatUtil;
import com.akshita.jad.core.util.affect.EnhancerAffect;
import com.akshita.jad.core.util.matcher.WildcardMatcher;

import com.akshita.jad.core.mcp.JADMcpBootstrap;
import com.akshita.jad.mcp.server.CommandExecutor;
import com.akshita.jad.mcp.server.protocol.server.handler.McpHttpRequestHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;


/**
 * @author vlinux on 15/5/2.
 * @author hengyunabc
 */
public class JADBootstrap {
    private static final String JAD_SPY_JAR = "jad-spy.jar";
    public static final String JAD_HOME_PROPERTY = "jad.home";
    private static String JAD_HOME = null;

    public static final String CONFIG_NAME_PROPERTY = "jad.config.name";
    public static final String CONFIG_LOCATION_PROPERTY = "jad.config.location";
    public static final String CONFIG_OVERRIDE_ALL = "jad.config.overrideAll";

    private static JADBootstrap jadBootstrap;

    private JADEnvironment jadEnvironment;
    private Configure configure;

    private AtomicBoolean isBindRef = new AtomicBoolean(false);
    private Instrumentation instrumentation;
    private InstrumentTransformer classLoaderInstrumentTransformer;
    private Thread shutdown;
    private ShellServer shellServer;
    private ScheduledExecutorService executorService;
    private SessionManager sessionManager;
    private TunnelClient tunnelClient;

    private File outputPath;

    private static LoggerContext loggerContext;
    private EventExecutorGroup workerGroup;

    private Timer timer = new Timer("jad-timer", true);

    private TransformerManager transformerManager;

    private ResultViewResolver resultViewResolver;

    private HistoryManager historyManager;

    private HttpApiHandler httpApiHandler;

    private McpHttpRequestHandler mcpRequestHandler;
    private JADMcpBootstrap jadMcpBootstrap;

    private HttpSessionManager httpSessionManager;
    private SecurityAuthenticator securityAuthenticator;

    private JADBootstrap(Instrumentation instrumentation, Map<String, String> args) throws Throwable {
        this.instrumentation = instrumentation;

        initFastjson();

        // 1. initSpy()
        initSpy();
        // 2. JADEnvironment
        initJADEnvironment(args);

        String outputPathStr = configure.getOutputPath();
        if (outputPathStr == null) {
            outputPathStr = JADConstants.JAD_OUTPUT;
        }
        outputPath = new File(outputPathStr);
        outputPath.mkdirs();

        // 3. init logger
        loggerContext = LogUtil.initLogger(jadEnvironment);

        // 4. ClassLoader
        enhanceClassLoader();
        // 5. init beans
        initBeans();

        // 6. start agent server
        bind(configure);

        executorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                final Thread t = new Thread(r, "jad-command-execute");
                t.setDaemon(true);
                return t;
            }
        });

        shutdown = new Thread("as-shutdown-hooker") {

            @Override
            public void run() {
                JADBootstrap.this.destroy();
            }
        };

        transformerManager = new TransformerManager(instrumentation);
        Runtime.getRuntime().addShutdownHook(shutdown);
    }

    private void initFastjson() {
        // ignore getter error #1661
        // #2081
        JSON.config(JSONWriter.Feature.IgnoreErrorGetter, JSONWriter.Feature.WriteNonStringKeyAsString);
    }

    private void initBeans() {
        this.resultViewResolver = new ResultViewResolver();
        this.historyManager = new HistoryManagerImpl();
    }

    private void initSpy() throws Throwable {
        // TODO init SpyImpl ?

        // SpyBootstrapClassLoader
        ClassLoader parent = ClassLoader.getSystemClassLoader().getParent();
        Class<?> spyClass = null;
        if (parent != null) {
            try {
                spyClass =parent.loadClass("java.jad.SpyAPI");
            } catch (Throwable e) {
                // ignore
            }
        }
        if (spyClass == null) {
            CodeSource codeSource = JADBootstrap.class.getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                File jadCoreJarFile = new File(codeSource.getLocation().toURI().getSchemeSpecificPart());
                File spyJarFile = new File(jadCoreJarFile.getParentFile(), JAD_SPY_JAR);
                instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(spyJarFile));
            } else {
                throw new IllegalStateException("can not find " + JAD_SPY_JAR);
            }
        }
    }

    void enhanceClassLoader() throws IOException, UnmodifiableClassException {
        if (configure.getEnhanceLoaders() == null) {
            return;
        }
        Set<String> loaders = new HashSet<String>();
        for (String s : configure.getEnhanceLoaders().split(",")) {
            loaders.add(s.trim());
        }

        //  ClassLoader#loadClsss ，ClassLoader SpyAPI
        // https://github.com/akshita_sahu/jad/issues/1596
        byte[] classBytes = IOUtils.getBytes(JADBootstrap.class.getClassLoader()
                .getResourceAsStream(ClassLoader_Instrument.class.getName().replace('.', '/') + ".class"));

        SimpleClassMatcher matcher = new SimpleClassMatcher(loaders);
        InstrumentConfig instrumentConfig = new InstrumentConfig(AsmUtils.toClassNode(classBytes), matcher);

        InstrumentParseResult instrumentParseResult = new InstrumentParseResult();
        instrumentParseResult.addInstrumentConfig(instrumentConfig);
        classLoaderInstrumentTransformer = new InstrumentTransformer(instrumentParseResult);
        instrumentation.addTransformer(classLoaderInstrumentTransformer, true);

        if (loaders.size() == 1 && loaders.contains(ClassLoader.class.getName())) {
            //  java.lang.ClassLoader，
            instrumentation.retransformClasses(ClassLoader.class);
        } else {
            InstrumentationUtils.trigerRetransformClasses(instrumentation, loaders);
        }
    }
    
    private void initJADEnvironment(Map<String, String> argsMap) throws IOException {
        if (jadEnvironment == null) {
            jadEnvironment = new JADEnvironment();
        }

        /**
         * <pre>
         * ， > System Env > System Properties > jad.properties
         * jad.properties ，。 jad.config.overrideAll=true
         * https://github.com/akshita_sahu/jad/issues/986
         * </pre>
         */
        Map<String, Object> copyMap;
        if (argsMap != null) {
            copyMap = new HashMap<String, Object>(argsMap);
            //  jad.home
            if (!copyMap.containsKey(JAD_HOME_PROPERTY)) {
                copyMap.put(JAD_HOME_PROPERTY, jadHome());
            }
        } else {
            copyMap = new HashMap<String, Object>(1);
            copyMap.put(JAD_HOME_PROPERTY, jadHome());
        }

        MapPropertySource mapPropertySource = new MapPropertySource("args", copyMap);
        jadEnvironment.addFirst(mapPropertySource);

        tryToLoadJADProperties();

        configure = new Configure();
        BinderUtils.inject(jadEnvironment, configure);
    }

    private static String jadHome() {
        if (JAD_HOME != null) {
            return JAD_HOME;
        }
        CodeSource codeSource = JADBootstrap.class.getProtectionDomain().getCodeSource();
        if (codeSource != null) {
            try {
                JAD_HOME = new File(codeSource.getLocation().toURI().getSchemeSpecificPart()).getParentFile().getAbsolutePath();
            } catch (Throwable e) {
                AnsiLog.error("try to find jad.home from CodeSource error", e);
            }
        }
        if (JAD_HOME == null) {
            JAD_HOME = new File("").getAbsolutePath();
        }
        return JAD_HOME;
    }

    static String reslove(JADEnvironment jadEnvironment, String key, String defaultValue) {
        String value = jadEnvironment.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return jadEnvironment.resolvePlaceholders(value);
    }

    // try to load jad.properties
    private void tryToLoadJADProperties() throws IOException {
        this.jadEnvironment.resolvePlaceholders(CONFIG_LOCATION_PROPERTY);

        String location = reslove(jadEnvironment, CONFIG_LOCATION_PROPERTY, null);

        if (location == null) {
            location = jadHome();
        }

        String configName = reslove(jadEnvironment, CONFIG_NAME_PROPERTY, "jad");

        if (location != null) {
            if (!location.endsWith(".properties")) {
                location = new File(location, configName + ".properties").getAbsolutePath();
            }
            if (new File(location).exists()) {
                Properties properties = FileUtils.readProperties(location);

                boolean overrideAll = false;
                if (jadEnvironment.containsProperty(CONFIG_OVERRIDE_ALL)) {
                    overrideAll = jadEnvironment.getRequiredProperty(CONFIG_OVERRIDE_ALL, boolean.class);
                } else {
                    overrideAll = Boolean.parseBoolean(properties.getProperty(CONFIG_OVERRIDE_ALL, "false"));
                }

                PropertySource<?> propertySource = new PropertiesPropertySource(location, properties);
                if (overrideAll) {
                    jadEnvironment.addFirst(propertySource);
                } else {
                    jadEnvironment.addLast(propertySource);
                }
            }
        }

    }

    /**
     * Bootstrap jad server
     *
     * @param configure 
     * @throws IOException 
     */
    private void bind(Configure configure) throws Throwable {

        long start = System.currentTimeMillis();

        if (!isBindRef.compareAndSet(false, true)) {
            throw new IllegalStateException("already bind");
        }

        // init random port
        if (configure.getTelnetPort() != null && configure.getTelnetPort() == 0) {
            int newTelnetPort = SocketUtils.findAvailableTcpPort();
            configure.setTelnetPort(newTelnetPort);
            logger().info("generate random telnet port: " + newTelnetPort);
        }
        if (configure.getHttpPort() != null && configure.getHttpPort() == 0) {
            int newHttpPort = SocketUtils.findAvailableTcpPort();
            configure.setHttpPort(newHttpPort);
            logger().info("generate random http port: " + newHttpPort);
        }
        // try to find appName
        if (configure.getAppName() == null) {
            configure.setAppName(System.getProperty(JADConstants.PROJECT_NAME,
                    System.getProperty(JADConstants.SPRING_APPLICATION_NAME, null)));
        }

        try {
            if (configure.getTunnelServer() != null) {
                tunnelClient = new TunnelClient();
                tunnelClient.setAppName(configure.getAppName());
                tunnelClient.setId(configure.getAgentId());
                tunnelClient.setTunnelServerUrl(configure.getTunnelServer());
                tunnelClient.setVersion(JADBanner.version());
                ChannelFuture channelFuture = tunnelClient.start();
                channelFuture.await(10, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            logger().error("start tunnel client error", t);
        }

        try {
            ShellServerOptions options = new ShellServerOptions()
                            .setInstrumentation(instrumentation)
                            .setPid(PidUtils.currentLongPid())
                            .setWelcomeMessage(JADBanner.welcome());
            if (configure.getSessionTimeout() != null) {
                options.setSessionTimeout(configure.getSessionTimeout() * 1000);
            }

            this.httpSessionManager = new HttpSessionManager();
            if (IPUtils.isAllZeroIP(configure.getIp()) && StringUtils.isBlank(configure.getPassword())) {
                //  listen 0.0.0.0 ，，
                String errorMsg = "Listening on 0.0.0.0 is very dangerous! External users can connect to your machine! "
                        + "No password is currently configured. " + "Therefore, a default password is generated, "
                        + "and clients need to use the password to connect!";
                AnsiLog.error(errorMsg);
                configure.setPassword(StringUtils.randomString(64));
                AnsiLog.error("Generated jad password: " + configure.getPassword());

                logger().error(errorMsg);
                logger().info("Generated jad password: " + configure.getPassword());
            }

            this.securityAuthenticator = new SecurityAuthenticatorImpl(configure.getUsername(), configure.getPassword());

            shellServer = new ShellServerImpl(options);

            List<String> disabledCommands = new ArrayList<String>();
            if (configure.getDisabledCommands() != null) {
                String[] strings = StringUtils.tokenizeToStringArray(configure.getDisabledCommands(), ",");
                if (strings != null) {
                    disabledCommands.addAll(Arrays.asList(strings));
                }
            }
            BuiltinCommandPack builtinCommands = new BuiltinCommandPack(disabledCommands);
            List<CommandResolver> resolvers = new ArrayList<CommandResolver>();
            resolvers.add(builtinCommands);

            //worker group
            workerGroup = new NioEventLoopGroup(new DefaultThreadFactory("jad-TermServer", true));

            // TODO: discover user provided command resolver
            if (configure.getTelnetPort() != null && configure.getTelnetPort() > 0) {
                logger().info("try to bind telnet server, host: {}, port: {}.", configure.getIp(), configure.getTelnetPort());
                shellServer.registerTermServer(new HttpTelnetTermServer(configure.getIp(), configure.getTelnetPort(),
                        options.getConnectionTimeout(), workerGroup, httpSessionManager));
            } else {
                logger().info("telnet port is {}, skip bind telnet server.", configure.getTelnetPort());
            }
            if (configure.getHttpPort() != null && configure.getHttpPort() > 0) {
                logger().info("try to bind http server, host: {}, port: {}.", configure.getIp(), configure.getHttpPort());
                shellServer.registerTermServer(new HttpTermServer(configure.getIp(), configure.getHttpPort(),
                        options.getConnectionTimeout(), workerGroup, httpSessionManager));
            } else {
                // listen local address in VM communication
                if (configure.getTunnelServer() != null) {
                    shellServer.registerTermServer(new HttpTermServer(configure.getIp(), configure.getHttpPort(),
                            options.getConnectionTimeout(), workerGroup, httpSessionManager));
                }
                logger().info("http port is {}, skip bind http server.", configure.getHttpPort());
            }

            for (CommandResolver resolver : resolvers) {
                shellServer.registerCommandResolver(resolver);
            }

            shellServer.listen(new BindHandler(isBindRef));
            if (!isBind()) {
                throw new IllegalStateException("JAD failed to bind telnet or http port! Telnet port: "
                        + String.valueOf(configure.getTelnetPort()) + ", http port: "
                        + String.valueOf(configure.getHttpPort()));
            }

            //http api session manager
            sessionManager = new SessionManagerImpl(options, shellServer.getCommandManager(), shellServer.getJobController());
            //http api handler
            httpApiHandler = new HttpApiHandler(historyManager, sessionManager);

            // Mcp Server
            String mcpEndpoint = configure.getMcpEndpoint();
            String mcpProtocol = configure.getMcpProtocol();
            if (mcpEndpoint != null && !mcpEndpoint.trim().isEmpty()) {
                logger().info("try to start mcp server, endpoint: {}, protocol: {}.", mcpEndpoint, mcpProtocol);
                CommandExecutor commandExecutor = new CommandExecutorImpl(sessionManager);
                this.jadMcpBootstrap = new JADMcpBootstrap(commandExecutor, mcpEndpoint, mcpProtocol);
                this.mcpRequestHandler = this.jadMcpBootstrap.start().getMcpRequestHandler();
            }
            logger().info("as-server listening on network={};telnet={};http={};timeout={};mcp={};mcpProtocol={};", configure.getIp(),
                    configure.getTelnetPort(), configure.getHttpPort(), options.getConnectionTimeout(), configure.getMcpEndpoint(), configure.getMcpProtocol());

            // 
            if (configure.getStatUrl() != null) {
                logger().info("jad stat url: {}", configure.getStatUrl());
            }
            UserStatUtil.setStatUrl(configure.getStatUrl());
            UserStatUtil.setAgentId(configure.getAgentId());
            UserStatUtil.jadStart();

            try {
                SpyAPI.init();
            } catch (Throwable e) {
                // ignore
            }

            logger().info("as-server started in {} ms", System.currentTimeMillis() - start);
        } catch (Throwable e) {
            logger().error("Error during start as-server", e);
            destroy();
            throw e;
        }
    }

    private void shutdownWorkGroup() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully(200, 200, TimeUnit.MILLISECONDS);
            workerGroup = null;
        }
    }

    /**
     * 
     *
     * @return true:;false:
     */
    public boolean isBind() {
        return isBindRef.get();
    }

    public EnhancerAffect reset() throws UnmodifiableClassException {
        return Enhancer.reset(this.instrumentation, new WildcardMatcher("*"));
    }

    /**
     * call reset() before destroy()
     */
    public void destroy() {
        if (this.jadMcpBootstrap != null) {
            try {
                // stop  mcp keep-alive ， stop  JADClassLoader 
                this.jadMcpBootstrap.shutdown();
            } catch (Throwable e) {
                logger().error("stop mcp server error", e);
            } finally {
                this.jadMcpBootstrap = null;
                this.mcpRequestHandler = null;
            }
        }
        if (shellServer != null) {
            shellServer.close();
            shellServer = null;
        }
        if (sessionManager != null) {
            sessionManager.close();
            sessionManager = null;
        }
        if (this.httpSessionManager != null) {
            httpSessionManager.stop();
        }
        if (timer != null) {
            timer.cancel();
        }
        if (this.tunnelClient != null) {
            try {
                tunnelClient.stop();
            } catch (Throwable e) {
                logger().error("stop tunnel client error", e);
            }
        }
        if (executorService != null) {
            executorService.shutdownNow();
        }
        if (transformerManager != null) {
            transformerManager.destroy();
        }
        if (classLoaderInstrumentTransformer != null) {
            instrumentation.removeTransformer(classLoaderInstrumentTransformer);
        }
        // clear the reference in Spy class.
        cleanUpSpyReference();
        shutdownWorkGroup();
        UserStatUtil.destroy();
        if (shutdown != null) {
            try {
                Runtime.getRuntime().removeShutdownHook(shutdown);
            } catch (Throwable t) {
                // ignore
            }
        }
        logger().info("as-server destroy completed.");
        if (loggerContext != null) {
            loggerContext.stop();
        }
    }

    /**
     * 
     *
     * @param instrumentation JVM
     * @return JADServer
     * @throws Throwable
     */
    public synchronized static JADBootstrap getInstance(Instrumentation instrumentation, String args) throws Throwable {
        if (jadBootstrap != null) {
            return jadBootstrap;
        }

        Map<String, String> argsMap = FeatureCodec.DEFAULT_COMMANDLINE_CODEC.toMap(args);
        // 
        Map<String, String> mapWithPrefix = new HashMap<String, String>(argsMap.size());
        for (Entry<String, String> entry : argsMap.entrySet()) {
            mapWithPrefix.put("jad." + entry.getKey(), entry.getValue());
        }
        return getInstance(instrumentation, mapWithPrefix);
    }

    /**
     * 
     *
     * @param instrumentation JVM
     * @return JADServer
     * @throws Throwable
     */
    public synchronized static JADBootstrap getInstance(Instrumentation instrumentation, Map<String, String> args) throws Throwable {
        if (jadBootstrap == null) {
            jadBootstrap = new JADBootstrap(instrumentation, args);
        }
        return jadBootstrap;
    }

    /**
     * @return JADServer
     */
    public static JADBootstrap getInstance() {
        if (jadBootstrap == null) {
            throw new IllegalStateException("JADBootstrap must be initialized before!");
        }
        return jadBootstrap;
    }

    public void execute(Runnable command) {
        executorService.execute(command);
    }

    /**
     * SpyAPI
     */
    private void cleanUpSpyReference() {
        try {
            SpyAPI.setNopSpy();
            SpyAPI.destroy();
        } catch (Throwable e) {
            // ignore
        }
        // AgentBootstrap.resetJADClassLoader();
        try {
            Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("com.akshita.jad.agent334.AgentBootstrap");
            Method method = clazz.getDeclaredMethod("resetJADClassLoader");
            method.invoke(null);
        } catch (Throwable e) {
            // ignore
        }
    }

    public TunnelClient getTunnelClient() {
        return tunnelClient;
    }

    public ShellServer getShellServer() {
        return shellServer;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public Timer getTimer() {
        return this.timer;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return this.executorService;
    }

    public Instrumentation getInstrumentation() {
        return this.instrumentation;
    }

    public TransformerManager getTransformerManager() {
        return this.transformerManager;
    }

    private Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    public ResultViewResolver getResultViewResolver() {
        return resultViewResolver;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public HttpApiHandler getHttpApiHandler() {
        return httpApiHandler;
    }

    public McpHttpRequestHandler getMcpRequestHandler() {
        return mcpRequestHandler;
    }

    public File getOutputPath() {
        return outputPath;
    }

    public SecurityAuthenticator getSecurityAuthenticator() {
        return securityAuthenticator;
    }

    public Configure getConfigure() {
        return configure;
    }
}
