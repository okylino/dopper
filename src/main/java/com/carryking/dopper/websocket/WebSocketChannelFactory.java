package com.carryking.dopper.websocket;

import com.carryking.dopper.websocket.impl.DefaultWebSocketChannel;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @Author: carryking
 * @Date: 2018/4/21 21:05
 * @Description: WebSocketChannel工厂类
 */
public class WebSocketChannelFactory {

    /**
     * 绑定到Channel的WebSocketChannel键字符串
     */
    private static final String CHANNEL_ATTRIBUTE_KEY = "WebSocketChannel";

    /**
     * 将WebSocketChannel绑定到Channel上下文中,如果Channel中已存在WebSocketChannel则直接返回
     * @param channel
     * @param webSocketHandle
     * @return
     */
    public static WebSocketChannel newInstance(Channel channel,WebSocketHandle webSocketHandle) {

        AttributeKey<WebSocketChannel> webSocketChannelAttributeKey = AttributeKey.valueOf(CHANNEL_ATTRIBUTE_KEY);

        if (channel.hasAttr(webSocketChannelAttributeKey) == false) {
            WebSocketChannel socketChannel = new DefaultWebSocketChannel(channel,webSocketHandle);
            channel.attr(webSocketChannelAttributeKey).set(socketChannel);
        }
        WebSocketChannel webSocketChannel = channel.attr(webSocketChannelAttributeKey).get();
        return webSocketChannel;
    }

}
