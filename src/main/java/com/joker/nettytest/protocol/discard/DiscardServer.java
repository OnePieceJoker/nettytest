package com.joker.nettytest.protocol.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
    
    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // NioEventLoopGroup是处理I/O操作的多线程事件循环
        // How many Threads are used and how they are mapped to the created Channel's 
        // depends on the EventLoopGroup implementtation and may be even configurable via a constructor.
        // 默认是jvm可用的最大处理器数量

        // bossGroup用于接手传入的连接(accepts an incoming connection.)
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // workerGroup：当boss接受连接并向worker注册了接受的连接的时候，worker来处理接受到的连接的流量
        // handlers the traffic of the accepted connection once the boss accepts the connection
        // and registers the accepted connection to the worker.
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // ServerBootstrap is a helper class that sets up a server.
            // 我们可以使用Channel直接设置server.
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            // 这里是指定使用NioServerSockerChannel类用于实例化新的Channel来接受新的连接.
                .channel(NioServerSocketChannel.class)
                // 这里来指定下自创建的handler.
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DiscardServerHandler());
                    }
                })
                // 设置特定于Channel实现的参数
                // option()用于接受传入连接的NioServerSocketChannel
                .option(ChannelOption.SO_BACKLOG, 128)
                // childOption()用于父级ServerChannel接收的channel.
                // 这里是NioSocketChannel
                .childOption(ChannelOption.SO_KEEPALIVE, true);

                // Bind and start to accept incoming connections.
                ChannelFuture f = b.bind(port).sync();

                // Wait until the sever socker is closed.
                // In this example, this does not happen, but you can do that to gracefully.
                // shut down your server.
                f.channel().closeFuture().sync();
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

        new DiscardServer(port).run();
    }
}