package com.joker.nettytest.protocol.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoFirstInboundHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        buf = ctx.alloc().buffer(5);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        buf.release();
        buf = null;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg;
        buf.writeBytes(m);
        m.release();
        if (buf.readableBytes() >= 5) {
            System.out.println("进入EchoFirstInboundHandler.channelRead");
            String data = buf.toString(CharsetUtil.UTF_8);
            System.out.println("EchoFirstInboundHandler.channelRead：" + data);
            // ChannelPipeline添加的Handler处理
            // 必须要调用fireChannelRead()方法来让之后注册的ChannelInboundHandler类型的HChannelRead()方法被调用
            // 执行该方法后，会去调用后续的Handler处理
            // ctx.write(buf);
            super.channelRead(ctx, Unpooled.copiedBuffer("From EchoFirstInboundHandler message:" + data, CharsetUtil.UTF_8));
            // ctx.fireChannelRead(Unpooled.copiedBuffer("From EchoFirstInboundHandler message:" + data, CharsetUtil.UTF_8));
            // ctx.write(buf);
            // 如果执行了write操作，则会直接在该InboundHandler后面去调用OutboundHandler
            // 知道所有OutboundHandler执行完毕后，才会去接着调用后续的InboundHandler
            // ctx.writeAndFlush(buf);
            // 后续代码的处理是等待当前Handler后面的Handler处理完毕后才会去执行
            System.out.println("First Handler开始进行后续处理：芜湖起飞");
            System.out.println("退出EchoFirstInboundHandler.channelRead");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("进入First Inbound Handler method: channelReadComplete");
        // 通过调用fire开头的相应的方法来进入到下一个相应的Handler处理的方法中
        ctx.fireChannelReadComplete();
        System.out.println("退出First Inbound Handler method: channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}