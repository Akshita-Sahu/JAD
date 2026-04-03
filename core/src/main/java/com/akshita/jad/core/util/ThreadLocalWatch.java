package com.akshita.jad.core.util;

/**
 * 。
 * 
 * @author vlinux 16/6/1.
 * @author hengyunabc 2016-10-31
 */
public class ThreadLocalWatch {

    /**
     *  long[]  ring stack， JADClassLoader  ThreadLocalMap ，
     *  stop/detach  JADClassLoader  GC 。
     *
     * <pre>
     * ：
     * - stack[0]  pos（0..cap）
     * - stack[1..cap] 
     * </pre>
     */
    private static final int DEFAULT_STACK_SIZE = 1024 * 4;
    private final ThreadLocal<long[]> timestampRef = ThreadLocal.withInitial(() -> new long[DEFAULT_STACK_SIZE + 1]);

    public long start() {
        final long timestamp = System.nanoTime();
        push(timestampRef.get(), timestamp);
        return timestamp;
    }

    public long cost() {
        return (System.nanoTime() - pop(timestampRef.get()));
    }

    public double costInMillis() {
        return (System.nanoTime() - pop(timestampRef.get())) / 1000000.0;
    }

    /**
     * 
     * <pre>
     * stack，，。
     * stackpush/pop ，push，，pop。
     * ，，。
     * ，pos，。Stack，monitor/watch。
     * 
     * </pre>
     * 
     * @author hengyunabc 2019-11-20
     *
     */
    static void push(long[] stack, long value) {
        int cap = stack.length - 1;
        int pos = (int) stack[0];
        if (pos < cap) {
            pos++;
        } else {
            // if stack is full, reset pos
            pos = 1;
        }
        stack[pos] = value;
        stack[0] = pos;
    }

    static long pop(long[] stack) {
        int cap = stack.length - 1;
        int pos = (int) stack[0];
        if (pos > 0) {
            long value = stack[pos];
            stack[0] = pos - 1;
            return value;
        }

        pos = cap;
        long value = stack[pos];
        stack[0] = pos - 1;
        return value;
    }
}
