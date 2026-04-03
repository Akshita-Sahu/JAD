package com.akshita.jad.core.advisor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * <pre>
 * *  ClassFileTransformer
 * *  Enhancer ，
 * </pre>
 * 
 * @see com.akshita.jad.core.advisor.Enhancer
 * @author hengyunabc 2020-05-18
 *
 */
public class TransformerManager {

    private Instrumentation instrumentation;
    private List<ClassFileTransformer> watchTransformers = new CopyOnWriteArrayList<ClassFileTransformer>();
    private List<ClassFileTransformer> traceTransformers = new CopyOnWriteArrayList<ClassFileTransformer>();
    
    /**
     *  watch/trace Transformer TODO  order ？
     */
    private List<ClassFileTransformer> reTransformers = new CopyOnWriteArrayList<ClassFileTransformer>();

    /**
     *  Transformer，
     *  transformer  addTransformer(transformer, false) 
     */
    private List<ClassFileTransformer> lazyTransformers = new CopyOnWriteArrayList<ClassFileTransformer>();

    private ClassFileTransformer classFileTransformer;
    
    /**
     *  transformer（ retransform-capable）
     *  transformer 
     */
    private ClassFileTransformer lazyClassFileTransformer;

    public TransformerManager(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;

        classFileTransformer = new ClassFileTransformer() {

            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                for (ClassFileTransformer classFileTransformer : reTransformers) {
                    byte[] transformResult = classFileTransformer.transform(loader, className, classBeingRedefined,
                            protectionDomain, classfileBuffer);
                    if (transformResult != null) {
                        classfileBuffer = transformResult;
                    }
                }

                for (ClassFileTransformer classFileTransformer : watchTransformers) {
                    byte[] transformResult = classFileTransformer.transform(loader, className, classBeingRedefined,
                            protectionDomain, classfileBuffer);
                    if (transformResult != null) {
                        classfileBuffer = transformResult;
                    }
                }

                for (ClassFileTransformer classFileTransformer : traceTransformers) {
                    byte[] transformResult = classFileTransformer.transform(loader, className, classBeingRedefined,
                            protectionDomain, classfileBuffer);
                    if (transformResult != null) {
                        classfileBuffer = transformResult;
                    }
                }

                return classfileBuffer;
            }

        };
        instrumentation.addTransformer(classFileTransformer, true);
        
        //  transformer，
        // ： addTransformer(transformer, false) 
        lazyClassFileTransformer = new ClassFileTransformer() {

            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                // （classBeingRedefined == null）
                if (classBeingRedefined != null) {
                    return null;
                }
                
                for (ClassFileTransformer transformer : lazyTransformers) {
                    byte[] transformResult = transformer.transform(loader, className, classBeingRedefined,
                            protectionDomain, classfileBuffer);
                    if (transformResult != null) {
                        classfileBuffer = transformResult;
                    }
                }

                return classfileBuffer;
            }

        };
        //  false ，
        instrumentation.addTransformer(lazyClassFileTransformer, false);
    }

    public void addTransformer(ClassFileTransformer transformer, boolean isTracing) {
        if (isTracing) {
            traceTransformers.add(transformer);
        } else {
            watchTransformers.add(transformer);
        }
    }
    
    /**
     *  transformer，
     */
    public void addLazyTransformer(ClassFileTransformer transformer) {
        lazyTransformers.add(transformer);
    }

    public void addRetransformer(ClassFileTransformer transformer) {
        reTransformers.add(transformer);
    }

    public void removeTransformer(ClassFileTransformer transformer) {
        reTransformers.remove(transformer);
        watchTransformers.remove(transformer);
        traceTransformers.remove(transformer);
        lazyTransformers.remove(transformer);
    }

    public void destroy() {
        reTransformers.clear();
        watchTransformers.clear();
        traceTransformers.clear();
        lazyTransformers.clear();
        instrumentation.removeTransformer(classFileTransformer);
        instrumentation.removeTransformer(lazyClassFileTransformer);
    }

}
