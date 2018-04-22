package com.carryking.dopper.websocket;

import com.carryking.dopper.entity.UpgradeResponse;
import com.carryking.dopper.event.CloseEvent;
import io.netty.handler.codec.http.HttpHeaders;
import org.springframework.context.ApplicationContext;

/**
 * @Author: carryking
 * @Date: 2018/4/21 20:48
 * @Description: WebSocket处理拦截接口
 */
public interface WebSocketHandle {

    /**
     * 在协议进行 HTTP -> WebSocket协议升级时调用
     * 如果返回false则代表升级失败 关闭连接
     *
     * @return
     */
    UpgradeResponse beforeOpen(WebSocketChannel channel, HttpHeaders headers);

    /**
     * WebSocket连接打开时调用
     */
    void open(WebSocketChannel channel);

    /**
     * 处理文本消息
     *
     * @param channel
     * @param text
     */
    void onMessage(WebSocketChannel channel, String text);

    /**
     * 处理二进制消息
     *
     * @param channel
     * @param bytes
     */
    void onMessage(WebSocketChannel channel, byte[] bytes);

    /**
     * 处理消息发生异常时调用 异常时会主动关闭链接 无须再调用WebSocketChannel的close方法
     *
     * @param channel
     */
    void onError(WebSocketChannel channel, Throwable ex);

    /**
     * 链接关闭时调用
     *
     * @param channel
     */
    void onClose(WebSocketChannel channel, CloseEvent event);

    /**
     * 服务器成功启动时调用
     *
     * @param ctx
     */
    void onStarted(ApplicationContext ctx);
}
