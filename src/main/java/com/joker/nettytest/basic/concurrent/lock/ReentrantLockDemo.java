package com.joker.nettytest.basic.concurrent.lock;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 可重入锁(A reentrant mutual exclusion lock)
 * 基本行为和语义与使用了{@code synchronized}的方法相同，但具有扩展性
 * 
 * ReentrantLock类的构造函数提供了一个fairness参数配置。
 * 当设置为true时，意味着该锁为公平锁，即在争用下，锁倾向于访问等待时间最长的线程
 * 否则，该锁无法保证任何特定的访问顺序
 * 
 * 但是，锁的公平性不能保证线程调用的公平性。因此，使用了公平锁的许多线程之一可能会连续多次获得它，
 * 而其他活动线程未进行且当前未持有该锁.
 * 
 * 不管之前的状态如何, 反序列化的锁都会处于解锁状态
 * 
 * 使用例子 {@code 
 * class X {
 *     private final ReentrantLock lock = new ReentrantLock();
 * 
 *     public void m() {
 *          lock.lock();
 *          try {
 *           // ...do something
 *         } finally { lock.unlock(); }
 *     }
 * }}
 */
public class ReentrantLockDemo implements Lock, Serializable {

    private static final long serialVersionUID = 7000253326300674170L;
    // 重要：提供了同步机制
    private final Sync sync;

    /**
     * 使用AQS状态表示锁的保留数
     */
    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 6159743040376112749L;

        /**
         * 执行{@link Lock#lock}, 子类化的原因是为非公平锁提供快速路径
         */
        abstract void lock();

        /**
         * 执行非公平的tryLock, tryAcquire()方法在子类实现，但是需要调用该方法
         * @param acquires
         * @return
         */
        final boolean nonfairTryAcquire(int acquires) {
            // 获取当前线程
            final Thread current = Thread.currentThread();
            // 返回同步状态的当前值
            int c = getState();
            if (c == 0) {
                // 原子性操作：如果当前值是等于0（期望值）, 则更新值为设定值（acquires）
                if (compareAndSetState(0, acquires)) {
                    // 当前线程独占该锁
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) { // 当前线程已拥有该锁
                // 可重入，如果要解锁的话，也要一次次释放该锁
                int nextc = c + acquires;
                if (nextc < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                // 设置状态
                setState(nextc);
                return true;
            }
            return false;
        }

        protected final boolean tryRelease(int release) {
            // 当前状态数减去要释放的数
            int c = getState() - release;
            // 判断当前线程是否是独占线程
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            boolean free = false;
            if (c == 0) { // 该独占线程的状态全部释放完毕
                free = true;
                setExclusiveOwnerThread(null);
            }
            // 不然，就减少release数目（可重入特性）
            setState(c);
            return free;
        }

        protected final boolean isHeldExclusively() {
            // 当前线程是否是独占线程
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        // TODO 阅读AQS源码时再处理
        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        final Thread getOwner() {
            // 获取独占锁
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        final boolean isLocked() {
            return getState() != 0;
        }

        /**
         * 从流中重构实例，即反序列化
         * @param s
         * @throws IOException
         * @throws ClassNotFoundException
         */
        private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // 反序列化时，直接设置状态为0，即解锁
        }
        
    }

    // 非公平锁
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = -2712342496123544167L;

        final void lock() {
            if (compareAndSetState(0, 1)) { // 如果当前状态为解锁状态，则给当前线程加锁
                setExclusiveOwnerThread(Thread.currentThread());
            } else {
                // acquire()方法会调用tryAcquire(int acquires)方法 
                // 不停尝试去获取该锁，忽略中断
                acquire(1);
            }
        }

        /**
         * acquire方法会调用tryAcquire()方法尝试去获取锁
         */
        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    // 公平锁
    static final class FairSync extends Sync {
        private static final long serialVersionUID = 9054641042084666176L;

        final void lock() {
            acquire(1);
        }

        protected final boolean tryArquire(int acquires) {
            // 获取当前线程
            final Thread current = Thread.currentThread();
            // 获取当前保留数
            int c = getState();
            if (c == 0) {
                // 注意hasQueuedPredecessors()方法，公平锁要先判断当前线程是否在队列的最前面，越前代表等待时间越长
                if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) { // 下面的判断逻辑与非公平锁一致
                int nextc = c + acquires;
                if (nextc < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc);
                return true;
            }

            return false;
        }
    }

    public ReentrantLockDemo() {
        // 默认构造函数实现非公平锁
        sync = new NonfairSync();
    }

    public ReentrantLockDemo(boolean fair) {
        // 通过参数来构造公平锁或非公平锁
        sync = fair ? new FairSync() : new NonfairSync();
    }

    /**
     * 为当前线程获取锁
     * 如果锁没有被其他线程持有，则当前线程获得该锁
     * 如果当前线程已有锁，则累加（可重入）
     * 如果锁被其他线程占有，则当前线程休眠，一直尝试去获取该锁
     */
    @Override
    public void lock() {
        sync.lock();
    }

    /**
     * 除非当前线程被中断，否则获取锁
     * 
     * 如果当前线程：在进入此方法时设置了中断状态；或在获取锁的过程中被设置了中断，
     * 则会引发{@link InterruptedException}并清除当前线程的中断状态
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /**
     * 注意：这边获取锁的机制是非公平的
     * 如果调用{@code tryLock()}，无论当前是公平锁还是非公平锁，都会直接去尝试获取锁，
     * 如果锁没有被占有，则直接获取锁，不管当前是否有其他线程在等待
     * 如果锁被其他线程占有，则直接返回false
     * 
     * 虽然该方法是破坏公平性，但是在某些情况下还是很有用的
     * 如果要遵守公平性设置，则可以调用{@link #tryLock(long, TimeUnit) tryLock(0, TimeUnit.SECONDS)}方法,该方法还可以检测中断
     */
    @Override
    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }

    /**
     * 如果在给定的等待时间内另一个线程未持有该锁，并且当前线程没有中断，则获取该锁
     * 该方法会检测线程中断，通过通过调用${code tryAcquire(int)}方式来尊重当前锁的公平性设置
     * 如果想要一个定时的tryLock允许在公平锁上插入，可以将定时和非定时设置在一起
     * 
     * {@code 
     * if (lock.tryLock() || lock.tryLock(timeout, unit)) {}
     * }
     * 
     * 如果当前线程：在进入此方法时设置了中断状态；或在获取锁的过程中被设置了中断，
     * 则会引发{@link InterruptedException}并清除当前线程的中断状态
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        // 尝试释放此锁
        sync.tryRelease(1);
    }

    @Override
    public Condition newCondition() {
        // TODO
        return sync.newCondition();
    }

    public int getHoldCount() {
        // 如果当前线程是独占的，则返回当前保留数，否则返回0
        return sync.getHoldCount();
    }

    public boolean isHeldByCunnentThread() {
        // 判断当前线程是否是独占
        return sync.isHeldExclusively();
    }

    public boolean isLocked() {
        // 如果保留数不为0，代表锁定
        return sync.isLocked();
    }

    public final boolean isFair() {
        // 当前锁是否是公平锁机制
        return sync instanceof FairSync;
    }

    protected Thread getOwner() {
        // 如果当前线程占有锁时，则返回当前线程，否则，返回null
        return sync.getOwner();
    }

    public final boolean hasQueuedThreads() {
        // TODO
        return sync.hasQueuedThreads();
    }

    public final boolean hasQueuedThreads(Thread thread) {
        // TODO
        return sync.isQueued(thread);
    }

    public int getQueueLenght() {
        // TODO
        return sync.getQueueLength();
    }

    protected Collection<Thread> getQueuedThreads() {
        // TODO
        return sync.getQueuedThreads();
    }

    // TODO
    public boolean hasWaiters(Condition condition) {
        if (condition == null) {
            throw new NullPointerException();
        }
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject)) {
            throw new IllegalArgumentException("not owner");
        }
        return sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

    // TODO
    public int getWaitQueueLength(Condition condition) {
        if (condition == null) {
            throw new NullPointerException();
        }
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject)) {
            throw new IllegalArgumentException("not owner");
        }
        return sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

    // TODO
    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null) {
            throw new NullPointerException();
        }
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject)) {
            throw new IllegalArgumentException("not owner");
        }
        return sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

    public String toString() {
        Thread o = sync.getOwner();
        return super.toString() + ((o == null) ?
                                   "[Unlocked]" :
                                   "[Locked by thread " + o.getName() + "]");
    }
    
}