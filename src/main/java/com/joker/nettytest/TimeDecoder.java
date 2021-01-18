package com.joker.nettytest;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

public class TimeDecoder extends ByteToMessageDecoder {
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        out.add(in.readBytes(4));
    }
}

// 也可以实现ReplayingDecoder来进一步简化解码器
// public class TimeDecoder extends ReplayingDecoder<Void> {
//     @Override
//     protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//         out.add(in.readBytes(4));
//     }
// }