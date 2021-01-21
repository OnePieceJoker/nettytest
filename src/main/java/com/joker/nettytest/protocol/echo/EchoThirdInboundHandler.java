package com.joker.nettytest.protocol.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoThirdInboundHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("进入EchoThirdInboundHandler.channelRead方法");
        String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
        System.out.println("EchoThirdInboundHandler.channelRead:" + data);
        System.out.println("退出EchoThirdInboundHandler.channelRead方法");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}