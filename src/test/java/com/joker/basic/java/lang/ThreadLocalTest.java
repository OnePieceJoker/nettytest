package com.joker.basic.java.lang;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ThreadLocalTest {
    
    private List<String> messages = new ArrayList<>(); 

    private static final int HASH_INCREMENT = 0x61c88647;

    public static final ThreadLocal<ThreadLocalTest> holder = 
        ThreadLocal.withInitial(ThreadLocalTest::new);

    public static void add(String message) {
        holder.get().messages.add(message);
    }

    public static List<String> clear() {
        List<String> messages = holder.get().messages;
        holder.remove();

        System.out.println("size: " + holder.get().messages.size());
        return messages;
    }

    public static void main(String[] args) throws InterruptedException {
        // ThreadLocalTest.add("一枝红杏出墙来");
        // System.out.println(holder.get().messages);
        // ThreadLocalTest.clear();
        // Thread t = new Thread(() -> test("abc", false));
        // t.start();
        // t.join();
        // System.out.println("---gc后---");
        // Thread t2 = new Thread(() -> test("def", true));
        // t2.start();
        // t2.join();
        int hashCode = 0;
        for (int i = 0; i < 16; i++) {
            hashCode = i * HASH_INCREMENT + HASH_INCREMENT;
            int bucket = hashCode & 15;
            System.out.println(i + " 在桶中的位置：" + bucket);
        }
    }

    private static void test(String s, boolean isGC) {
        try {
            new ThreadLocal<>().set(s);
            if (isGC) {
                System.gc();
            }
            Thread t = Thread.currentThread();
            Class<? extends Thread> clz = t.getClass();
            Field field = clz.getDeclaredField("threadLocals");
            field.setAccessible(true);
            Object threadLocalMap = field.get(t);
            Class<?> tlmClass = threadLocalMap.getClass();
            Field tableField = tlmClass.getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] arr = (Object[]) tableField.get(threadLocalMap);
            for (Object o : arr) {
                if (o != null) {
                    Class<?> entryClass = o.getClass();
                    Field valueField = entryClass.getDeclaredField("value");
                    Field referenceField = entryClass.getSuperclass().getSuperclass().getDeclaredField("referent");
                    valueField.setAccessible(true);
                    referenceField.setAccessible(true);
                    System.out.println(String.format("弱引用key: %s, 值: %s", referenceField.get(o), valueField.get(o)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}