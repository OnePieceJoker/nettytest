package com.joker.nettytest.basic.concurrent;

import java.util.Date;

public class MyRunnable implements Runnable {
    
    private String command;

    public MyRunnable(String s) {
        this.command = s;
    }
    @Override
    public void run() {
       System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
       processCommand();
       System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
    }

    public void processCommand() {
        try {
            System.out.println(toString());
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.command;
    }
}