package com.akshita.jad.core.server;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;

import org.jboss.modules.ModuleClassLoader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.akshita_sahu.bytekit.utils.ReflectionUtils;
import com.akshita.jad.common.JavaVersionUtils;
import com.akshita.jad.core.bytecode.TestHelper;
import com.akshita.jad.core.config.Configure;
import com.akshita.jad.core.env.JADEnvironment;

import net.bytebuddy.agent.ByteBuddyAgent;

/**
 * 
 * @author hengyunabc 2020-12-02
 *
 */
public class JADBootstrapTest {
    @Before
    public void beforeMethod() {
        // jboss modules need jdk8
        org.junit.Assume.assumeTrue(JavaVersionUtils.isGreaterThanJava7());
    }

    @Test
    public void test() throws Exception {
        Instrumentation instrumentation = ByteBuddyAgent.install();
        TestHelper.appendSpyJar(instrumentation);

        JADBootstrap jadBootstrap = Mockito.mock(JADBootstrap.class);
        Mockito.doCallRealMethod().when(jadBootstrap).enhanceClassLoader();

        Configure configure = Mockito.mock(Configure.class);
        Mockito.when(configure.getEnhanceLoaders())
                .thenReturn("java.lang.ClassLoader,org.jboss.modules.ConcurrentClassLoader");
        Field configureField = JADBootstrap.class.getDeclaredField("configure");
        configureField.setAccessible(true);
        ReflectionUtils.setField(configureField, jadBootstrap, configure);

        Field instrumentationField = JADBootstrap.class.getDeclaredField("instrumentation");
        instrumentationField.setAccessible(true);
        ReflectionUtils.setField(instrumentationField, jadBootstrap, instrumentation);

        org.jboss.modules.ModuleClassLoader moduleClassLoader = Mockito.mock(ModuleClassLoader.class);

        boolean flag = false;
        try {
            moduleClassLoader.loadClass("java.jad.SpyAPI");
        } catch (Exception e) {
            flag = true;
        }
        assertThat(flag).isTrue();

        jadBootstrap.enhanceClassLoader();

        Class<?> loadClass = moduleClassLoader.loadClass("java.jad.SpyAPI");

        System.err.println(loadClass);

    }

    @Test
    public void testConfigLocationNull() throws Exception {
        JADEnvironment jadEnvironment = new JADEnvironment();
        String location = JADBootstrap.reslove(jadEnvironment, JADBootstrap.CONFIG_LOCATION_PROPERTY, null);
        assertThat(location).isEqualTo(null);
    }

    @Test
    public void testConfigLocation() throws Exception {
        JADEnvironment jadEnvironment = new JADEnvironment();

        System.setProperty("hhhh", "fff");
        System.setProperty(JADBootstrap.CONFIG_LOCATION_PROPERTY, "test${hhhh}");

        String location = JADBootstrap.reslove(jadEnvironment, JADBootstrap.CONFIG_LOCATION_PROPERTY, null);
        System.clearProperty("hhhh");
        System.clearProperty(JADBootstrap.CONFIG_LOCATION_PROPERTY);

        assertThat(location).isEqualTo("test" + "fff");
    }

    @Test
    public void testConfigNameDefault() throws Exception {
        JADEnvironment jadEnvironment = new JADEnvironment();

        String configName = JADBootstrap.reslove(jadEnvironment, JADBootstrap.CONFIG_NAME_PROPERTY, "jad");
        assertThat(configName).isEqualTo("jad");
    }

    @Test
    public void testConfigName() throws Exception {
        JADEnvironment jadEnvironment = new JADEnvironment();

        System.setProperty(JADBootstrap.CONFIG_NAME_PROPERTY, "testName");
        String configName = JADBootstrap.reslove(jadEnvironment, JADBootstrap.CONFIG_NAME_PROPERTY, "jad");
        System.clearProperty(JADBootstrap.CONFIG_NAME_PROPERTY);
        assertThat(configName).isEqualTo("testName");
    }
}
