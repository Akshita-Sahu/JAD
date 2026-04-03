package com.akshita.jad.core.advisor;

import java.jad.SpyAPI;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLClassLoader;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import com.akshita_sahu.bytekit.utils.AsmUtils;
import com.akshita_sahu.bytekit.utils.Decompiler;
import com.akshita_sahu.deps.org.objectweb.asm.Type;
import com.akshita_sahu.deps.org.objectweb.asm.tree.ClassNode;
import com.akshita_sahu.deps.org.objectweb.asm.tree.MethodNode;
import com.akshita.jad.core.bytecode.TestHelper;
import com.akshita.jad.core.server.JADBootstrap;
import com.akshita.jad.core.util.ClassLoaderUtils;
import com.akshita.jad.core.util.matcher.EqualsMatcher;

import demo.MathGame;
import net.bytebuddy.agent.ByteBuddyAgent;

/**
 * 
 * @author hengyunabc 2020-05-19
 *
 */
public class EnhancerTest {

    @Test
    public void test() throws Throwable {
        Instrumentation instrumentation = ByteBuddyAgent.install();

        TestHelper.appendSpyJar(instrumentation);

        JADBootstrap.getInstance(instrumentation, "ip=127.0.0.1");

        AdviceListener listener = Mockito.mock(AdviceListener.class);

        EqualsMatcher<String> methodNameMatcher = new EqualsMatcher<String>("print");
        EqualsMatcher<String> classNameMatcher = new EqualsMatcher<String>(MathGame.class.getName());

        Enhancer enhancer = new Enhancer(listener, true, false, classNameMatcher, null, methodNameMatcher);

        ClassLoader inClassLoader = MathGame.class.getClassLoader();
        String className = MathGame.class.getName();
        Class<?> classBeingRedefined = MathGame.class;

        ClassNode classNode = AsmUtils.loadClass(MathGame.class);

        byte[] classfileBuffer = AsmUtils.toBytes(classNode);

        byte[] result = enhancer.transform(inClassLoader, className, classBeingRedefined, null, classfileBuffer);

        ClassNode resultClassNode1 = AsmUtils.toClassNode(result);

//        FileUtils.writeByteArrayToFile(new File("/tmp/MathGame1.class"), result);

        result = enhancer.transform(inClassLoader, className, classBeingRedefined, null, result);

        ClassNode resultClassNode2 = AsmUtils.toClassNode(result);

//        FileUtils.writeByteArrayToFile(new File("/tmp/MathGame2.class"), result);

        MethodNode resultMethodNode1 = AsmUtils.findMethods(resultClassNode1.methods, "print").get(0);
        MethodNode resultMethodNode2 = AsmUtils.findMethods(resultClassNode2.methods, "print").get(0);

        Assertions
                .assertThat(AsmUtils
                        .findMethodInsnNode(resultMethodNode1, Type.getInternalName(SpyAPI.class), "atEnter").size())
                .isEqualTo(AsmUtils.findMethodInsnNode(resultMethodNode2, Type.getInternalName(SpyAPI.class), "atEnter")
                        .size());

        Assertions.assertThat(AsmUtils
                .findMethodInsnNode(resultMethodNode1, Type.getInternalName(SpyAPI.class), "atExceptionExit").size())
                .isEqualTo(AsmUtils
                        .findMethodInsnNode(resultMethodNode2, Type.getInternalName(SpyAPI.class), "atExceptionExit")
                        .size());

        Assertions.assertThat(AsmUtils
                .findMethodInsnNode(resultMethodNode1, Type.getInternalName(SpyAPI.class), "atBeforeInvoke").size())
                .isEqualTo(AsmUtils
                        .findMethodInsnNode(resultMethodNode2, Type.getInternalName(SpyAPI.class), "atBeforeInvoke")
                        .size());
        Assertions.assertThat(AsmUtils
                .findMethodInsnNode(resultMethodNode1, Type.getInternalName(SpyAPI.class), "atInvokeException").size())
                .isEqualTo(AsmUtils
                        .findMethodInsnNode(resultMethodNode2, Type.getInternalName(SpyAPI.class), "atInvokeException")
                        .size());

        String string = Decompiler.decompile(result);

        System.err.println(string);
    }

    @Test
    public void testEnhanceWithClassLoaderHash() throws Throwable {
        Instrumentation instrumentation = ByteBuddyAgent.install();
        TestHelper.appendSpyJar(instrumentation);
        JADBootstrap.getInstance(instrumentation, "ip=127.0.0.1");

        URL codeSource = MathGame.class.getProtectionDomain().getCodeSource().getLocation();
        URLClassLoader anotherClassLoader = new URLClassLoader(new URL[] { codeSource }, null);
        try {
            Class<?> anotherMathGame = Class.forName(MathGame.class.getName(), true, anotherClassLoader);
            Assertions.assertThat(anotherMathGame.getClassLoader()).isNotSameAs(MathGame.class.getClassLoader());

            AdviceListener listener = Mockito.mock(AdviceListener.class);
            EqualsMatcher<String> methodNameMatcher = new EqualsMatcher<String>("print");
            EqualsMatcher<String> classNameMatcher = new EqualsMatcher<String>(MathGame.class.getName());

            // Enhancer  ClassLoader （ JAD ）。
            //  ClassLoader ， classloader hash 。
            String targetClassLoaderHash = Integer.toHexString(anotherClassLoader.hashCode());
            Enhancer enhancer = new Enhancer(listener, false, false, classNameMatcher, null, methodNameMatcher, false,
                    targetClassLoaderHash);

            com.akshita.jad.core.util.affect.EnhancerAffect affect = enhancer.enhance(instrumentation, 50);

            String expectedMethodPrefix = ClassLoaderUtils.classLoaderHash(anotherClassLoader) + "|"
                    + MathGame.class.getName() + "#print|";
            String nonTargetMethodPrefix = ClassLoaderUtils.classLoaderHash(MathGame.class.getClassLoader()) + "|" + MathGame.class.getName()
                    + "#print|";

            Assertions.assertThat(affect.cCnt()).isEqualTo(1);
            Assertions.assertThat(affect.mCnt()).isEqualTo(1);
            Assertions.assertThat(affect.getMethods()).hasSize(1);
            Assertions.assertThat(affect.getMethods()).allMatch(m -> m.startsWith(expectedMethodPrefix));
            Assertions.assertThat(affect.getMethods()).noneMatch(m -> m.startsWith(nonTargetMethodPrefix));
        } finally {
            anotherClassLoader.close();
        }
    }

}
