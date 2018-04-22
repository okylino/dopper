package com.carryking.dopper.websocket;

import com.carryking.dopper.entity.UpgradeResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author: carryking
 * @Date: 2018/4/21 21:00
 * @Description: WebSocketHandle抽象实现
 */
public abstract class AbstractWebSocketHandle implements WebSocketHandle{

    private Logger log = LoggerFactory.getLogger(AbstractWebSocketHandle.class);

    @Override
    public UpgradeResponse beforeOpen(WebSocketChannel channel,HttpHeaders headers) {
        UpgradeResponse response = new UpgradeResponse(true, HttpResponseStatus.OK);
        return response;
    }

    @Override
    public void onError(WebSocketChannel channel,Throwable ex) {
        ex.printStackTrace();
        channel.close();
    }

}
