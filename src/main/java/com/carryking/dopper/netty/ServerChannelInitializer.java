package com.carryking.dopper.netty;

import com.carryking.dopper.config.WebSocketConfig;
import com.carryking.dopper.websocket.WebSocketHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author: carryking
 * @Date: 2018/4/21 21:39
 * @Description:
 */
public class ServerChannelInitializer extends ChannelInitializer<Channel> {

    private WebSocketConfig config;

    private WebSocketHandle webSocketHandle;

    public ServerChannelInitializer(WebSocketConfig config, WebSocketHandle webSocketHandle) {
        this.config = config;
        this.webSocketHandle = webSocketHandle;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        //TODO 如果需要开启SSL 请做相关的操作 暂不处理

        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new WebSocketServerProtocolHookHandler(webSocketHandle,config.getUrl(),null,false,config.getMaxFrameSize(),false));
        pipeline.addLast(new DefaultWebSocketFrameHandler(webSocketHandle));
    }
}
