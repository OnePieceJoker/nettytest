package com.joker.desionpatterns.adapter;

import java.util.Map;

import io.netty.util.internal.InternalThreadLocalMap;

/**
 * 该类用于做ChannelHandler的适配器类
 * ChannelHandler是遗留下来的很重要的类，然后因为设计的变动，需要调整，
 * 这边使用了适配模式来实现该类的扩展
 * 适配了ChannelHandler中可共有的方法:handlerAdded、handlerRemoved,以及判断是否使用了@Sharable注解
 */
public abstract class ChannelHandlerAdapter implements ChannelHandler {

    boolean added;

    protected void ensureNotSharble() {
        // 判断
        if (isSharable()) {
            throw new IllegalStateException("");
        }
    }

    public boolean isSharable() {
        /**
         * 判断是否有使用@Sharable注解，同时会进行缓存
         */
        Class<?> clazz = getClass();
        Map<Class<?>, Boolean> cache = InternalThreadLocalMap.get().handlerSharableCache();
        Boolean sharable = cache.get(clazz);
        if (sharable == null) {
            sharable = clazz.isAnnotationPresent(Sharable.class);
            cache.put(clazz, sharable);
        }
        return sharable;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // do something
        // 由子类去实现
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        // do something
        // 由子类去实现
    }
    
    /**
     * netty这边给该方法加了一个@Skip注解，该注解会跳过该方法,用来保证职责分离
     * @Skip注解不会继承，也就是说子类可以通过覆盖该方法以达成调用该方法的目的
     */
    // @Skip
    @Override
    @Deprecated
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO Auto-generated method stub
        
    }
    
}
