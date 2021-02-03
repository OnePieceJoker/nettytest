package com.joker.basic.java.util.concurrent.lock;

import java.io.Serializable;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;

/**
 * AQS，提供了一个框架，用于实现依赖于先进先出（FIFO）等待队列的阻塞锁和相关的同步器（semaphores,events）
 * 该类旨在为大多数依赖单个原子int值表示状态的同步器提供有用的基础
 * 
 * 子类应定义为用于实现其所在类的同步属性的非公共内部帮助器类
 * 必须定义更改此状态的受保护的方法，定义该状态对于获取（acquired）或释放（released）此对象而言意味着什么
 * 
 * 该类支持排他(exclusive mode)模式和共享(shared mode)模式
 */
public abstract class AbstractQueuedSynchronizerDemo extends AbstractOwnableSynchronizer implements Serializable {

	private static final long serialVersionUID = -6507747478745427361L;
    
}