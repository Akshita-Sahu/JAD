package com.akshita.jad.tunnel.server.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = { "com.akshita.jad.tunnel.server.app",
        "com.akshita.jad.tunnel.server.endpoint" })
@EnableCaching
public class JADTunnelApplication {

    public static void main(String[] args) {
        SpringApplication.run(JADTunnelApplication.class, args);
    }

}
