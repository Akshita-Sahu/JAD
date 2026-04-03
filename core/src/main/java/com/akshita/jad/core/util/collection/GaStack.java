package com.akshita.jad.core.util.collection;

/**
 * 
 * Created by vlinux on 15/6/21.
 * @param <E>
 */
public interface GaStack<E> {

    E pop();

    void push(E e);

    E peek();

    boolean isEmpty();

}
