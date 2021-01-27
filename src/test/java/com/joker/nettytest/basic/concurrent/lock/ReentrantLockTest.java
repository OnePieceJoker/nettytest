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

    public static void main(String[] args) {
        // 通过synchronized来实现同步
        Student1 student1 = new Student1();
        Student2 student2 = new Student2();
        Teacher teacher = new Teacher();

        Thread t = new Thread(() -> {
            for (int i = 0; i < 4; i++) {
                System.out.println("上课");
            }
            teacher.isTeacherFlag();
            System.out.println("学生1被吵醒了，1s后反应过来");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            student1.setStudent1Flag(true);
        });

        Thread s1 = new Thread(() -> {
            student1.isStudent1Flag();
            System.out.println("准备唤醒学生2，需要1s");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            student2.setStudent2Flag(true);
        });

        Thread s2 = new Thread(() -> {
            student2.isStudent2Flag();
        });

        s1.start();
        s2.start();
        t.start();
    }

    static class Student1 {
        private boolean student1Flag = false;

        public synchronized boolean isStudent1Flag() {
            System.out.println("学生1准备睡觉1min");
            if (!this.student1Flag) {
                try {
                    System.out.println("学生1睡着了");
                    wait(1*1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("学生1被唤醒");
            return student1Flag;
        }

        public synchronized void setStudent1Flag(boolean student1Flag) {
            this.student1Flag = student1Flag;
            notify();
        }
    }
    static class Student2 {
        private boolean student2Flag = false;

        public synchronized boolean isStudent2Flag() {
            System.out.println("学生2准备睡觉5min");
            if (!this.student2Flag) {
                try {
                    System.out.println("学生2睡着了");
                    wait(5*1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("学生2被唤醒");
            return student2Flag;
        }

        public synchronized void setStudent2Flag(boolean student2Flag) {
            notify();
            this.student2Flag = student2Flag;
        }
    }

    static class Teacher {
        private boolean teacherFlag = true;

        public synchronized boolean isTeacherFlag() {
            if (!this.teacherFlag) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("老师准备要上课");
            return teacherFlag;
        }

        public synchronized void setTeacherFlag(boolean teacherFlag) {
            this.teacherFlag = teacherFlag;
            notify();
        }
    }

}