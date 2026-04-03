package com.akshita.jad.core.env;

import java.util.Properties;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * 
 * @author hengyunabc 2019-12-27
 *
 */
public class JADEnvironmentTest {

    @Test
    public void test() {
        JADEnvironment jadEnvironment = new JADEnvironment();

        Assertions.assertThat(jadEnvironment.resolvePlaceholders("hello, ${java.version}"))
                .isEqualTo("hello, " + System.getProperty("java.version"));

        Assertions.assertThat(jadEnvironment.resolvePlaceholders("hello, ${xxxxxxxxxxxxxxx}"))
                .isEqualTo("hello, ${xxxxxxxxxxxxxxx}");

        System.setProperty("xxxxxxxxxxxxxxx", "vvv");

        Assertions.assertThat(jadEnvironment.resolvePlaceholders("hello, ${xxxxxxxxxxxxxxx}"))
                .isEqualTo("hello, vvv");

        System.clearProperty("xxxxxxxxxxxxxxx");
    }

    @Test
    public void test_properties() {
        JADEnvironment jadEnvironment = new JADEnvironment();

        Properties properties1 = new Properties();
        Properties properties2 = new Properties();
        jadEnvironment.addLast(new PropertiesPropertySource("test1", properties1));
        jadEnvironment.addLast(new PropertiesPropertySource("test2", properties2));

        properties2.put("test.key", "2222");

        Assertions.assertThat(jadEnvironment.resolvePlaceholders("hello, ${test.key}")).isEqualTo("hello, 2222");

        properties1.put("java.version", "test");
        properties1.put("test.key", "test");

        Assertions.assertThat(jadEnvironment.resolvePlaceholders("hello, ${java.version}"))
                .isEqualTo("hello, " + System.getProperty("java.version"));

        Assertions.assertThat(jadEnvironment.resolvePlaceholders("hello, ${test.key}")).isEqualTo("hello, test");
    }
}
