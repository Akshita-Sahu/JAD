package com.akshita.jad.common;

/**
 * 
 * @author hengyunabc 2021-04-27
 *
 */
public class VmToolUtils {
    private static String libName = null;
    static {
        if (OSUtils.isMac()) {
            libName = "libJADJniLibrary.dylib";
        }
        if (OSUtils.isLinux()) {
            if (OSUtils.isArm32()) {
                libName = "libJADJniLibrary-arm.so";
            } else if (OSUtils.isArm64()) {
                libName = "libJADJniLibrary-aarch64.so";
            } else if (OSUtils.isX86_64()) {
                libName = "libJADJniLibrary-x64.so";
            } else if (OSUtils.isLoongArch64()) {
                libName = "libJADJniLibrary-loongarch64.so";
            }else {
                libName = "libJADJniLibrary-" + OSUtils.arch() + ".so";
            }
        }
        if (OSUtils.isWindows()) {
            libName = "libJADJniLibrary-x64.dll";
            if (OSUtils.isX86()) {
                libName = "libJADJniLibrary-x86.dll";
            }
        }
    }

    public static String detectLibName() {
        return libName;
    }
}
