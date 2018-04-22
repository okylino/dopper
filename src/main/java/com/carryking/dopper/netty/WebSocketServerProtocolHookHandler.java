package com.carryking.dopper.netty;

import com.carryking.dopper.event.CloseEvent;
import com.carryking.dopper.websocket.WebSocketChannel;
import com.carryking.dopper.websocket.WebSocketChannelFactory;
import com.carryking.dopper.websocket.WebSocketHandle;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: carryking
 * @Date: 2018/4/22 17:02
 * @Description:
 */
public class WebSocketServerProtocolHookHandler extends WebSocketServerProtocolHandler {

    private Logger log = LoggerFactory.getLogger(WebSocketServerProtocolHookHandler.class);

    private WebSocketHandle webSocketHandle;

    public WebSocketServerProtocolHookHandler(WebSocketHandle webSocketHandle, String websocketPath) {
        super(websocketPath);
        this.webSocketHandle = webSocketHandle;
    }

    public WebSocketServerProtocolHookHandler(WebSocketHandle webSocketHandle, String websocketPath, String subprotocols) {
        super(websocketPath, subprotocols);
        this.webSocketHandle = webSocketHandle;
    }

    public WebSocketServerProtocolHookHandler(WebSocketHandle webSocketHandle, String websocketPath, String subprotocols, boolean allowExtensions) {
        super(websocketPath, subprotocols, allowExtensions);
        this.webSocketHandle = webSocketHandle;
    }

    public WebSocketServerProtocolHookHandler(WebSocketHandle webSocketHandle, String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize) {
        super(websocketPath, subprotocols, allowExtensions, maxFrameSize);
        this.webSocketHandle = webSocketHandle;
    }

    public WebSocketServerProtocolHookHandler(WebSocketHandle webSocketHandle, String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch) {
        super(websocketPath, subprotocols, allowExtensions, maxFrameSize, allowMaskMismatch);
        this.webSocketHandle = webSocketHandle;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //统一传递到最后的Handle处理
        ctx.fireExceptionCaught(cause);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        if (frame instanceof CloseWebSocketFrame) {
            WebSocketChannel webSocketChannel = WebSocketChannelFactory.newInstance(ctx.channel(), webSocketHandle);
            webSocketHandle.onClose(webSocketChannel, CloseEvent.PASSIVITY_CLOSE);
        }
        super.decode(ctx, frame, out);
    }


}
