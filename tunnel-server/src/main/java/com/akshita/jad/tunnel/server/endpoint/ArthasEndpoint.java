package com.akshita.jad.tunnel.server.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import com.akshita.jad.tunnel.server.TunnelServer;
import com.akshita.jad.tunnel.server.app.configuration.JADProperties;

@Endpoint(id = "jad")
public class JADEndpoint {

    @Autowired
    JADProperties jadProperties;
    @Autowired
    TunnelServer tunnelServer;

    @ReadOperation
    public Map<String, Object> invoke() {
        Map<String, Object> result = new HashMap<>(4);

        result.put("version", this.getClass().getPackage().getImplementationVersion());
        result.put("properties", jadProperties);

        result.put("agents", tunnelServer.getAgentInfoMap());
        result.put("clientConnections", tunnelServer.getClientConnectionInfoMap());

        return result;
    }

}
