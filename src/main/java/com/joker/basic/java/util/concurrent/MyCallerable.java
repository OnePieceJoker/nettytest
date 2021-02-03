package com.joker.basic.java.util.concurrent;

import java.util.concurrent.Callable;

public class MyCallerable implements Callable<String> {
    
    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        return Thread.currentThread().getName();
    }
}