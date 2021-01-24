package com.joker.desionpatterns.adapter;

import java.net.SocketAddress;

import io.netty.channel.ChannelPromise;

/**
 * 让ChannelHandler类进行职责分离 该类是ChannelHandler类的一个扩展类，用于处理io-outbound-operations
 */
public interface ChannelOutboundHandler extends ChannelHandler {
    
    void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception;

    void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, 
        SocketAddress localAddress, ChannelPromise promise) throws Exception;

    void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception;

    void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception;

    void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception;

    void read(ChannelHandlerContext ctx) throws Exception;

    void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception;

    void flush(ChannelHandlerContext ctx) throws Exception;
}
