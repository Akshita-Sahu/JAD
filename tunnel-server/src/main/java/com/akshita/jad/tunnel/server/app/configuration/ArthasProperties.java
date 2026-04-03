package com.akshita.jad.tunnel.server.app.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.akshita.jad.tunnel.server.utils.InetAddressUtil;
import com.akshita.jad.common.JADConstants;

/**
 * 
 * @author hengyunabc 2019-08-29
 *
 */
@Component
@ConfigurationProperties(prefix = "jad")
public class JADProperties {

    private Server server;

    private EmbeddedRedis embeddedRedis;

    /**
     * supoort apps.html/agents.html
     */
    private boolean enableDetailPages = false;

    private boolean enableIframeSupport = true;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public EmbeddedRedis getEmbeddedRedis() {
        return embeddedRedis;
    }

    public void setEmbeddedRedis(EmbeddedRedis embeddedRedis) {
        this.embeddedRedis = embeddedRedis;
    }

    public boolean isEnableDetailPages() {
        return enableDetailPages;
    }

    public void setEnableDetailPages(boolean enableDetailPages) {
        this.enableDetailPages = enableDetailPages;
    }

    public boolean isEnableIframeSupport() {
        return enableIframeSupport;
    }

    public void setEnableIframeSupport(boolean enableIframeSupport) {
        this.enableIframeSupport = enableIframeSupport;
    }

    public static class Server {
        /**
         * tunnel server listen host
         */
        private String host;
        private int port;
        private boolean ssl;
        private String path = JADConstants.DEFAULT_WEBSOCKET_PATH;

        /**
         * 。redis，tunnel server。
         */
        private String clientConnectHost = InetAddressUtil.getInetAddress();

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

        public boolean isSsl() {
            return ssl;
        }

        public void setSsl(boolean ssl) {
            this.ssl = ssl;
        }

        public String getClientConnectHost() {
            return clientConnectHost;
        }

        public void setClientConnectHost(String clientConnectHost) {
            this.clientConnectHost = clientConnectHost;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

    /**
     * for test
     * 
     * @author hengyunabc 2020-11-03
     *
     */
    public static class EmbeddedRedis {
        private boolean enabled = false;
        private String host = "127.0.0.1";
        private int port = 6379;
        private List<String> settings = new ArrayList<String>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
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

        public List<String> getSettings() {
            return settings;
        }

        public void setSettings(List<String> settings) {
            this.settings = settings;
        }
    }

}
