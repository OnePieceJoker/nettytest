package com.joker.nettytest.protocol.stomp.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class StompWebSocketChatServer {
    
    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

    public void start(final int port) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new StompWebSocketChatServerInitializer("/chat"));

            b.bind(port).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("Open your web browser and navigate to http://127.0.0.1:" + PORT + "/");
                    } else {
                        System.out.println("Cannot start server, follows exception " + future.cause());
                    }
                }
            }).channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new StompWebSocketChatServer().start(PORT);
    }
}