package com.akshita.jad.tunnel.server;

/**
 * @author hengyunabc 2020-10-30
 *
 */
public class AgentClusterInfo {
    /**
     * agentip tunnel server
     */
    private String host;
    private int port;
    private String jadVersion;

    /**
     * agent  tunnel server ip  port
     */
    private String clientConnectHost;
    private int clientConnectTunnelPort;

    public AgentClusterInfo() {

    }

    public AgentClusterInfo(AgentInfo agentInfo, String clientConnectHost, int clientConnectTunnelPort) {
        this.host = agentInfo.getHost();
        this.port = agentInfo.getPort();
        this.jadVersion = agentInfo.getJADVersion();
        this.clientConnectHost = clientConnectHost;
        this.clientConnectTunnelPort = clientConnectTunnelPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getJADVersion() {
        return jadVersion;
    }

    public void setJADVersion(String jadVersion) {
        this.jadVersion = jadVersion;
    }

    public String getClientConnectHost() {
        return clientConnectHost;
    }

    public void setClientConnectHost(String clientConnectHost) {
        this.clientConnectHost = clientConnectHost;
    }

    public int getClientConnectTunnelPort() {
        return clientConnectTunnelPort;
    }

    public void setClientConnectTunnelPort(int clientConnectTunnelPort) {
        this.clientConnectTunnelPort = clientConnectTunnelPort;
    }

}
