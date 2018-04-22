package com.carryking.dopper.websocket.impl;

import com.carryking.dopper.websocket.SendService;
import com.carryking.dopper.websocket.WebSocketChannel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: carryking
 * @Date: 2018/4/21 20:21
 * @Description: 消息发送服务接口实现
 */
@Service
public class SendServiceImpl implements SendService {

    private static final Logger log = LoggerFactory.getLogger(SendServiceImpl.class);

    @Override
    public void send(WebSocketChannel channel, String content) {
        if (content == null) {
            return;
        }

        if (channelIsOpen(channel.channel()) == false) {
            if (log.isDebugEnabled()) {

                log.debug("发送文本消息失败,远程channel已关闭;WebSocketId:{},SendContext:{}", channel.id(), content);
                return;
            }
        }
        ChannelFuture future = channel.channel().writeAndFlush(new TextWebSocketFrame(content));
        if (log.isDebugEnabled()) {
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    log.debug("发送文本消息成功;WebSocketId:{},SendContext:{}", channel.id(), content);
                }
            });
        }
    }

    @Override
    public void send(WebSocketChannel channel, byte[] bytes) {

        if (bytes == null) {
            return;
        }

        if (channelIsOpen(channel.channel()) == false) {
            if (log.isDebugEnabled()) {

                log.debug("发送byte[]消息失败,远程channel已关闭;channelLongId:{},WebSocketId:{},SendContext:{}",
                        channel.id(), bytes);
                return;
            }
        }
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        ChannelFuture future = channel.channel().writeAndFlush(new BinaryWebSocketFrame(byteBuf));
        if (log.isDebugEnabled()) {
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    log.debug("发送byte[]消息成功;channelLongId:{},WebSocketId:{},SendContext:{}", channel.id(), bytes);
                }
            });
        }
    }

    private boolean channelIsOpen(Channel channel) {
        if (channel == null) {
            return false;
        }
        //
        return channel.isActive();
    }

}
