package com.akshita.jad.core.mcp.tool.function.klass100;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

public class ClassLoaderTool extends AbstractJADTool {

    public static final String MODE_STATS = "stats";
    public static final String MODE_INSTANCES = "instances";
    public static final String MODE_TREE = "tree";
    public static final String MODE_ALL_CLASSES = "all-classes";
    public static final String MODE_URL_STATS = "url-stats";
    public static final String MODE_URL_CLASSES = "url-classes";

    @Tool(
            name = "classloader",
            description = "ClassLoader ，、、URLs，。 sc "
    )
    public String classloader(
            @ToolParam(description = "：stats(，), instances(), tree(), all-classes(，), url-stats(URL), url-classes(URL)", required = false)
            String mode,

            @ToolParam(description = "ClassLoaderhashcode（16），ClassLoader", required = false)
            String classLoaderHash,

            @ToolParam(description = "ClassLoader，sun.misc.Launcher$AppClassLoader，hashcode", required = false)
            String classLoaderClass,

            @ToolParam(description = "，META-INF/MANIFEST.MF", required = false)
            String resource,

            @ToolParam(description = "，", required = false)
            String loadClass,

            @ToolParam(description = "： URL/jar （ -d）， mode=url-classes ", required = false)
            Boolean details,

            @ToolParam(description = " jar /URL ， mode=url-classes ", required = false)
            String jar,

            @ToolParam(description = "/， mode=url-classes ", required = false)
            String classFilter,

            @ToolParam(description = " jar/class（ -E）， mode=url-classes ", required = false)
            Boolean regex,

            @ToolParam(description = " URL/jar （ -n）， 100， mode=url-classes ", required = false)
            Integer limit,

            ToolContext toolContext) {
        StringBuilder cmd = buildCommand("classloader");

        if (mode != null) {
            switch (mode.toLowerCase()) {
                case MODE_INSTANCES:
                    cmd.append(" -l");
                    break;
                case MODE_TREE:
                    cmd.append(" -t");
                    break;
                case MODE_ALL_CLASSES:
                    cmd.append(" -a");
                    break;
                case MODE_URL_STATS:
                    cmd.append(" --url-stat");
                    break;
                case MODE_URL_CLASSES:
                    cmd.append(" --url-classes");
                    break;
                case MODE_STATS:
                default:
                    break;
            }
        }

        if (classLoaderHash != null && !classLoaderHash.trim().isEmpty()) {
            addParameter(cmd, "-c", classLoaderHash);
        } else if (classLoaderClass != null && !classLoaderClass.trim().isEmpty()) {
            addParameter(cmd, "--classLoaderClass", classLoaderClass);
        }

        addParameter(cmd, "-r", resource);

        addParameter(cmd, "--load", loadClass);

        if (mode != null && MODE_URL_CLASSES.equalsIgnoreCase(mode)) {
            addFlag(cmd, "-d", details);
            addFlag(cmd, "-E", regex);
            if (limit != null && limit > 0) {
                addParameter(cmd, "-n", String.valueOf(limit));
            }
            addParameter(cmd, "--jar", jar);
            addParameter(cmd, "--class", classFilter);
        }

        return executeSync(toolContext, cmd.toString());
    }
}
