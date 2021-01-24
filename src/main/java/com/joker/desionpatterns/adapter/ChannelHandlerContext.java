package com.joker.desionpatterns.adapter;

public interface ChannelHandlerContext extends ChannelInboundInvoker, ChannelOutboundInvoker {

    @Override
    ChannelInboundInvoker fireChannelRegistered();
    
}
