package com.carryking.dopper.netty;

import com.carryking.dopper.config.WebSocketConfig;
import com.carryking.dopper.websocket.WebSocketHandle;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @Author: carryking
 * @Date: 2018/4/21 21:39
 * @Description:
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger log = LoggerFactory.getLogger(ServerChannelInitializer.class);

    private WebSocketConfig config;

    private WebSocketHandle webSocketHandle;

    public ServerChannelInitializer(WebSocketConfig config, WebSocketHandle webSocketHandle) {
        this.config = config;
        this.webSocketHandle = webSocketHandle;
        init();
    }

    private static SslContext sslCtx = null;


    private void init()
    {
        if (config.isEnableSSL()) {

            if (StringUtils.isBlank(config.getServerCrtPath()) || StringUtils.isBlank(config.getServerPKCS8Path())) {
                if (log.isDebugEnabled()) {
                    log.debug("WebSocketConfig:{}",config);
                }
                throw new RuntimeException("服务器初始化:启用SSL配置但是未配置证书地址或私钥地址,系统启动失败");
            }

            try {
                File certChainFile = new File(config.getServerCrtPath());
                File keyFile = new File(config.getServerPKCS8Path());
                sslCtx = SslContextBuilder.forServer(certChainFile, keyFile).sslProvider(SslProvider.OPENSSL)
                        .clientAuth(ClientAuth.NONE).build();

            } catch (Throwable ex) {
                log.error("服务器初始化:加载证书地址时发生异常,系统启动失败;crtFilePath={},pkcs8FilePath={}", config.getServerCrtPath(), config.getServerPKCS8Path());
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        if (sslCtx != null && config.isEnableSSL()) {
            pipeline.addLast(sslCtx.newHandler(channel.alloc()));
        }

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new WebSocketServerProtocolHookHandler(webSocketHandle, config.getUrl(), null, false, config.getMaxFrameSize(), false));
        pipeline.addLast(new DefaultWebSocketFrameHandler(webSocketHandle));
    }
}
