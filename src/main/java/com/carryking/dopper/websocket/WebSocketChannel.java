package com.carryking.dopper.websocket;

import io.netty.channel.Channel;

/**
 * @Author: carryking
 * @Date: 2018/4/21 18:04
 * @Description: WebSocket连接接口
 */
public interface WebSocketChannel {

    /**
     * 当前websocket连接id
     * @return
     */
    String id();

    /**
     * 当前服务器绑定ip地址
     * @return
     */
    String serverIp();

    /**
     * 当前服务器绑定的端口
     * @return
     */
    int serverPort();

    /**
     * 远程连接ip
     * @return
     */
    String remoteIp();

    /**
     * 远程端口
     * @return
     */
    int remotePort();

    /**
     * 获取netty原生channel
     * @return
     */
    Channel channel();

    /**
     * 请求关闭远程与客户端的连接
     */
    void close();
}
