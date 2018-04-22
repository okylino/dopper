package com.carryking.dopper.websocket;

/**
 * @Author: carryking
 * @Date: 2018/4/21 18:12
 * @Description: 消息发送服务接口
 */
public interface SendService {

    /**
     * 向远程客户端发送字符串
     * @param channel
     * @param content
     */
    void send(WebSocketChannel channel, String content);

    /**
     * 向远程客户端发送字节数组
     * @param channel
     * @param bytes
     */
    void send(WebSocketChannel channel, byte[] bytes);

}
