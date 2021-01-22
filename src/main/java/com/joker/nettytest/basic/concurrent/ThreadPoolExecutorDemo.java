package com.joker.nettytest.basic.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo {
    
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    public static void main(String[] args) {
        // 通过ThreadPoolExecutor构造线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 核心线程数，最小可以同时运行的线程数量
            CORE_POOL_SIZE, 
            // 当workQueue中存放的任务达到队列容量时，当前可同时运行的最大线程数量
            MAX_POOL_SIZE, 
            // 当线程池中的线程数量大于corePoolSize时，如果没有新的任务提交，
            // 核心线程数据之外的线程会等待keepAliveTime之后再回收销毁
            KEEP_ALIVE_TIME, 
            // keepAliveTime时间单位
            TimeUnit.SECONDS,
            // workQueue: 放置超过corePoolSize的线程
            new ArrayBlockingQueue<>(QUEUE_CAPACITY), 
            new NameingThreadFactory("joker"),
            // 设置饱和策略为CallerRunsPolicy
            // 饱和策略,当同时运行的线程数量达到最大线程数量且队列也已经饱和时：
            // ThreadPoolExecutor.AbortPolicy: 抛出RejectedExecutionException来拒绝新任务的处理
            // ThreadPoolExecutor.CallerRunsPolicy: 每个线程都会被执行，如果执行程序已关闭，则会丢弃该任务 
            // ThreadPoolExecutor.DiscardPolicy: 不处理新任务，直接丢弃掉
            // ThreadPoolExecutor.DiscardOldestPolicy: 此策略将丢弃最早的未处理的任务请求
            new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; i++) {
            Runnable worker = new MyRunnable("" + i);
            executor.execute(worker);
            // 方法引用
            // executor.execute(worker::run);
            // lambda: 接口必须是函数式接口，即该接口只有一个方法
            // 可以用@FunctionInterface注解标明该接口是个函数式接口，免得他人添加新方法
            // executor.execute(() -> {});
        }

        // 终止线程池
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finished all threads");
    }
}