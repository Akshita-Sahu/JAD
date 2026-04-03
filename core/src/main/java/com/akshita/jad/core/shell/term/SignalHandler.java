package com.akshita.jad.core.shell.term;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface SignalHandler {
    boolean deliver(int key);
}