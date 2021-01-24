package com.joker.nettytest.protocol.stomp.websocket;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import io.netty.handler.codec.stomp.StompSubframe;
import io.netty.handler.codec.stomp.StompSubframeAggregator;
import io.netty.handler.codec.stomp.StompSubframeDecoder;

@Sharable
public class StompWebSocketProtocolCodec extends MessageToMessageCodec<WebSocketFrame, StompSubframe> {

    private final StompChatHandler stompChatHandler = new StompChatHandler();
    private final StompWebSocketFrameEncoder stompWebSocketFrameEncoder = new StompWebSocketFrameEncoder();
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            StompVersion stompVersion = StompVersion.findBySubProtocol(((HandshakeComplete) evt).selectedSubprotocol());
            ctx.channel().attr(StompVersion.CHANNEL_ATTRIBUTE_KEY).set(stompVersion);
            ctx.pipeline()
               .addLast(new WebSocketFrameAggregator(65536))
               .addLast(new StompSubframeDecoder())
               .addLast(new StompSubframeAggregator(65536))
               .addLast(stompChatHandler)
               .remove(StompWebSocketClientPageHandler.INSTANCE);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, StompSubframe msg, List<Object> out) throws Exception {
        stompWebSocketFrameEncoder.encode(ctx, msg, out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame, List<Object> out) throws Exception {
        if (webSocketFrame instanceof TextWebSocketFrame) {
            out.add(webSocketFrame.content().retain());
        } else {
            ctx.close();
        }
    }
}
