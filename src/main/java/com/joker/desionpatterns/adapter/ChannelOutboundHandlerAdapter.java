package com.joker.desionpatterns.adapter;

import java.net.SocketAddress;

import io.netty.channel.ChannelPromise;

public class ChannelOutboundHandlerAdapter extends ChannelHandlerAdapter implements ChannelOutboundHandler {

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
            ChannelPromise promise) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub

    }
    
}
