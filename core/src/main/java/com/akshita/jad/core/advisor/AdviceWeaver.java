package com.akshita.jad.core.advisor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;

/**
 * <br/>
 * <p/>
 * <h2></h2>
 * :(threadFrameStack),(frameStack)
 * <p/>
 * Created by vlinux on 15/5/17.
 */
public class AdviceWeaver {

    private static final Logger logger = LoggerFactory.getLogger(AdviceWeaver.class);

    // 
    private final static Map<Long/*ADVICE_ID*/, AdviceListener> advices
            = new ConcurrentHashMap<Long, AdviceListener>();

    /**
     * 
     *
     * @param listener 
     */
    public static void reg(AdviceListener listener) {

        // 
        listener.create();

        // 
        advices.put(listener.id(), listener);
    }

    /**
     * 
     *
     * @param listener 
     */
    public static void unReg(AdviceListener listener) {
        if (null != listener) {
            // 
            advices.remove(listener.id());

            // 
            listener.destroy();
        }
    }

    public static AdviceListener listener(long id) {
        return advices.get(id);
    }

    /**
     * 
     *
     * @param listener 
     */
    public static void resume(AdviceListener listener) {
        // 
        advices.put(listener.id(), listener);
    }

    /**
     * 
     *
     * @param adviceId ID
     */
    public static AdviceListener suspend(long adviceId) {
        // 
        return advices.remove(adviceId);
    }

}
