package com.carryking.dopper.websocket.impl;

import com.carryking.dopper.event.CloseEvent;
import com.carryking.dopper.websocket.WebSocketChannel;
import com.carryking.dopper.websocket.WebSocketHandle;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: carryking
 * @Date: 2018/4/21 21:03
 * @Description: WebSocketChannel默认实现
 */
public class DefaultWebSocketChannel implements WebSocketChannel {

    private static final Logger log = LoggerFactory.getLogger(DefaultWebSocketChannel.class);

    /**
     * netty原生Channel
     */
    private Channel channel;

    /**
     * 链接ID 当Channel id不存在时自动生成32位随机字符串
     */
    private String id;

    /**
     * 当前服务器ip
     */
    private String serverIp;

    /**
     * 当前服务器监听端口
     */
    private int serverPort;

    /**
     * 远程客户端ip
     */
    private String remoteIp;

    /**
     * 远程客户端端口
     */
    private int remotePort;

    private WebSocketHandle webSocketHandle;

    public DefaultWebSocketChannel(Channel channel, WebSocketHandle webSocketHandle) {
        this.channel = channel;
        this.webSocketHandle = webSocketHandle;
        init();
    }

    private void init() {
        //netty 4.1 channel's hashcode is unique --> https://github.com/netty/netty/issues/1583
        /*if (channel != null && channel instanceof NioServerSocketChannel && StringUtils.isNotBlank(channel.id().asLongText())) {
            id = channel.id().asLongText();
        } else {
            id = RandomStringUtils.randomAlphanumeric(32);
        }*/

        id = String.valueOf(channel.hashCode());

        if (channel instanceof NioSocketChannel == false) {
            webSocketHandle.onError(this, new RuntimeException("WebSocket Channel is not NioSocketChannel"));
            return;
        }

        NioSocketChannel nioServerSocketChannel = (NioSocketChannel) channel;
        serverIp = nioServerSocketChannel.localAddress().getHostString();
        serverPort = nioServerSocketChannel.localAddress().getPort();

        remoteIp = nioServerSocketChannel.remoteAddress().getHostString();
        remotePort = nioServerSocketChannel.remoteAddress().getPort();
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public String serverIp() {
        return this.serverIp;
    }

    @Override
    public int serverPort() {
        return this.serverPort;
    }

    @Override
    public String remoteIp() {
        return this.remoteIp;
    }

    @Override
    public int remotePort() {
        return this.remotePort;
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public void close() {
        WebSocketChannel webSocketChannel = this;
        channel.close().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                try {
                    webSocketHandle.onClose(webSocketChannel, CloseEvent.ACTIVE_CLOSE);
                } catch (Throwable ex) {
                    log.error("处理WebSocketHandle onClose方法时发生异常");
                }
            }
        });
    }
}
