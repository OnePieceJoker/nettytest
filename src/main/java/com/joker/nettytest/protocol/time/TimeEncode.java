package com.joker.nettytest.protocol.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

// public class TimeEncode extends ChannelOutboundHandlerAdapter {
    
//     @Override
//     public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//         UnixTime m = (UnixTime) msg;
//         ByteBuf encoded = ctx.alloc().buffer(4);
//         encoded.writeInt((int) m.value());
//         // 这里做了两件事：
//         // 1. 按原样传递原始的ChannelPromiss，以便当编码数据实际写到线路上时，Netty会将其标记为成功或失败
//         // 2. 这里没有调用ctx.flush()，有一个单独的处理方法void flush(ChannelHandlerContext ctx)用于覆盖flush()操作.
//         ctx.write(encoded, promise);
//     }
// }

// 进一步简化代码的话，可以使用MessageToByteEncoder
public class TimeEncode extends MessageToByteEncoder<UnixTime> {
    @Override
    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) throws Exception {
        out.writeInt((int) msg.value());
    }
}