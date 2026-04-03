package com.akshita.jad.tunnel.server.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.akshita.jad.tunnel.server.TunnelServer;
import com.akshita.jad.tunnel.server.cluster.TunnelClusterStore;

/**
 * 
 * @author hengyunabc 2020-10-27
 *
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class TunnelServerConfiguration {

    @Autowired
    JADProperties jadProperties;

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public TunnelServer tunnelServer(@Autowired(required = false) TunnelClusterStore tunnelClusterStore) {
        TunnelServer tunnelServer = new TunnelServer();

        tunnelServer.setHost(jadProperties.getServer().getHost());
        tunnelServer.setPort(jadProperties.getServer().getPort());
        tunnelServer.setSsl(jadProperties.getServer().isSsl());
        tunnelServer.setPath(jadProperties.getServer().getPath());
        tunnelServer.setClientConnectHost(jadProperties.getServer().getClientConnectHost());
        if (tunnelClusterStore != null) {
            tunnelServer.setTunnelClusterStore(tunnelClusterStore);
        }
        return tunnelServer;
    }

}
