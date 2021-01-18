package com.joker.nettytest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
    
    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 跟ServerBootstrap相似，不过Bootstrap是用于no-server channels，比如客户端或无连接通道
            Bootstrap b = new Bootstrap();
            // 如果仅指定了一个EventLoopGroup，那么它会同时被用于boss group和worker group
            // The boss worker is not used for the client side though.
            b.group(workerGroup);
            // 使用NioSocketChannel来创建客户端Channel
            b.channel(NioSocketChannel.class);
            // 这里不使用childOption()，因为客户端SocketChannel没有父级
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });

            // Start the client
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}