package com.akshita.jad.tunnel.server.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.akshita.jad.tunnel.server.app.configuration.JADProperties;

/**
 * 
 * @author hengyunabc 2021-08-11
 *
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JADProperties jadProperties;
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).authenticated().anyRequest()
        .permitAll().and().formLogin();
        // allow iframe
        if (jadProperties.isEnableIframeSupport()) {
            httpSecurity.headers().frameOptions().disable();
        }
    }
}