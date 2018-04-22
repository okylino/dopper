package com.carryking.dopper.netty;

import com.carryking.dopper.entity.UpgradeResponse;
import com.carryking.dopper.event.CloseEvent;
import com.carryking.dopper.websocket.WebSocketChannel;
import com.carryking.dopper.websocket.WebSocketChannelFactory;
import com.carryking.dopper.websocket.WebSocketHandle;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author: carryking
 * @Date: 2018/4/21 21:53
 * @Description: WebSocket消息处理
 */
public class DefaultWebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private WebSocketHandle webSocketHandle;

    private static final Logger log = LoggerFactory.getLogger(DefaultWebSocketFrameHandler.class);

    public DefaultWebSocketFrameHandler(WebSocketHandle webSocketHandle) {
        this.webSocketHandle = webSocketHandle;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {

        if (msg instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) msg).text();
            WebSocketChannel webSocketChannel = WebSocketChannelFactory.newInstance(ctx.channel(), webSocketHandle);

            try {
                webSocketHandle.onMessage(webSocketChannel, text);
            } catch (Throwable throwable) {
                log.error("WebSocketHandle处理onMessage时发生异常;WebSocketChannelId:{},text:{}", webSocketChannel.id(), text, throwable);
                ctx.fireExceptionCaught(throwable);
            }
        }

        if (msg instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) msg;
            int count = binaryWebSocketFrame.content().readableBytes();
            byte[] bytes = new byte[count];
            binaryWebSocketFrame.content().readBytes(bytes);
            WebSocketChannel webSocketChannel = WebSocketChannelFactory.newInstance(ctx.channel(), webSocketHandle);
            try {
                webSocketHandle.onMessage(webSocketChannel, bytes);
            } catch (Throwable throwable) {
                log.error("WebSocketHandle处理onMessage时发生异常;WebSocketChannelId:{},bytes", webSocketChannel.id(), throwable);
                ctx.fireExceptionCaught(throwable);
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        //upgrade拦截 判断升级(身份校验?)是否成功
        boolean success = false;
        final WebSocketChannel webSocketChannel = WebSocketChannelFactory.newInstance(ctx.channel(), webSocketHandle);
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete complete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;

            UpgradeResponse response = webSocketHandle.beforeOpen(webSocketChannel, complete.requestHeaders());

            if (log.isWarnEnabled() &&
                    ((response.isAccess() && response.getStatus() != HttpResponseStatus.OK)
                            || (response.isAccess() == false && response.getStatus() == HttpResponseStatus.OK))) {
                log.warn("upgrade过程中,UpgradeResponse返回的状态与Http状态码不对应");
            }

            success = response.isAccess();
            if (success == false) {
                DefaultFullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, response.getStatus(),
                        Unpooled.EMPTY_BUFFER, false);
                ctx.channel().writeAndFlush(res).addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        ctx.channel().close().addListener(new GenericFutureListener<Future<? super Void>>() {
                            @Override
                            public void operationComplete(Future<? super Void> future) throws Exception {
                                //身份校验不通过
                                webSocketHandle.onClose(webSocketChannel, CloseEvent.ACTIVE_CLOSE);
                            }
                        });
                    }
                });
            }
        }
        if (success) {
            ctx.channel().newSucceededFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    webSocketHandle.open(webSocketChannel);
                }
            });
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        WebSocketChannel webSocketChannel = WebSocketChannelFactory.newInstance(ctx.channel(), webSocketHandle);
        try {
            webSocketHandle.onError(webSocketChannel, cause);

            if (cause instanceof WebSocketHandshakeException) {
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HTTP_1_1, HttpResponseStatus.BAD_REQUEST, Unpooled.wrappedBuffer(cause.getMessage().getBytes()));
                ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE).addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        webSocketHandle.onClose(webSocketChannel, CloseEvent.PASSIVITY_CLOSE);
                    }
                });
            }
        } catch (Throwable throwable) {
            log.error("处理onError时发生异常,关闭链接",throwable);
            ctx.close().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    webSocketHandle.onClose(webSocketChannel, CloseEvent.PASSIVITY_CLOSE);
                }
            });
        }
    }


}
