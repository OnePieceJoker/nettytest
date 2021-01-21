package com.joker.nettytest.protocol.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class EchoThirdOutboundHandler extends ChannelOutboundHandlerAdapter {
    
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("进入EchoThirdOutboundHandler方法");
        ctx.write(msg);
        System.out.println("退出EchoThirdOutboundHandler方法");
    }
}