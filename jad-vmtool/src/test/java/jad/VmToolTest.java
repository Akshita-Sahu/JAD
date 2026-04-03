package jad;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import com.akshita.jad.common.VmToolUtils;

/**
 * jvm：-Xms128m -Xmx128m
 */
public class VmToolTest {
    private VmTool initVmTool() {
        File path = new File(VmTool.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();

        String libPath = new File(path, VmToolUtils.detectLibName()).getAbsolutePath();
        return VmTool.getInstance(libPath);
    }

    /**
     * macbook
     * allLoadedClasses->1050
     * jad.VmTool@5bb21b69 jad.VmTool@6b9651f3
     * before instances->[jad.VmTool@5bb21b69, jad.VmTool@6b9651f3]
     * size->16
     * count->2
     * sum size->32
     * null null
     * after instances->[]
     */
    @Test
    public void testIsSnapshot() {
        try {
            VmTool vmtool = initVmTool();
            //native，，(int)
            Class<?>[] allLoadedClasses = vmtool.getAllLoadedClasses();
            System.out.println("allLoadedClasses->" + allLoadedClasses.length);

            //，getInstances(Class<T> klass)
            WeakReference<VmToolTest> weakReference1 = new WeakReference<VmToolTest>(new VmToolTest());
            WeakReference<VmToolTest> weakReference2 = new WeakReference<VmToolTest>(new VmToolTest());
            System.out.println(weakReference1.get() + " " + weakReference2.get());
            VmTool[] beforeInstances = vmtool.getInstances(VmTool.class);
            System.out.println("before instances->" + beforeInstances);
            System.out.println("size->" + vmtool.getInstanceSize(weakReference1.get()));
            System.out.println("count->" + vmtool.countInstances(VmTool.class));
            System.out.println("sum size->" + vmtool.sumInstanceSize(VmTool.class));
            beforeInstances = null;

            vmtool.forceGc();
            Thread.sleep(100);
            System.out.println(weakReference1.get() + " " + weakReference2.get());
            VmTool[] afterInstances = vmtool.getInstances(VmTool.class);
            System.out.println("after instances->" + afterInstances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetInstancesMemoryLeak() {
        //20sjprofiler
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        VmTool vmtool = initVmTool();
        final AtomicLong totalTime = new AtomicLong();
        //200000
        for (int i = 1; i <= 2; i++) {
            long start = System.currentTimeMillis();
            WeakReference<Object[]> reference = new WeakReference<Object[]>(vmtool.getInstances(Object.class));
            Object[] instances = reference.get();
            long cost = System.currentTimeMillis() - start;
            totalTime.addAndGet(cost);
            System.out.println(i + " instance size:" + (instances == null ? 0 : instances.length) + ", cost " + cost + "ms avgCost " + totalTime.doubleValue() / i + "ms");
            instances = null;
            vmtool.forceGc();
        }
    }

    @Test
    public void testSumInstancesMemoryLeak() {
        //20sjprofiler
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        VmTool vmtool = initVmTool();
        final AtomicLong totalTime = new AtomicLong();
        //200000
        for (int i = 1; i <= 2; i++) {
            long start = System.currentTimeMillis();
            long sum = vmtool.sumInstanceSize(Object.class);
            long cost = System.currentTimeMillis() - start;
            totalTime.addAndGet(cost);
            System.out.println(i + " sum:" + sum + ", cost " + cost + "ms avgCost " + totalTime.doubleValue() / i + "ms");
        }
    }

    @Test
    public void testCountInstancesMemoryLeak() {
        //20sjprofiler
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        VmTool vmtool = initVmTool();
        final AtomicLong totalTime = new AtomicLong();
        //200000
        for (int i = 1; i <= 2; i++) {
            long start = System.currentTimeMillis();
            long count = vmtool.countInstances(Object.class);
            long cost = System.currentTimeMillis() - start;
            totalTime.addAndGet(cost);
            System.out.println(i + " count:" + count + ", cost " + cost + "ms avgCost " + totalTime.doubleValue() / i + "ms");
        }
    }

    @Test
    public void testGetAllLoadedClassesMemoryLeak() {
        //20sjprofiler
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        VmTool vmtool = initVmTool();
        final AtomicLong totalTime = new AtomicLong();
        //200000
        for (int i = 1; i <= 2; i++) {
            long start = System.currentTimeMillis();
            Class<?>[] allLoadedClasses = vmtool.getAllLoadedClasses();
            long cost = System.currentTimeMillis() - start;
            totalTime.addAndGet(cost);
            System.out.println(i + " class size:" + allLoadedClasses.length + ", cost " + cost + "ms avgCost " + totalTime.doubleValue() / i + "ms");
            allLoadedClasses = null;
        }
    }

    class LimitTest {
    }

    @Test
    public void test_getInstances_lmiit() {
        VmTool vmtool = initVmTool();

        ArrayList<LimitTest> list = new ArrayList<LimitTest>();
        for (int i = 0; i < 10; ++i) {
            list.add(new LimitTest());
        }
        LimitTest[] instances = vmtool.getInstances(LimitTest.class, 5);
        Assertions.assertThat(instances).hasSize(5);
        LimitTest[] instances2 = vmtool.getInstances(LimitTest.class, -1);
        Assertions.assertThat(instances2).hasSize(10);
        LimitTest[] instances3 = vmtool.getInstances(LimitTest.class, 1);
        Assertions.assertThat(instances3).hasSize(1);
    }

    interface III {
    }

    class AAA implements III {
    }

    @Test
    public void test_getInstances_interface() {
        AAA aaa = new AAA();
        VmTool vmtool = initVmTool();
        III[] interfaceInstances = vmtool.getInstances(III.class);
        Assertions.assertThat(interfaceInstances.length).isEqualTo(1);

        AAA[] ObjectInstances = vmtool.getInstances(AAA.class);
        Assertions.assertThat(ObjectInstances.length).isEqualTo(1);

        Assertions.assertThat(interfaceInstances[0]).isEqualTo(ObjectInstances[0]);
    }

    @Test
    public void test_interrupt_thread() throws InterruptedException {
        String threadName = "interruptMe";
        final RuntimeException[] re = new RuntimeException[1];
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    System.out.printf("Thread name is: [%s], thread id is: [%d].\n", Thread.currentThread().getName(),Thread.currentThread().getId());
                    TimeUnit.SECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    re[0] = new RuntimeException("interrupted " + Thread.currentThread().getId() + " thread success.");
                }
            }
        };
        Thread interruptMe = new Thread(runnable,threadName);
        Thread interruptMe1 = new Thread(runnable,threadName);

        interruptMe.start();
        interruptMe1.start();

        VmTool tool = initVmTool();
        tool.interruptSpecialThread((int) interruptMe.getId());
        TimeUnit.SECONDS.sleep(5);
        Assert.assertEquals(("interrupted " + interruptMe.getId() + " thread success."), re[0].getMessage());
    }

    @Test
    public void testMallocTrim() {
        VmTool vmtool = initVmTool();
        vmtool.mallocTrim();
    }

    @Test
    public void testMallocStats() {
        VmTool vmtool = initVmTool();
        vmtool.mallocStats();
    }

    static class ByteHolder {
        byte[] bytes;

        ByteHolder(int sizeMb) {
            this.bytes = new byte[sizeMb * 1024 * 1024];
        }
    }

    @Test
    public void testHeapAnalyze() {
        VmTool vmtool = initVmTool();
        String result = vmtool.heapAnalyze(5, 3);
        Assertions.assertThat(result).contains("class_number:").contains("object_number:");
    }

    @Test
    public void testReferenceAnalyze() {
        //  root(stack local) ，
        ByteHolder bh1 = new ByteHolder(1);
        ByteHolder bh2 = new ByteHolder(2);
        Assertions.assertThat(bh1).isNotNull();
        Assertions.assertThat(bh2).isNotNull();

        VmTool vmtool = initVmTool();
        String result = vmtool.referenceAnalyze(ByteHolder.class, 2, -1);
        Assertions.assertThat(result).contains("ByteHolder").contains("root");
    }
}
