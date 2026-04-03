package com.akshita.jad.core.server.instrument;

import com.akshita_sahu.bytekit.agent.inst.Instrument;
import com.akshita_sahu.bytekit.agent.inst.InstrumentApi;

/**
 * @see java.lang.ClassLoader#loadClass(String)
 * @author hengyunabc 2020-11-30
 *
 */
@Instrument(Class = "java.lang.ClassLoader")
public abstract class ClassLoader_Instrument {
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.startsWith("java.jad.")) {
            ClassLoader extClassLoader = ClassLoader.getSystemClassLoader().getParent();
            if (extClassLoader != null) {
                return extClassLoader.loadClass(name);
            }
        }

        Class clazz = InstrumentApi.invokeOrigin();
        return clazz;
    }
}
