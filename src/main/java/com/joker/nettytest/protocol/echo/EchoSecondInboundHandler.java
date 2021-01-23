package com.joker.nettytest.protocol.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoSecondInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("进入EchoSecondInboundHandler方法");;
        String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
        System.out.println("EchoSescondInboundHandler.channelRead：" + data);
        // ctx.writeAndFlush(msg);
        // 不要在调用了write()或writeAndFlush()方法之后再去使用msg变量
        // 建议：
        // 1. 如果有需要在InboundHandle链中调用OutboundHandle的情况
        //    一定要先备份下msg数据，免得影响后续的处理
        // 2. 建议将msg的副本传递到下一个InboundHandler
        ctx.fireChannelRead(Unpooled.copiedBuffer("From Second handler message:" + data, CharsetUtil.UTF_8));
        // ctx.fireChannelRead(msg);
        // ctx.fireChannelRead(msg);
        System.out.println("退出EchoSecondInboundHandler方法");;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("进入Second Inbound Handler method: channelReadComplete");
        System.out.println("退出Second Inbound Handler method: channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    
}