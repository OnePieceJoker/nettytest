package com.joker.nettytest.protocol.time;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    
    /**
     * channelActive()方法在建立连接并准备产生流量时就会被调用
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        // 要发送新消息时，需要分配一个包含消息的新缓冲区
        // 通过ChannelHandlerContext.alloc()获取当前的ByteBufAllocator
        // final ByteBuf time = ctx.alloc().buffer(4);
        // time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        // 什么是java.nio.ByteBuffer.flip()?
        // 这里为什么没有了java.nio.ByteBuffer.flip()?
        // ByteBuf有两个指针：一个用于读取操作，一个用于写入操作
        // 当我们将某些内容写入ByteBuf时，写入者索引增加，而读取器索引不变
        // The reader index and the write index分别表示消息的开始和结束位置
        // 相反，NIO缓冲区没有提供一种clean way来确定消息内容的开始和结束位置
        // 必须要调用flip()方法。如果忘记翻转缓冲区时会遇到麻烦：不会发送任何内容或发送不正确的内容
        // 需要注意的一点是ChannelHandlerContext.write()(and writeAndFlush())方法返回的是ChannelFuture.
        // ChannelFuture表示an I/O operation which has not yet occurred.
        // 这意味着操作可能尚未执行任何请求，因为Netty中的所有操作都是异步的(asynchronous).
        // e.g. 以下代码可能在发送消息之前就关闭连接：
        // ------------------------
        //     Channel ch = ...;
        //     ch.writeAndFlush(message);
        //     ch.close();
        // ------------------------
        // 因此，我们应该在ChannelFuture.write()方法完成后再调用close()方法
        // Pipeline中ChannelHandler的执行顺序
        // final ChannelFuture f = ctx.channel().writeAndFlush(new UnixTime());
        final ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        // 如何得知一个写请求完成了呢？
        // 我们可以添加一个listener来监听ChannelFuture的操作
        // 我们也可以使用预定义的侦听器简化代码
        f.addListener(ChannelFutureListener.CLOSE);
        // f.addListener(new ChannelFutureListener(){
        
        //     @Override
        //     public void operationComplete(ChannelFuture future) throws Exception {
        //         assert f == future;
        //         ctx.close();
        //     }
        // });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}