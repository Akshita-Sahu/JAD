package com.akshita.jad.tunnel.server.endpoint;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.akshita.jad.tunnel.server.app.configuration.JADProperties;

@EnableConfigurationProperties(JADProperties.class)
@Configuration
public class JADEndPointAutoconfiguration {

    @ConditionalOnMissingBean
    @Bean
    @ConditionalOnAvailableEndpoint
    public JADEndpoint jadEndPoint() {
        return new JADEndpoint();
    }
}
