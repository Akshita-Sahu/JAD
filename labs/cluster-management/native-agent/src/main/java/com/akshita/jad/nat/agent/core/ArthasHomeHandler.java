package com.akshita.jad.nat.agent.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @description: find jad home
 * @author：flzjkl
 * @date: 2024-07-27 9:12
 */
public class JADHomeHandler {

    private static final Logger logger = LoggerFactory.getLogger(JADHomeHandler.class);
    public static File JAD_HOME_DIR;

    public static void findJADHome() {
        // find jad home
        File jadHomeDir = null;
        try {
            if (jadHomeDir == null) {
                // try to find from ~/.jad/lib
                File jadDir = new File(System.getProperty("user.home"), ".jad" + File.separator + "lib"
                        + File.separator + "jad");
                verifyJADHome(jadDir.getAbsolutePath());
                jadHomeDir = jadDir;
            }
        } catch (Exception e) {
            // ignore
        }

        // Try set the directory where jad-boot.jar is located to arhtas home
        try {
            if (jadHomeDir == null) {
                URL jarUrl = JADHomeHandler.class.getProtectionDomain().getCodeSource().getLocation();
                if (jarUrl != null) {
                    File jadDir = new File(jarUrl.toURI());
                    // If the path is a JAR file, use it directly
                    String jarDir = jadDir.getParent();
                    verifyJADHome(jarDir);
                    if (jadDir != null) {
                        jadHomeDir = new File(jarDir);
                    }
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        if (jadHomeDir == null) {
            logger.error("Please ensure that jad-native agent-client is in the same directory as jad-core.jar, jad-agent.jar, and jad-spy.jar");
            throw new RuntimeException("jad home not found");
        }

        JAD_HOME_DIR = jadHomeDir;
    }

    private static void verifyJADHome(String jadHome) {
        File home = new File(jadHome);
        if (home.isDirectory()) {
            String[] fileList = {"jad-core.jar", "jad-agent.jar", "jad-spy.jar"};

            for (String fileName : fileList) {
                if (!new File(home, fileName).exists()) {
                    logger.error("Please ensure that jad-native agent-client is in the same directory as jad-core.jar, jad-agent.jar, and jad-spy.jar");
                    throw new IllegalArgumentException(
                            fileName + " do not exist, jad home: " + home.getAbsolutePath());
                }
            }
            return;
        }

        throw new IllegalArgumentException("illegal jad home: " + home.getAbsolutePath());
    }
}
