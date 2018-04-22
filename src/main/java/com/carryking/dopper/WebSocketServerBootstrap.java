package com.carryking.dopper;

import com.carryking.dopper.config.WebSocketConfig;
import com.carryking.dopper.netty.ServerChannelInitializer;
import com.carryking.dopper.websocket.WebSocketHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: carryking
 * @Date: 2018/4/21 17:34
 * @Description: WebSocket服务启动类
 */
@ComponentScan(basePackageClasses = WebSocketServerBootstrap.class)
public class WebSocketServerBootstrap {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServerBootstrap.class);

    public static void run(Class classz) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(classz,WebSocketServerBootstrap.class);

        WebSocketConfig config = context.getBean(WebSocketConfig.class);

        if (config == null) {
            log.error("WebSocket Server启动失败,Bean-->WebSocketConfig 没有成功加载");
            return;
        }

        WebSocketHandle webSocketHandle = context.getBean(WebSocketHandle.class);

        if (webSocketHandle == null) {
            log.error("WebSocket Server启动失败,Bean-->WebSocketHandle 没有成功加载");
            return;
        }

        startServer(config,webSocketHandle,context);
    }

    private static void startServer(WebSocketConfig config, WebSocketHandle handle, ApplicationContext context) {

        ServerBootstrap bootstrap = new ServerBootstrap();

        EventLoopGroup work = new NioEventLoopGroup();
        EventLoopGroup boss = new NioEventLoopGroup();

        try {
            bootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(config.getHost(),config.getPort())
                    .childHandler(new ServerChannelInitializer(config, handle));

            ChannelFuture future = bootstrap.bind(config.getPort()).sync();

            handle.onStarted(context);

            log.info("WebSocket Server Started SUCCESS !! Bind on {}:{}{}", config.getHost(), config.getPort(), config.getUrl());
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            log.info("WebSocket Server Started Fail",ex);
        }
    }

}
