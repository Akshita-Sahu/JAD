package com.akshita.jad.core;

import java.lang.reflect.Field;

import com.akshita.jad.common.JADConstants;
import com.akshita.jad.common.JavaVersionUtils;
import com.akshita.jad.common.UnsafeUtils;

import ognl.OgnlRuntime;

/**
 * 
 * Created by vlinux on 15/6/4.
 */
public class GlobalOptions {
    public static final String STRICT_MESSAGE = "By default, strict mode is true, "
            + "not allowed to set object properties. "
            + "Want to set object properties, execute `options strict false`";

    /**
     * <br/>
     * JVM，<br/>
     * ，，
     */
    @Option(level = 0,
            name = "unsafe",
            summary = "Option to support system-level class",
            description  =
                    "This option enables to proxy functionality of JVM classes."
                            +  " Due to serious security risk a JVM crash is possibly be introduced."
                            +  " Do not activate it unless you are able to manage."
    )
    public static volatile boolean isUnsafe = false;

    /**
     * dump<br/>
     * ，dump，
     */
    @Option(level = 1,
            name = "dump",
            summary = "Option to dump the enhanced classes",
            description =
                    "This option enables the enhanced classes to be dumped to external file " +
                            "for further de-compilation and analysis."
    )
    public static volatile boolean isDump = false;

    /**
     * <br/>
     * ，
     */
    @Option(level = 1,
            name = "batch-re-transform",
            summary = "Option to support batch reTransform Class",
            description = "This options enables to reTransform classes with batch mode."
    )
    public static volatile boolean isBatchReTransform = true;

    /**
     * json<br/>
     * ，json，-x
     */
    @Option(level = 2,
            name = "json-format",
            summary = "Option to support JSON format of object output",
            description = "This option enables to format object output with JSON when -x option selected."
    )
    public static volatile boolean isUsingJson = false;

    /**
     * ObjectView （）
     */
    @Option(level = 1,
            name = "object-size-limit",
            summary = "Option to control ObjectView output size limit",
            description = "Upper size limit in bytes for ObjectView output, must be greater than 0. Default value is 10 * 1024 * 1024."
    )
    public static volatile int objectSizeLimit = JADConstants.MAX_HTTP_CONTENT_LENGTH;

    /**
     * 
     */
    @Option(
            level = 1,
            name = "disable-sub-class",
            summary = "Option to control include sub class when class matching",
            description = "This option disable to include sub class when matching class."
    )
    public static volatile boolean isDisableSubClass = false;

    /**
     * interface
     * https://github.com/akshita_sahu/jad/issues/1105
     */
    @Option(
            level = 1,
            name = "support-default-method",
            summary = "Option to control include default method in interface when class matching",
            description = "This option disable to include default method in interface when matching class."
    )
    public static volatile boolean isSupportDefaultMethod = JavaVersionUtils.isGreaterThanJava7();

    /**
     * 
     */
    @Option(level = 1,
            name = "save-result",
            summary = "Option to print command's result to log file",
            description = "This option enables to save each command's result to log file, " +
                    "which path is ${user.home}/logs/jad-cache/result.log."
    )
    public static volatile boolean isSaveResult = false;

    /**
     * job
     */
    @Option(level = 2,
            name = "job-timeout",
            summary = "Option to job timeout",
            description = "This option setting job timeout,The unit can be d, h, m, s for day, hour, minute, second. "
                    + "1d is one day in default"
    )
    public static volatile String jobTimeout = "1d";

    /**
     * parentfield
     * @see com.akshita.jad.core.view.ObjectView
     */
    @Option(level = 1,
            name = "print-parent-fields",
            summary = "Option to print all fileds in parent class",
            description = "This option enables print files in parent class, default value true."
    )
    public static volatile boolean printParentFields = true;

    /**
     * verbose 
     */
    @Option(level = 1,
            name = "verbose",
            summary = "Option to print verbose information",
            description = "This option enables print verbose information, default value false."
    )
    public static volatile boolean verbose = false;

    /**
     * strict 。 ognl 
     * @see ognl.OgnlRuntime#getUseStricterInvocationValue()
     */
    @Option(level = 1,
            name = "strict",
            summary = "Option to strict mode",
            description = STRICT_MESSAGE
    )
    public static volatile boolean strict = true;

    public static void updateOnglStrict(boolean strict) {
        try {
            Field field = OgnlRuntime.class.getDeclaredField("_useStricterInvocation");
            field.setAccessible(true);
            // 
            Object staticFieldBase = UnsafeUtils.UNSAFE.staticFieldBase(field);
            long staticFieldOffset = UnsafeUtils.UNSAFE.staticFieldOffset(field);

            // 
            UnsafeUtils.UNSAFE.putBoolean(staticFieldBase, staticFieldOffset, strict);
        } catch (NoSuchFieldException | SecurityException e) {
            // ignore
        }
    }

}
