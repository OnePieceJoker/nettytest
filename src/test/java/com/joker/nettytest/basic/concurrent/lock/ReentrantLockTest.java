package com.joker.nettytest.basic.concurrent.lock;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class ReentrantLockTest {
    private final ReentrantLock lock = new ReentrantLock();
    private int i = 0;

    @Test
    public void testReentrantLock() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int j = 0; j < 10000000; j++) {
                try {
                    lock.lockInterruptibly();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                try {
                    i++;
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int j = 0; j < 10000000; j++) {
                lock.lock();
                try {
                    i++;
                } finally {
                    lock.unlock();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(i);
    }

}