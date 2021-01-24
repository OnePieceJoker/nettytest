package com.joker.desionpatterns.adapter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface ChannelHandler {
    
    void handlerAdded(ChannelHandlerContext ctx) throws Exception;

    void handlerRemoved(ChannelHandlerContext ctx) throws Exception;

    /**
     * 当跑出Throwable时，调用该方法进行处理
     * @param ctx
     * @param cause
     * @throws Exception
     * @deprecated 该方法弃用了，应该实现{@link ChannelInboundHandler}的方法
     */
    @Deprecated
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;

    /**
     * 表明该ChannelHandler是否可以被共享
     * 
     * @Inherited: 元注解，代表继承了使用了该注解的父类的子类也会继承该注解
     * 
     * @Documented: 代表该注解由javadoc记录
     * 
     * @Target: 代表该注解可以出现的位置
     * @ELementType.TYPE: 类，接口（包括注解类型）, 或枚举声明
     * 
     * @Retention: 元注解，是注解的注解，
     * RetentionPolicy.RUNTIME代表了注解的信息被保留在class文件中，且当程序编译时,会被虚拟机保留在运行时，即可以通过反射读取到该信息
     * @RetentionPolicy.CLASS代表该注解的信息会被保留在class文件中，但不会被虚拟机读取
     * @RetentionPolicy.SOURCE代表该注解的信息仅保留在源代码中，不会留在classs文件中
     * 
     * @interface: 自定义注解，即@Sharable
     */
    @Inherited
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Sharable {
        // no value
    }
}
