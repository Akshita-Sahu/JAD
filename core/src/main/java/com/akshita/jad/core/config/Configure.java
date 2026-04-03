package com.akshita.jad.core.config;

import com.akshita.jad.core.shell.ShellServerOptions;
import com.akshita.jad.core.util.reflect.JADReflectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static java.lang.reflect.Modifier.isStatic;

/**
 * <pre>
 * 。
 * ，。
 *  com.akshita.jad.core.JAD#attach  Configure#toStrig
 * <pre>
 *
 * @author vlinux
 * @author hengyunabc 2018-11-12
 */
@Config(prefix = "jad")
public class Configure {

    private String ip;
    private Integer telnetPort;
    private Integer httpPort;
    private Long javaPid;
    private String jadCore;
    private String jadAgent;

    private String tunnelServer;
    private String agentId;

    private String username;
    private String password;

    /**
     * @see com.akshita.jad.common.JADConstants#JAD_OUTPUT
     */
    private String outputPath;

    /**
     * ClassLoader， , 
     */
    private String enhanceLoaders;

    /**
     * <pre>
     * 1.  jad.agentId ，
     * 2. ， appname，， system properties project.name demo，
     *     agentId  demo-xxxx
     * </pre>
     */
    private String appName;
    /**
     * report executed command
     */
    private String statUrl;

    /**
     * session timeout seconds
     * @see ShellServerOptions#DEFAULT_SESSION_TIMEOUT
     */
    private Long sessionTimeout;

    /**
     * disabled commands
     */
    private String disabledCommands;

    /**
     * ，password。jad.properties true
     */
    private Boolean localConnectionNonAuth;

    /**
     * MCP (Model Context Protocol) endpoint path
     */
    private String mcpEndpoint;

    /**
     * MCP Server Protocol: STREAMABLE or STATELESS
     */
    private String mcpProtocol;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getTelnetPort() {
        return telnetPort;
    }

    public void setTelnetPort(int telnetPort) {
        this.telnetPort = telnetPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public long getJavaPid() {
        return javaPid;
    }

    public void setJavaPid(long javaPid) {
        this.javaPid = javaPid;
    }

    public String getJADAgent() {
        return jadAgent;
    }

    public void setJADAgent(String jadAgent) {
        this.jadAgent = jadAgent;
    }

    public String getJADCore() {
        return jadCore;
    }

    public void setJADCore(String jadCore) {
        this.jadCore = jadCore;
    }

    public Long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getTunnelServer() {
        return tunnelServer;
    }

    public void setTunnelServer(String tunnelServer) {
        this.tunnelServer = tunnelServer;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getStatUrl() {
        return statUrl;
    }

    public void setStatUrl(String statUrl) {
        this.statUrl = statUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnhanceLoaders() {
        return enhanceLoaders;
    }

    public void setEnhanceLoaders(String enhanceLoaders) {
        this.enhanceLoaders = enhanceLoaders;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisabledCommands() {
        return disabledCommands;
    }

    public void setDisabledCommands(String disabledCommands) {
        this.disabledCommands = disabledCommands;
    }

    public boolean isLocalConnectionNonAuth() {
        return localConnectionNonAuth != null && localConnectionNonAuth;
    }

    public void setLocalConnectionNonAuth(boolean localConnectionNonAuth) {
        this.localConnectionNonAuth = localConnectionNonAuth;
    }

    public String getMcpEndpoint() {
        return mcpEndpoint;
    }

    public void setMcpEndpoint(String mcpEndpoint) {
        this.mcpEndpoint = mcpEndpoint;
    }

    public String getMcpProtocol() {
        return mcpProtocol;
    }

    public void setMcpProtocol(String mcpProtocol) {
        this.mcpProtocol = mcpProtocol;
    }

    /**
     * 
     *
     * @return 
     */
    @Override
    public String toString() {

        final Map<String, String> map = new HashMap<String, String>();
        for (Field field : JADReflectUtils.getFields(Configure.class)) {

            // 
            if (isStatic(field.getModifiers())) {
                continue;
            }

            // 
            try {
                Object fieldValue = JADReflectUtils.getFieldValueByField(this, field);
                if (fieldValue != null) {
                    map.put(field.getName(), String.valueOf(fieldValue));
                }
            } catch (Throwable t) {
                //
            }

        }

        return FeatureCodec.DEFAULT_COMMANDLINE_CODEC.toString(map);
    }

    /**
     * 
     *
     * @param toString 
     * @return 
     */
    public static Configure toConfigure(String toString) throws IllegalAccessException {
        final Configure configure = new Configure();
        final Map<String, String> map = FeatureCodec.DEFAULT_COMMANDLINE_CODEC.toMap(toString);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            final Field field = JADReflectUtils.getField(Configure.class, entry.getKey());
            if (null != field && !isStatic(field.getModifiers())) {
                JADReflectUtils.set(field, JADReflectUtils.valueOf(field.getType(), entry.getValue()), configure);
            }
        }
        return configure;
    }

}
