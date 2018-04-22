package com.carryking.dopper.event;

import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;

/**
 * @Author: carryking
 * @Date: 2018/4/21 20:56
 * @Description: WebSocket链接关闭事件枚举
 */
public enum CloseEvent {

    /**
     * 主动关闭
     * 1.主动调用{@link com.carryking.dopper.websocket.WebSocketHandle}的close方法
     * 2.{@link com.carryking.dopper.websocket.WebSocketHandle}的beforeOpen返回失败
     */
    ACTIVE_CLOSE,

    /**
     * 被动关闭
     * 1.处理过程发生异常
     * 2.远程发送关闭控制帧{@link CloseWebSocketFrame}
     */
    PASSIVITY_CLOSE,

}
