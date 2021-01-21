package com.joker.nettytest.protocol.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    
    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 对于ChannelOutboundHandler而言，它们的调用顺序是根据添加时的顺序倒序处理的
                        // ChannelOutboundHandler的声明要在最后一个有效(InboundHandler中调用了fireChannelRead())的InboudHandler的声明之前
                        ch.pipeline().addLast(new EchoFirstOutboundHandler());
                        ch.pipeline().addLast(new EchoSecondOutboundHandler());

                        // addLast()方法
                        // 对于ChannelInboundHandler而言，它们的调用顺序是根据添加时的顺序正序调用的
                        ch.pipeline().addLast(new EchoFirstInboundHandler());
                        ch.pipeline().addLast(new EchoSecondInboundHandler());
                        ch.pipeline().addLast(new EchoThirdInboundHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            System.out.println("EchoServer服务正在启动...");
            ChannelFuture f = b.bind(port).sync();
            System.out.println("EchoServer服务启动成功，绑定端口为：" + port);
            f.channel().closeFuture().sync();
            System.out.println("EchoServer服务关闭");
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new EchoServer(port).run();
    }
}