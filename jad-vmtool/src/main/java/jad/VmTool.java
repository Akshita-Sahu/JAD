package jad;

import java.util.Map;

/**
 * @author ZhangZiCheng 2021-02-12
 * @author hengyunabc 2021-04-26
 * @since 3.5.1
 */
public class VmTool implements VmToolMXBean {

    /**
     * jni-lib
     */
    public final static String JNI_LIBRARY_NAME = "JADJniLibrary";

    private static VmTool instance;

    private VmTool() {
    }

    public static VmTool getInstance() {
        return getInstance(null);
    }

    public static synchronized VmTool getInstance(String libPath) {
        if (instance != null) {
            return instance;
        }

        if (libPath == null) {
            System.loadLibrary(JNI_LIBRARY_NAME);
        } else {
            System.load(libPath);
        }

        instance = new VmTool();
        return instance;
    }

    private static synchronized native void forceGc0();

    /**
     * classjvm
     */
    private static synchronized native <T> T[] getInstances0(Class<T> klass, int limit);

    /**
     * classjvm，：Byte
     */
    private static synchronized native long sumInstanceSize0(Class<?> klass);

    /**
     * ，：Byte
     */
    private static native long getInstanceSize0(Object instance);

    /**
     * classjvm
     */
    private static synchronized native long countInstances0(Class<?> klass);

    /**
     * 
     * @param klass  Class.class
     * @return
     */
    private static synchronized native Class<?>[] getAllLoadedClasses0(Class<?> klass);

    /**
     * 。
     */
    private static synchronized native String heapAnalyze0(int classNum, int objectNum);

    /**
     * 。
     */
    private static synchronized native String referenceAnalyze0(Class<?> klass, int objectNum, int backtraceNum);

    @Override
    public void forceGc() {
        forceGc0();
    }

    @Override
    public void interruptSpecialThread(int threadId) {
        Map<Thread, StackTraceElement[]> allThread = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> entry : allThread.entrySet()) {
            if (entry.getKey().getId() == threadId) {
                entry.getKey().interrupt();
                return;
            }
        }
    }

    @Override
    public <T> T[] getInstances(Class<T> klass) {
        return getInstances0(klass, -1);
    }

    @Override
    public <T> T[] getInstances(Class<T> klass, int limit) {
        if (limit == 0) {
            throw new IllegalArgumentException("limit can not be 0");
        }
        return getInstances0(klass, limit);
    }

    @Override
    public long sumInstanceSize(Class<?> klass) {
        return sumInstanceSize0(klass);
    }

    @Override
    public long getInstanceSize(Object instance) {
        return getInstanceSize0(instance);
    }

    @Override
    public long countInstances(Class<?> klass) {
        return countInstances0(klass);
    }

    @Override
    public Class<?>[] getAllLoadedClasses() {
        return getAllLoadedClasses0(Class.class);
    }

    @Override
    public int mallocTrim() {
        return mallocTrim0();
    }

    private static synchronized native int mallocTrim0();

    @Override
    public boolean mallocStats() {
        return mallocStats0();
    }
    private static synchronized native boolean mallocStats0();

    @Override
    public String heapAnalyze(int classNum, int objectNum) {
        return heapAnalyze0(classNum, objectNum);
    }

    @Override
    public String referenceAnalyze(Class<?> klass, int objectNum, int backtraceNum) {
        return referenceAnalyze0(klass, objectNum, backtraceNum);
    }
}
