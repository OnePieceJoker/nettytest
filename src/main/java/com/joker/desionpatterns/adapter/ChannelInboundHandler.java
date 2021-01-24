package com.joker.desionpatterns.adapter;

/**
 * 该类将ChannelHandler类的职责进行了分离，让继承的类专注于处理inbound
 * 该类是ChannelHandler类的一个扩展类，用于处理inbound业务
 */
public interface ChannelInboundHandler extends ChannelHandler {
    
    void channelRegistered(ChannelHandlerContext ctx) throws Exception;

    void channelUnregistered(ChannelHandlerContext ctx) throws Exception;

    void channelActive(ChannelHandlerContext ctx) throws Exception;

    void channelInactive(ChannelHandlerContext ctx) throws Exception;

    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;

    void channelReadComplete(ChannelHandlerContext ctx) throws Exception;

    void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception;

    void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception;
    
    /**
     * @SuppressWarnings:抑制编辑器的警告
     */
    @Override
    @SuppressWarnings("deprecation")
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;

}
