package com.joker.nettytest.protocol.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

public class EchoFirstOutboundHandler extends ChannelOutboundHandlerAdapter {
    
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("进入EchoFirstOutbountHandler.write方法");    
        // ctx.channel().writeAndFlush(Unpooled.copiedBuffer("writAndFlush写入操作", CharsetUtil.UTF_8));
        // 如果没有调用flash()方法，那么跟服务端的连接就不会断开
        // *******同时只有调用了write()方法，才会接着去调用后面的ChannelOutboundHandler
        // ----------------------------
        // 不要再OutbountHandler中调用firChannelRead方法，不然会进入死循环
        // ctx.fireChannelRead(msg);
        // ----------------------------
        // System.out.println("OutBoundHandle调用write方法之前");
        // String beforeData = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
        // System.out.println("OutBoundHandle调用write方法之前的msg:" + beforeData);
        ctx.write(msg, promise);
        // 在执行了flush()方法之后不要尝试去使用msg
        // System.out.println("OutBoundHandle调用write方法之后");
        // String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
        // System.out.println("OutBoundHandle调用write方法之后的msg:" + data);
        // OutboundHandle中，在write()后调用fireChannelRead()无效
        // 会报IndexOutOfBoundsException
        System.out.println("退出EchoFirstOutbountHandler.write方法");    
    }
}