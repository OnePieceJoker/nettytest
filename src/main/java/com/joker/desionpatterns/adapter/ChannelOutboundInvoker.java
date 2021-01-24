package com.joker.desionpatterns.adapter;

import java.net.SocketAddress;

import io.netty.channel.ChannelFuture;

public interface ChannelOutboundInvoker {
    
    ChannelFuture bind(SocketAddress localAddress);
}
