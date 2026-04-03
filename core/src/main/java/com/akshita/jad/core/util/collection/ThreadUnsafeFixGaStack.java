package com.akshita.jad.core.util.collection;

import java.util.NoSuchElementException;

/**
 * <br/>
 * JDK10.
 * Created by vlinux on 15/6/21.
 * @param <E>
 */
public class ThreadUnsafeFixGaStack<E> implements GaStack<E> {

    private final static int EMPTY_INDEX = -1;
    private final Object[] elementArray;
    private final int max;
    private int current = EMPTY_INDEX;

    public ThreadUnsafeFixGaStack(int max) {
        this.max = max;
        this.elementArray = new Object[max];
    }

    private void checkForPush() {
        // stack is full
        if (current == max) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private void checkForPopOrPeek() {
        // stack is empty
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public E pop() {
        checkForPopOrPeek();
        return (E) elementArray[current--];
    }

    @Override
    public void push(E e) {
        checkForPush();
        elementArray[++current] = e;
    }

    @Override
    public E peek() {
        checkForPopOrPeek();
        return (E) elementArray[current];
    }

    @Override
    public boolean isEmpty() {
        return current == EMPTY_INDEX;
    }

}
