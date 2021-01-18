package com.joker.nettytest;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buf;

    /**
     * ChannelHandler有两个生命周期侦听器方法: handlerAdded()\handlerRemoved()
     * 可以执行任意（取消）初始化任务，只要保证不会长时间阻塞即可
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        buf.release();
        buf = null;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 基于流的传输是有一定风险的
        // 基于流的传输的缓冲区是以字节队列的形式存储的，而不是数据包队列
        // 这可能导致在传输数据的过程中，数据变得fragmented.
        // The First Solution
        // 创建一个内部积累缓冲区(internal cumulative buffer)
        // 等待所有的数据(本例数据大小为4 bytes)被接收到该缓冲区之后再处理数据

        // ByteBuf m = (ByteBuf) msg;
        // 所有接收到的数据都会累积到buf中
        // buf.writeBytes(m);
        // m.release();

        // 检查buf是否有足够的数据（该示例中是4个字节），然后处理相应的业务逻辑
        // 否则，当有更多数据到达时，Netty会再次调用channelRead()方法，最终将累加所有4个字节
        // if (buf.readableBytes() >= 4) {
        //     long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
        //     System.out.println(new Date(currentTimeMillis));
        //     ctx.close();
        // }

        // The second solution
        ByteBuf m = (ByteBuf) msg;
        try {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        } finally {
            m.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}