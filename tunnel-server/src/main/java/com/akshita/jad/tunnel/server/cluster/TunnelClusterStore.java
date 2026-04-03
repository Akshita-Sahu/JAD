package com.akshita.jad.tunnel.server.cluster;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.akshita.jad.tunnel.server.AgentClusterInfo;

/**
 * agentId tunnel server，
 * 
 * @author hengyunabc 2020-10-27
 *
 */
public interface TunnelClusterStore {
    public void addAgent(String agentId, AgentClusterInfo info, long expire, TimeUnit timeUnit);

    public AgentClusterInfo findAgent(String agentId);

    public void removeAgent(String agentId);

    public Collection<String> allAgentIds();

    public Map<String, AgentClusterInfo> agentInfo(String appName);
}
