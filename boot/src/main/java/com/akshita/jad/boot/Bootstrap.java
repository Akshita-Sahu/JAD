package com.akshita.jad.boot;

import static com.akshita.jad.boot.ProcessUtils.STATUS_EXEC_ERROR;
import static com.akshita.jad.boot.ProcessUtils.STATUS_EXEC_TIMEOUT;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.akshita.jad.common.AnsiLog;
import com.akshita.jad.common.JavaVersionUtils;
import com.akshita.jad.common.SocketUtils;
import com.akshita.jad.common.UsageRender;
import com.akshita_sahu.middleware.cli.CLI;
import com.akshita_sahu.middleware.cli.CommandLine;
import com.akshita_sahu.middleware.cli.UsageMessageFormatter;
import com.akshita_sahu.middleware.cli.annotations.Argument;
import com.akshita_sahu.middleware.cli.annotations.CLIConfigurator;
import com.akshita_sahu.middleware.cli.annotations.Description;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Option;
import com.akshita_sahu.middleware.cli.annotations.Summary;

/**
 * @author hengyunabc 2018-10-26
 *
 */
@Name("jad-boot")
@Summary("Bootstrap JAD")
@Description("NOTE: JAD 4 supports JDK 8+. If you need to diagnose applications running on JDK 6/7, you can use JAD 3.\n\n"
                +"EXAMPLES:\n" + "  java -jar jad-boot.jar <pid>\n"
                + "  java -jar jad-boot.jar --telnet-port 9999 --http-port -1\n"
                + "  java -jar jad-boot.jar --username admin --password <password>\n"
                + "  java -jar jad-boot.jar --tunnel-server 'ws://192.168.10.11:7777/ws' --app-name demoapp\n"
                + "  java -jar jad-boot.jar --tunnel-server 'ws://192.168.10.11:7777/ws' --agent-id bvDOe8XbTM2pQWjF4cfw\n"
                + "  java -jar jad-boot.jar --stat-url 'http://192.168.10.11:8080/api/stat'\n"
                + "  java -jar jad-boot.jar -c 'sysprop; thread' <pid>\n"
                + "  java -jar jad-boot.jar -f batch.as <pid>\n"
                + "  java -jar jad-boot.jar --use-version 4.1.8\n"
                + "  java -jar jad-boot.jar --versions\n"
                + "  java -jar jad-boot.jar --select math-game\n"
                + "  java -jar jad-boot.jar --session-timeout 3600\n" + "  java -jar jad-boot.jar --attach-only\n"
                + "  java -jar jad-boot.jar --disabled-commands stop,dump\n"
                + "  java -jar jad-boot.jar --repo-mirror akshita_sahu --use-http\n" + "WIKI:\n"
                + "  https://github.com/Akshita-Sahu/JAD/doc\n")
public class Bootstrap {
    private static final int DEFAULT_TELNET_PORT = 3658;
    private static final int DEFAULT_HTTP_PORT = 8563;
    private static final String DEFAULT_TARGET_IP = "127.0.0.1";
    private static File JAD_LIB_DIR;

    private boolean help = false;

    private long pid = -1;
    private String targetIp;
    private Integer telnetPort;
    private Integer httpPort;
    /**
     * @see com.akshita.jad.core.config.Configure#DEFAULT_SESSION_TIMEOUT_SECONDS
     */
    private Long sessionTimeout;

    private Integer height = null;
    private Integer width = null;

    private boolean verbose = false;

    /**
     * <pre>
     * The directory contains jad-core.jar/jad-client.jar/jad-spy.jar.
     * 1. When use-version is not empty, try to find jad home under ~/.jad/lib
     * 2. Try set the directory where jad-boot.jar is located to jad home
     * 3. Try to download from remote repo
     * </pre>
     */
    private String jadHome;

    /**
     * under ~/.jad/lib
     */
    private String useVersion;

    /**
     * list local and remote versions
     */
    private boolean versions;

    /**
     * download from remo repository. if timezone is +0800, default value is 'akshita_sahu', else is 'center'.
     */
    private String repoMirror;

    /**
     * enforce use http to download jad. default use https
     */
    private boolean useHttp = false;

    private boolean attachOnly = false;

    private String command;
    private String batchFile;

    private String tunnelServer;
    private String agentId;

    private String appName;

    private String username;
    private String password;

    private String statUrl;

    private String select;

    private String disabledCommands;

	static {
        String jadLibDirEnv = System.getenv("JAD_LIB_DIR");
        if (jadLibDirEnv != null) {
            JAD_LIB_DIR = new File(jadLibDirEnv);
            AnsiLog.info("JAD_LIB_DIR: " + jadLibDirEnv);
        } else {
            JAD_LIB_DIR = new File(
                    System.getProperty("user.home") + File.separator + ".jad" + File.separator + "lib");
        }

        try {
            JAD_LIB_DIR.mkdirs();
        } catch (Throwable t) {
            //ignore
        }
        if (!JAD_LIB_DIR.exists()) {
            // try to set a temp directory
            JAD_LIB_DIR = new File(System.getProperty("java.io.tmpdir") + File.separator + ".jad" + File.separator + "lib");
            try {
                JAD_LIB_DIR.mkdirs();
            } catch (Throwable e) {
                // ignore
            }
        }
        if (!JAD_LIB_DIR.exists()) {
            System.err.println("Can not find directory to save jad lib. please try to set user home by -Duser.home=");
        }
    }

    @Argument(argName = "pid", index = 0, required = false)
    @Description("Target pid")
    public void setPid(long pid) {
        this.pid = pid;
    }

    @Option(shortName = "h", longName = "help", flag = true)
    @Description("Print usage")
    public void setHelp(boolean help) {
        this.help = help;
    }

    @Option(longName = "target-ip")
    @Description("The target jvm listen ip, default 127.0.0.1")
    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    @Option(longName = "telnet-port")
    @Description("The target jvm listen telnet port, default 3658")
    public void setTelnetPort(int telnetPort) {
        this.telnetPort = telnetPort;
    }

    @Option(longName = "http-port")
    @Description("The target jvm listen http port, default 8563")
    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    @Option(longName = "session-timeout")
    @Description("The session timeout seconds, default 1800 (30min)")
    public void setSessionTimeout(Long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @Option(longName = "jad-home")
    @Description("The jad home")
    public void setJADHome(String jadHome) {
        this.jadHome = jadHome;
    }

    @Option(longName = "use-version")
    @Description("Use special version jad")
    public void setUseVersion(String useVersion) {
        this.useVersion = useVersion;
    }

    @Option(longName = "repo-mirror")
    @Description("Use special remote repository mirror, value is center/akshita_sahu or http repo url.")
    public void setRepoMirror(String repoMirror) {
        this.repoMirror = repoMirror;
    }

    @Option(longName = "versions", flag = true)
    @Description("List local and remote jad versions")
    public void setVersions(boolean versions) {
        this.versions = versions;
    }

    @Option(longName = "use-http", flag = true)
    @Description("Enforce use http to download, default use https")
    public void setuseHttp(boolean useHttp) {
        this.useHttp = useHttp;
    }

    @Option(longName = "attach-only", flag = true)
    @Description("Attach target process only, do not connect")
    public void setAttachOnly(boolean attachOnly) {
        this.attachOnly = attachOnly;
    }

    @Option(shortName = "c", longName = "command")
    @Description("Command to execute, multiple commands separated by ;")
    public void setCommand(String command) {
        this.command = command;
    }

    @Option(shortName = "f", longName = "batch-file")
    @Description("The batch file to execute")
    public void setBatchFile(String batchFile) {
        this.batchFile = batchFile;
    }

    @Option(longName = "height")
    @Description("jad-client terminal height")
    public void setHeight(int height) {
        this.height = height;
    }

    @Option(longName = "width")
    @Description("jad-client terminal width")
    public void setWidth(int width) {
        this.width = width;
    }

    @Option(shortName = "v", longName = "verbose", flag = true)
    @Description("Verbose, print debug info.")
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    @Option(longName = "tunnel-server")
    @Description("The tunnel server url")
    public void setTunnelServer(String tunnelServer) {
        this.tunnelServer = tunnelServer;
    }

    @Option(longName = "agent-id")
    @Description("The agent id register to tunnel server")
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Option(longName = "app-name")
    @Description("The app name")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Option(longName = "username")
    @Description("The username")
    public void setUsername(String username) {
        this.username = username;
    }
    @Option(longName = "password")
    @Description("The password")
    public void setPassword(String password) {
        this.password = password;
    }

    @Option(longName = "stat-url")
    @Description("The report stat url")
    public void setStatUrl(String statUrl) {
        this.statUrl = statUrl;
    }

    @Option(longName = "select")
    @Description("select target process by classname or JARfilename")
    public void setSelect(String select) {
        this.select = select;
    }

    @Option(longName = "disabled-commands")
    @Description("disable some commands ")
    public void setDisabledCommands(String disabledCommands) {
        this.disabledCommands = disabledCommands;
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException,
                    ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
                    IllegalArgumentException, InvocationTargetException {
        String javaHome = System.getProperty("java.home");
        if (javaHome != null) {
            AnsiLog.info("JAVA_HOME: " + javaHome);
        }
        Package bootstrapPackage = Bootstrap.class.getPackage();
        if (bootstrapPackage != null) {
            String jadBootVersion = bootstrapPackage.getImplementationVersion();
            if (jadBootVersion != null) {
                AnsiLog.info("jad-boot version: " + jadBootVersion);
            }
        }

        try {
            String javaToolOptions = System.getenv("JAVA_TOOL_OPTIONS");
            if (javaToolOptions != null && !javaToolOptions.trim().isEmpty()) {
                AnsiLog.info("JAVA_TOOL_OPTIONS: " + javaToolOptions);
            }
        } catch (Throwable e) {
            // ignore
        }

        Bootstrap bootstrap = new Bootstrap();

        CLI cli = CLIConfigurator.define(Bootstrap.class);
        CommandLine commandLine = cli.parse(Arrays.asList(args));

        try {
            CLIConfigurator.inject(commandLine, bootstrap);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(usage(cli));
            System.exit(1);
        }

        if (bootstrap.isVerbose()) {
            AnsiLog.level(Level.ALL);
        }
        if (bootstrap.isHelp()) {
            System.out.println(usage(cli));
            System.exit(0);
        }

        if (bootstrap.getRepoMirror() == null || bootstrap.getRepoMirror().trim().isEmpty()) {
            bootstrap.setRepoMirror("center");
            // if timezone is +0800, default repo mirror is akshita_sahu
            if (TimeUnit.MILLISECONDS.toHours(TimeZone.getDefault().getOffset(System.currentTimeMillis())) == 8) {
                bootstrap.setRepoMirror("akshita_sahu");
            }
        }
        AnsiLog.debug("Repo mirror:" + bootstrap.getRepoMirror());

        if (bootstrap.isVersions()) {
            System.out.println(UsageRender.render(listVersions()));
            System.exit(0);
        }

        if (JavaVersionUtils.isJava6() || JavaVersionUtils.isJava7()) {
            bootstrap.setuseHttp(true);
            AnsiLog.debug("Java version is {}, only support http, set useHttp to true.",
                            JavaVersionUtils.javaVersionStr());
        }

        // check telnet/http port
        long telnetPortPid = -1;
        long httpPortPid = -1;
        if (bootstrap.getTelnetPortOrDefault() > 0) {
            telnetPortPid = SocketUtils.findTcpListenProcess(bootstrap.getTelnetPortOrDefault());
            if (telnetPortPid > 0) {
                AnsiLog.info("Process {} already using port {}", telnetPortPid, bootstrap.getTelnetPortOrDefault());
            }
        }
        if (bootstrap.getHttpPortOrDefault() > 0) {
            httpPortPid = SocketUtils.findTcpListenProcess(bootstrap.getHttpPortOrDefault());
            if (httpPortPid > 0) {
                AnsiLog.info("Process {} already using port {}", httpPortPid, bootstrap.getHttpPortOrDefault());
            }
        }

        long pid = bootstrap.getPid();
        // select pid
        if (pid < 0) {
            try {
                pid = ProcessUtils.select(bootstrap.isVerbose(), telnetPortPid, bootstrap.getSelect());
            } catch (InputMismatchException e) {
                System.out.println("Please input an integer to select pid.");
                System.exit(1);
            }
            if (pid < 0) {
                System.out.println("Please select an available pid.");
                System.exit(1);
            }
        }

        checkTelnetPortPid(bootstrap, telnetPortPid, pid);

        if (httpPortPid > 0 && pid != httpPortPid) {
            AnsiLog.error("Target process {} is not the process using port {}, you will connect to an unexpected process.",
                            pid, bootstrap.getHttpPortOrDefault());
            AnsiLog.error("1. Try to restart jad-boot, select process {}, shutdown it first with running the 'stop' command.",
                            httpPortPid);
            AnsiLog.error("2. Or try to use different http port, for example: java -jar jad-boot.jar --telnet-port 9998 --http-port 9999");
            System.exit(1);
        }

        // find jad home
        File jadHomeDir = null;
        if (bootstrap.getJADHome() != null) {
            verifyJADHome(bootstrap.getJADHome());
            jadHomeDir = new File(bootstrap.getJADHome());
        }
        if (jadHomeDir == null && bootstrap.getUseVersion() != null) {
            // try to find from ~/.jad/lib
            File specialVersionDir = new File(System.getProperty("user.home"), ".jad" + File.separator + "lib"
                            + File.separator + bootstrap.getUseVersion() + File.separator + "jad");
            if (!specialVersionDir.exists()) {
                // try to download jad from remote server.
                DownloadUtils.downJADPackaging(bootstrap.getRepoMirror(), bootstrap.isUseHttp(),
                                bootstrap.getUseVersion(), JAD_LIB_DIR.getAbsolutePath());
            }
            verifyJADHome(specialVersionDir.getAbsolutePath());
            jadHomeDir = specialVersionDir;
        }

        // Try set the directory where jad-boot.jar is located to jad home
        if (jadHomeDir == null) {
            CodeSource codeSource = Bootstrap.class.getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                try {
                    // https://stackoverflow.com/a/17870390
                    File bootJarPath = new File(codeSource.getLocation().toURI().getSchemeSpecificPart());
                    verifyJADHome(bootJarPath.getParent());
                    jadHomeDir = bootJarPath.getParentFile();
                } catch (Throwable e) {
                    // ignore
                }

            }
        }

        // try to download from remote server
        if (jadHomeDir == null) {
            boolean checkFile =  JAD_LIB_DIR.exists() || JAD_LIB_DIR.mkdirs();
            if(!checkFile){
                AnsiLog.error("cannot create directory {}: maybe permission denied", JAD_LIB_DIR.getAbsolutePath());
                System.exit(1);
            }

            /**
             * <pre>
             * 1. get local latest version
             * 2. get remote latest version
             * 3. compare two version
             * </pre>
             */
            List<String> versionList = listNames(JAD_LIB_DIR);
            Collections.sort(versionList);

            String localLatestVersion = null;
            if (!versionList.isEmpty()) {
                localLatestVersion = versionList.get(versionList.size() - 1);
            }

            String remoteLatestVersion = DownloadUtils.readLatestReleaseVersion();

            boolean needDownload = false;
            if (localLatestVersion == null) {
                if (remoteLatestVersion == null) {
                    // exit
                    AnsiLog.error("Can not find JAD under local: {} and remote repo mirror: {}", JAD_LIB_DIR,
                            bootstrap.getRepoMirror());
                    AnsiLog.error(
                            "Unable to download jad from remote server, please download the full package according to wiki: https://github.com/akshita_sahu/jad");
                    System.exit(1);
                } else {
                    needDownload = true;
                }
            } else {
                if (remoteLatestVersion != null) {
                    if (localLatestVersion.compareTo(remoteLatestVersion) < 0) {
                        AnsiLog.info("local latest version: {}, remote latest version: {}, try to download from remote.",
                                localLatestVersion, remoteLatestVersion);
                        needDownload = true;
                    }
                }
            }
            if (needDownload) {
                // try to download jad from remote server.
                DownloadUtils.downJADPackaging(bootstrap.getRepoMirror(), bootstrap.isUseHttp(),
                        remoteLatestVersion, JAD_LIB_DIR.getAbsolutePath());
                localLatestVersion = remoteLatestVersion;
            }

            // get the latest version
            jadHomeDir = new File(JAD_LIB_DIR, localLatestVersion + File.separator + "jad");
        }

        verifyJADHome(jadHomeDir.getAbsolutePath());

        AnsiLog.info("jad home: " + jadHomeDir);

        if (telnetPortPid > 0 && pid == telnetPortPid) {
            AnsiLog.info("The target process already listen port {}, skip attach.", bootstrap.getTelnetPortOrDefault());
        } else {
            //double check telnet port and pid before attach
            telnetPortPid = findProcessByTelnetClient(jadHomeDir.getAbsolutePath(), bootstrap.getTelnetPortOrDefault());
            checkTelnetPortPid(bootstrap, telnetPortPid, pid);
            if (telnetPortPid > 0 && pid == telnetPortPid) {
                AnsiLog.info("The target process already listen port {}, skip attach.", bootstrap.getTelnetPortOrDefault());
            } else {

                // start jad-core.jar
                List<String> attachArgs = new ArrayList<String>();
                attachArgs.add("-jar");
                attachArgs.add(new File(jadHomeDir, "jad-core.jar").getAbsolutePath());
                attachArgs.add("-pid");
                attachArgs.add("" + pid);
                if (bootstrap.getTargetIp() != null) {
                    attachArgs.add("-target-ip");
                    attachArgs.add(bootstrap.getTargetIp());
                }

                if (bootstrap.getTelnetPort() != null) {
                    attachArgs.add("-telnet-port");
                    attachArgs.add("" + bootstrap.getTelnetPort());
                }

                if (bootstrap.getHttpPort() != null) {
                    attachArgs.add("-http-port");
                    attachArgs.add("" + bootstrap.getHttpPort());
                }

                attachArgs.add("-core");
                attachArgs.add(new File(jadHomeDir, "jad-core.jar").getAbsolutePath());
                attachArgs.add("-agent");
                attachArgs.add(new File(jadHomeDir, "jad-agent.jar").getAbsolutePath());
                if (bootstrap.getSessionTimeout() != null) {
                    attachArgs.add("-session-timeout");
                    attachArgs.add("" + bootstrap.getSessionTimeout());
                }

                if (bootstrap.getAppName() != null) {
                    attachArgs.add("-app-name");
                    attachArgs.add(bootstrap.getAppName());
                }

                if (bootstrap.getUsername() != null) {
                    attachArgs.add("-username");
                    attachArgs.add(bootstrap.getUsername());
                }
                if (bootstrap.getPassword() != null) {
                    attachArgs.add("-password");
                    attachArgs.add(bootstrap.getPassword());
                }

                if (bootstrap.getTunnelServer() != null) {
                    attachArgs.add("-tunnel-server");
                    attachArgs.add(bootstrap.getTunnelServer());
                }
                if (bootstrap.getAgentId() != null) {
                    attachArgs.add("-agent-id");
                    attachArgs.add(bootstrap.getAgentId());
                }
                if (bootstrap.getStatUrl() != null) {
                    attachArgs.add("-stat-url");
                    attachArgs.add(bootstrap.getStatUrl());
                }

                if (bootstrap.getDisabledCommands() != null) {
                    attachArgs.add("-disabled-commands");
                    attachArgs.add(bootstrap.getDisabledCommands());
                }

                AnsiLog.info("Try to attach process " + pid);
                AnsiLog.debug("Start jad-core.jar args: " + attachArgs);
                ProcessUtils.startJADCore(pid, attachArgs);

                AnsiLog.info("Attach process {} success.", pid);
            }
        }

        if (bootstrap.isAttachOnly()) {
            System.exit(0);
        }

        // start java telnet client
        // find jad-client.jar
        URLClassLoader classLoader = new URLClassLoader(
                        new URL[] { new File(jadHomeDir, "jad-client.jar").toURI().toURL() });
        Class<?> telnetConsoleClas = classLoader.loadClass("com.akshita.jad.client.TelnetConsole");
        Method mainMethod = telnetConsoleClas.getMethod("main", String[].class);
        List<String> telnetArgs = new ArrayList<String>();

        if (bootstrap.getCommand() != null) {
            telnetArgs.add("-c");
            telnetArgs.add(bootstrap.getCommand());
        }
        if (bootstrap.getBatchFile() != null) {
            telnetArgs.add("-f");
            telnetArgs.add(bootstrap.getBatchFile());
        }
        if (bootstrap.getHeight() != null) {
            telnetArgs.add("--height");
            telnetArgs.add("" + bootstrap.getHeight());
        }
        if (bootstrap.getWidth() != null) {
            telnetArgs.add("--width");
            telnetArgs.add("" + bootstrap.getWidth());
        }

        // telnet port ,ip
        telnetArgs.add(bootstrap.getTargetIpOrDefault());
        telnetArgs.add("" + bootstrap.getTelnetPortOrDefault());

        AnsiLog.info("jad-client connect {} {}", bootstrap.getTargetIpOrDefault(), bootstrap.getTelnetPortOrDefault());
        AnsiLog.debug("Start jad-client.jar args: " + telnetArgs);

        // fix https://github.com/akshita_sahu/jad/issues/833
        Thread.currentThread().setContextClassLoader(classLoader);
        mainMethod.invoke(null, new Object[] { telnetArgs.toArray(new String[0]) });
    }

    private static void checkTelnetPortPid(Bootstrap bootstrap, long telnetPortPid, long targetPid) {
        if (telnetPortPid > 0 && targetPid != telnetPortPid) {
            AnsiLog.error("The telnet port {} is used by process {} instead of target process {}, you will connect to an unexpected process.",
                    bootstrap.getTelnetPortOrDefault(), telnetPortPid, targetPid);
            AnsiLog.error("1. Try to restart jad-boot, select process {}, shutdown it first with running the 'stop' command.",
                            telnetPortPid);
            AnsiLog.error("2. Or try to stop the existing jad instance: java -jar jad-client.jar 127.0.0.1 {} -c \"stop\"", bootstrap.getTelnetPortOrDefault());
            AnsiLog.error("3. Or try to use different telnet port, for example: java -jar jad-boot.jar --telnet-port 9998 --http-port -1");
            System.exit(1);
        }
    }

    private static long findProcessByTelnetClient(String jadHomeDir, int telnetPort) {
        // start java telnet client
        List<String> telnetArgs = new ArrayList<String>();
        telnetArgs.add("-c");
        telnetArgs.add("session");
        telnetArgs.add("--execution-timeout");
        telnetArgs.add("2000");
        // telnet port ,ip
        telnetArgs.add("127.0.0.1");
        telnetArgs.add("" + telnetPort);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            String error = null;
            int status = ProcessUtils.startJADClient(jadHomeDir, telnetArgs, out);
            if (status == STATUS_EXEC_TIMEOUT) {
                error = "detection timeout";
            } else if (status == STATUS_EXEC_ERROR) {
                error = "detection error";
                AnsiLog.error("process status: {}", status);
                AnsiLog.error("process output: {}", out.toString());
            } else {
                // ignore connect error
            }
            if (error != null) {
                AnsiLog.error("The telnet port {} is used, but process {}, you will connect to an unexpected process.", telnetPort, error);
                AnsiLog.error("Try to use a different telnet port, for example: java -jar jad-boot.jar --telnet-port 9998 --http-port -1");
                System.exit(1);
            }

            //parse output, find java pid
            String output = out.toString("UTF-8");
            String javaPidLine = null;
            Scanner scanner = new Scanner(output);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("JAVA_PID")) {
                    javaPidLine = line;
                    break;
                }
            }
            if (javaPidLine != null) {
                // JAVA_PID    10473
                try {
                    String[] strs = javaPidLine.split("JAVA_PID");
                    if (strs.length > 1) {
                        return Long.parseLong(strs[strs.length - 1].trim());
                    }
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        } catch (Throwable ex) {
            AnsiLog.error("Detection telnet port error");
            AnsiLog.error(ex);
        }

        return -1;
    }

    private static String listVersions() {
        StringBuilder result = new StringBuilder(1024);
        List<String> versionList = listNames(JAD_LIB_DIR);
        Collections.sort(versionList);

        result.append("Local versions:\n");
        for (String version : versionList) {
            result.append(" ").append(version).append('\n');
        }
        result.append("Remote versions:\n");

        List<String> remoteVersions = DownloadUtils.readRemoteVersions();
        if (remoteVersions != null) {
            Collections.reverse(remoteVersions);
            for (String version : remoteVersions) {
                result.append(" " + version).append('\n');
            }
        } else {
            result.append(" unknown\n");
        }
        return result.toString();
    }

    private static List<String> listNames(File dir) {
        List<String> names = new ArrayList<String>();
        if (!dir.exists()) {
            return names;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return names;
        }
        for (File file : files) {
            String name = file.getName();
            if (name.startsWith(".") || file.isFile()) {
                continue;
            }
            names.add(name);
        }
        return names;
    }

    private static void verifyJADHome(String jadHome) {
        File home = new File(jadHome);
        if (home.isDirectory()) {
            String[] fileList = { "jad-core.jar", "jad-agent.jar", "jad-spy.jar" };

            for (String fileName : fileList) {
                if (!new File(home, fileName).exists()) {
                    throw new IllegalArgumentException(
                                    fileName + " do not exist, jad home: " + home.getAbsolutePath());
                }
            }
            return;
        }

        throw new IllegalArgumentException("illegal jad home: " + home.getAbsolutePath());
    }

    private static String usage(CLI cli) {
        StringBuilder usageStringBuilder = new StringBuilder();
        UsageMessageFormatter usageMessageFormatter = new UsageMessageFormatter();
        usageMessageFormatter.setOptionComparator(null);
        cli.usage(usageStringBuilder, usageMessageFormatter);
        return UsageRender.render(usageStringBuilder.toString());
    }

    public String getJADHome() {
        return jadHome;
    }

    public String getUseVersion() {
        return useVersion;
    }

    public String getRepoMirror() {
        return repoMirror;
    }

    public boolean isUseHttp() {
        return useHttp;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public String getTargetIpOrDefault() {
        if (this.targetIp == null) {
            return DEFAULT_TARGET_IP;
        } else {
            return this.targetIp;
        }
    }

    public Integer getTelnetPort() {
        return telnetPort;
    }
    
    public int getTelnetPortOrDefault() {
        if (this.telnetPort == null) {
            return DEFAULT_TELNET_PORT;
        } else {
            return this.telnetPort;
        }
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public int getHttpPortOrDefault() {
        if (this.httpPort == null) {
            return DEFAULT_HTTP_PORT;
        } else {
            return this.httpPort;
        }
    }

    public String getCommand() {
        return command;
    }

    public String getBatchFile() {
        return batchFile;
    }

    public boolean isAttachOnly() {
        return attachOnly;
    }

    public long getPid() {
        return pid;
    }

    public boolean isHelp() {
        return help;
    }

    public Long getSessionTimeout() {
        return sessionTimeout;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isVersions() {
        return versions;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public String getTunnelServer() {
        return tunnelServer;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getAppName() {
        return appName;
    }

    public String getStatUrl() {
        return statUrl;
    }

    public String getSelect() {
		return select;
	}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisabledCommands() {
        return disabledCommands;
    }
}
