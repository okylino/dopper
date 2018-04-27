package com.carryking.dopper.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: carryking
 * @Date: 2018/4/21 21:44
 * @Description: WebSocketServer配置实体
 */
@Component
public class WebSocketConfig {

    private final static String PATH_SUFFIX = "/";

    /**
     * host
     */
    @Value("${server.host:0.0.0.0}")
    private String host;

    /**
     * port
     */
    @Value("${server.port:80}")
    private int port;

    /**
     * 是否启用SSL
     */
    @Value("${server.ssl.enable:false}")
    private boolean enableSSL;

    /**
     * WebSocket URL
     */
    @Value("${url:/}")
    private String url;

    /**
     * maxFrameSize
     */
    @Value("${maxFrameSize:65536}")
    private int maxFrameSize;

    /**
     * 证书路径地址
     */
    @Value("${server.ssl.crt:}")
    private String serverCrtPath;

    /**
     * PKCS8私钥地址
     */
    @Value("${server.ssl.pkcs8:}")
    private String serverPKCS8Path;

    @PostConstruct
    private void init() {

        if (StringUtils.isBlank(url)) {
            url = PATH_SUFFIX;
        }

        if (PATH_SUFFIX.equals(StringUtils.right(url, 1))) {
            url = StringUtils.right(url, url.length() - 1);
        }

        url = StringUtils.isBlank(url) ? "/" : url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isEnableSSL() {
        return enableSSL;
    }

    public void setEnableSSL(boolean enableSSL) {
        this.enableSSL = enableSSL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getMaxFrameSize() {
        return maxFrameSize;
    }

    public void setMaxFrameSize(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    public String getServerCrtPath() {
        return serverCrtPath;
    }

    public void setServerCrtPath(String serverCrtPath) {
        this.serverCrtPath = serverCrtPath;
    }

    public String getServerPKCS8Path() {
        return serverPKCS8Path;
    }

    public void setServerPKCS8Path(String serverPKCS8Path) {
        this.serverPKCS8Path = serverPKCS8Path;
    }

    @Override
    public String toString() {
        return "WebSocketConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", enableSSL=" + enableSSL +
                ", url='" + url + '\'' +
                ", maxFrameSize=" + maxFrameSize +
                ", serverCrtPath='" + serverCrtPath + '\'' +
                ", serverPKCS8Path='" + serverPKCS8Path + '\'' +
                '}';
    }
}
