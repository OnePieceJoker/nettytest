package com.joker.basic.java.lang;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class ThreadLocalDemo<T> {
    
    private final int threadLocalHashCode = nextHashCode();

    /**
     * 下一个哈希值，原子更新，从零开始
     */
    private static AtomicInteger nextHashCode = new AtomicInteger();

    private static final int HASH_INCREMENT = 0x61c88647;

    /**
     * 返回下一个Hash值
     * @return
     */
    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    /**
     * 返回当前线程的初始值
     * 该方法只是简单的返回null，如果我们希望它返回非空数据，可以子类化，并重写该方法
     * 一般建议使用匿名内部类来实现
     * @return
     */
    protected T initialValue() {
        return null;
    }

    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal<>(supplier);
    }

    public ThreadLocalDemo() {}

    /**
     * ThreadLocal的扩展，从指定的Supplier中获取初始值
     * @param <T>
     */
    static final class SuppliedThreadLocal<T> extends ThreadLocal<T> {
        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return supplier.get();
        }
    }
}