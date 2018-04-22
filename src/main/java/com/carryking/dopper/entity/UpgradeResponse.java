package com.carryking.dopper.entity;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @Author: carryking
 * @Date: 2018/4/22 0:25
 * @Description: WebSocket协议升级时返回信息
 */
public class UpgradeResponse {

    /**
     * 身份校验不通过或取消打开WS通道时返回false
     */
    private boolean access;

    /**
     * 当access为false返回给客户端的状态码
     */
    private HttpResponseStatus status;

    public UpgradeResponse(boolean access, HttpResponseStatus status) {
        this.access = access;
        this.status = status;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }
}
