package com.joker.nettytest.basic.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NameingThreadFactory implements ThreadFactory {
    
    private final AtomicInteger threadNum = new AtomicInteger();
    private final ThreadGroup group;
    private String name;

    public NameingThreadFactory(String name) {
        this.name = name;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, name + " [#" + threadNum.incrementAndGet() + "]", 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        return t;
    }
}