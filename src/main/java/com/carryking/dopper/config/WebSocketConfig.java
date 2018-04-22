package com.carryking.dopper.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: carryking
 * @Date: 2018/4/21 21:44
 * @Description: WebSocketServer配置实体
 */
@Component
public class WebSocketConfig {

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
    @Value("${server.enableSSL:false}")
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

        if (StringUtils.isBlank(url)) {
            return "";
        }

        if ("/".equals(StringUtils.right(url, 1))) {
            url = StringUtils.right(url, url.length() - 1);
        }

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
}
