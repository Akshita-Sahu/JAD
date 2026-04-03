package com.akshita.jad.tunnel.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author hengyunabc 2019-08-27
 *
 */
public class AgentInfo {

    @JsonIgnore
    private ChannelHandlerContext channelHandlerContext;
    private String host;
    private int port;
    private String jadVersion;

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
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

}
