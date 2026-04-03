package jad;

/**
 * VmTool interface for JMX server. How to register VmTool MBean:
 *
 * <pre>
 * {@code
 *     ManagementFactory.getPlatformMBeanServer().registerMBean(
 *             VmTool.getInstance(),
 *             new ObjectName("jad:type=VmTool")
 *     );
 * }
 * </pre>
 * @author hengyunabc 2021-04-26
 */
public interface VmToolMXBean {

    /**
     * https://docs.oracle.com/javase/8/docs/platform/jvmti/jvmti.html#ForceGarbageCollection
     */
    public void forceGc();

    /**
     * 
     *
     * @param threadId ID
     */
    void interruptSpecialThread(int threadId);

    public <T> T[] getInstances(Class<T> klass);

    /**
     * classjvm
     * @param <T>
     * @param klass
     * @param limit  0 ，
     * @return
     */
    public <T> T[] getInstances(Class<T> klass, int limit);

    /**
     * classjvm，：Byte
     */
    public long sumInstanceSize(Class<?> klass);

    /**
     * ，：Byte
     */
    public long getInstanceSize(Object instance);

    /**
     * classjvm
     */
    public long countInstances(Class<?> klass);

    /**
     * 
     */
    public Class<?>[] getAllLoadedClasses();

    /**
     * glibc 
     */
    public int mallocTrim();

    /**
     * glibc  stderr
     */
    public boolean mallocStats();

    /**
     * （ GC Root ）。
     *
     * @param classNum  
     * @param objectNum 
     * @return 
     */
    public String heapAnalyze(int classNum, int objectNum);

    /**
     * ，（ GC Root）。
     *
     * @param klass         
     * @param objectNum     
     * @param backtraceNum  ，-1  root，0 
     * @return 
     */
    public String referenceAnalyze(Class<?> klass, int objectNum, int backtraceNum);
}
