package com.akshita.jad.core.command.express;

import java.lang.ref.WeakReference;

/**
 * ExpressFactory
 * @author ralf0131 2017-01-04 14:40.
 * @author hengyunabc 2018-10-08
 */
public class ExpressFactory {

    /**
     *  ThreadLocalMap  Express（ JADClassLoader ）， stop/detach ，
     *  JADClassLoader  GC 。
     *
     *  WeakReference ：Thread -> ThreadLocalMap -> value(WeakReference) -X-> Express。
     */
    private static final ThreadLocal<WeakReference<Express>> expressRef = ThreadLocal
            .withInitial(() -> new WeakReference<Express>(new OgnlExpress()));

    /**
     * get ThreadLocal Express Object
     * @param object
     * @return
     */
    public static Express threadLocalExpress(Object object) {
        WeakReference<Express> reference = expressRef.get();
        Express express = reference == null ? null : reference.get();
        if (express == null) {
            express = new OgnlExpress();
            expressRef.set(new WeakReference<Express>(express));
        }
        return express.reset().bind(object);
    }

    public static Express unpooledExpress(ClassLoader classloader) {
        if (classloader == null) {
            classloader = ClassLoader.getSystemClassLoader();
        }
        return new OgnlExpress(new ClassLoaderClassResolver(classloader));
    }
}
