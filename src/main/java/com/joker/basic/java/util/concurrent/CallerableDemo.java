package com.joker.basic.java.util.concurrent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallerableDemo {
    
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            CORE_POOL_SIZE, 
            MAX_POOL_SIZE, 
            KEEP_ALIVE_TIME, 
            TimeUnit.SECONDS, 
            new ArrayBlockingQueue<>(QUEUE_CAPACITY),
            new ThreadPoolExecutor.CallerRunsPolicy());

        List<Future<String>> futures = new ArrayList<>();
        Callable<String> callable = new MyCallerable();
        for (int i = 0; i < 10; i++) {
            Future<String> future =  executor.submit(callable);
            futures.add(future);
        }

        for (Future<String> future : futures) {
            try {
                System.out.println(new Date() + "::" + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finished all threads");
    }
}