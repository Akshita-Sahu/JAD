package com.akshita.jad.core.mcp.tool.function.monitor200;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

/**
 * profiler MCP Tool: Async Profiler（async-profiler）， JAD  profiler 。
 * <p>
 * ：{@link com.akshita.jad.core.command.monitor200.ProfilerCommand}
 */
public class ProfilerTool extends AbstractJADTool {

    private static final String[] SUPPORTED_ACTIONS = new String[] {
            "start",
            "resume",
            "stop",
            "dump",
            "check",
            "status",
            "meminfo",
            "list",
            "version",
            "load",
            "execute",
            "dumpCollapsed",
            "dumpFlat",
            "dumpTraces",
            "getSamples",
            "actions"
    };

    @Tool(
            name = "profiler",
            description = "Async Profiler :  JAD  profiler ， CPU/alloc/lock  flamegraph/jfr 。\n"
                    + ":\n"
                    + "- start: ， action=start, event=cpu\n"
                    + "- stop: ， action=stop, format=flamegraph, file=/tmp/result.html\n"
                    + "- status/list/actions: //\n"
                    + "- execute:  async-profiler agent ， action=execute, actionArg=\"stop,file=/tmp/result.html\""
    )
    public String profiler(
            @ToolParam(description = "（），: start/resume/stop/dump/check/status/meminfo/list/version/load/execute/dumpCollapsed/dumpFlat/dumpTraces/getSamples/actions")
            String action,

            @ToolParam(description = "（）。 action=execute ，: \"stop,file=/tmp/result.html\"", required = false)
            String actionArg,

            @ToolParam(description = " (--event)， cpu/alloc/lock/wall， cpu", required = false)
            String event,

            @ToolParam(description = " ns (--interval)， 10000000(10ms)", required = false)
            Long interval,

            @ToolParam(description = " Java  (--jstackdepth)， 2048", required = false)
            Integer jstackdepth,

            @ToolParam(description = " (--file)。 .html/.jfr  format； %t （ /tmp/result-%t.html）", required = false)
            String file,

            @ToolParam(description = " (--format)， flat[=N]|traces[=N]|collapsed|flamegraph|tree|jfr|md[=N]（ html）", required = false)
            String format,

            @ToolParam(description = "alloc  (--alloc)， 1m/512k/1000 ", required = false)
            String alloc,

            @ToolParam(description = " alloc  (--live)", required = false)
            Boolean live,

            @ToolParam(description = "lock  ns (--lock)， 10ms/10000000 ", required = false)
            String lock,

            @ToolParam(description = " profiler  JFR (--jfrsync)， profile 、.jfc  + ", required = false)
            String jfrsync,

            @ToolParam(description = "wall clock  ms (--wall)， 200", required = false)
            Long wall,

            @ToolParam(description = " (--threads)", required = false)
            Boolean threads,

            @ToolParam(description = " (--sched)", required = false)
            Boolean sched,

            @ToolParam(description = "C  (--cstack)， fp|dwarf|lbr|no", required = false)
            String cstack,

            @ToolParam(description = " (-s)", required = false)
            Boolean simple,

            @ToolParam(description = " (-g)", required = false)
            Boolean sig,

            @ToolParam(description = " Java  (-a)", required = false)
            Boolean ann,

            @ToolParam(description = " (-l)", required = false)
            Boolean lib,

            @ToolParam(description = " (--all-user)", required = false)
            Boolean allUser,

            @ToolParam(description = "， lambda  (--norm)", required = false)
            Boolean norm,

            @ToolParam(description = "（）， --include 'java/*'。。", required = false)
            String[] include,

            @ToolParam(description = "（）， --exclude '*Unsafe.park*'。。", required = false)
            String[] exclude,

            @ToolParam(description = " native  (--begin)", required = false)
            String begin,

            @ToolParam(description = " native  (--end)", required = false)
            String end,

            @ToolParam(description = "time-to-safepoint  (--ttsp)， begin/end ", required = false)
            Boolean ttsp,

            @ToolParam(description = "FlameGraph  (--title)", required = false)
            String title,

            @ToolParam(description = "FlameGraph  (--minwidth)", required = false)
            String minwidth,

            @ToolParam(description = " FlameGraph/Call tree (--reverse)", required = false)
            Boolean reverse,

            @ToolParam(description = " (--total)", required = false)
            Boolean total,

            @ToolParam(description = "JFR chunk  (--chunksize)， 100MB ", required = false)
            String chunksize,

            @ToolParam(description = "JFR chunk  (--chunktime)， 1h", required = false)
            String chunktime,

            @ToolParam(description = " (--loop)， continuous profiling， 300s", required = false)
            String loop,

            @ToolParam(description = " (--timeout)，， 300s", required = false)
            String timeout,

            @ToolParam(description = " (--duration)。： stop ，stop 。", required = false)
            Long duration,

            @ToolParam(description = " (--features)", required = false)
            String features,

            @ToolParam(description = " (--signal)", required = false)
            String signal,

            @ToolParam(description = " (--clock)， monotonic  tsc", required = false)
            String clock,

            ToolContext toolContext
    ) {
        String normalizedAction = normalizeAction(action);
        if ("execute".equals(normalizedAction) && (actionArg == null || actionArg.trim().isEmpty())) {
            throw new IllegalArgumentException("actionArg is required when action=execute");
        }

        StringBuilder cmd = buildCommand("profiler");
        cmd.append(" ").append(normalizedAction);

        if (actionArg != null && !actionArg.trim().isEmpty()) {
            addParameter(cmd, actionArg);
        }

        // profiler options
        addOption(cmd, "--event", event);
        addOption(cmd, "--alloc", alloc);
        addFlag(cmd, "--live", live);
        addOption(cmd, "--lock", lock);
        addOption(cmd, "--jfrsync", jfrsync);

        addOption(cmd, "--file", file);
        addOption(cmd, "--format", format);
        addOption(cmd, "--interval", interval);
        addOption(cmd, "--jstackdepth", jstackdepth);
        addOption(cmd, "--wall", wall);

        addOption(cmd, "--features", features);
        addOption(cmd, "--signal", signal);
        addOption(cmd, "--clock", clock);

        addFlag(cmd, "--threads", threads);
        addFlag(cmd, "--sched", sched);
        addOption(cmd, "--cstack", cstack);

        addFlag(cmd, "-s", simple);
        addFlag(cmd, "-g", sig);
        addFlag(cmd, "-a", ann);
        addFlag(cmd, "-l", lib);
        addFlag(cmd, "--all-user", allUser);
        addFlag(cmd, "--norm", norm);

        addRepeatableOption(cmd, "--include", include);
        addRepeatableOption(cmd, "--exclude", exclude);

        addOption(cmd, "--begin", begin);
        addOption(cmd, "--end", end);
        addFlag(cmd, "--ttsp", ttsp);

        addOption(cmd, "--title", title);
        addOption(cmd, "--minwidth", minwidth);
        addFlag(cmd, "--reverse", reverse);
        addFlag(cmd, "--total", total);

        addOption(cmd, "--chunksize", chunksize);
        addOption(cmd, "--chunktime", chunktime);
        addOption(cmd, "--loop", loop);
        addOption(cmd, "--timeout", timeout);
        addOption(cmd, "--duration", duration);

        logger.info("Executing profiler command: {}", cmd);
        return executeSync(toolContext, cmd.toString());
    }

    private static String normalizeAction(String action) {
        if (action == null || action.trim().isEmpty()) {
            throw new IllegalArgumentException("action is required");
        }

        String input = action.trim();
        for (String supported : SUPPORTED_ACTIONS) {
            if (supported.equalsIgnoreCase(input)) {
                return supported;
            }
        }

        StringBuilder supportedList = new StringBuilder();
        for (int i = 0; i < SUPPORTED_ACTIONS.length; i++) {
            if (i > 0) {
                supportedList.append(", ");
            }
            supportedList.append(SUPPORTED_ACTIONS[i]);
        }
        throw new IllegalArgumentException("Unsupported action: " + input + ". Supported actions: " + supportedList);
    }

    private void addOption(StringBuilder cmd, String option, String value) {
        if (value == null || value.trim().isEmpty()) {
            return;
        }
        cmd.append(" ").append(option);
        addParameter(cmd, value);
    }

    private void addOption(StringBuilder cmd, String option, Long value) {
        if (value == null) {
            return;
        }
        cmd.append(" ").append(option).append(" ").append(value);
    }

    private void addOption(StringBuilder cmd, String option, Integer value) {
        if (value == null) {
            return;
        }
        cmd.append(" ").append(option).append(" ").append(value);
    }

    private void addRepeatableOption(StringBuilder cmd, String option, String[] values) {
        if (values == null || values.length == 0) {
            return;
        }
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) {
                continue;
            }
            cmd.append(" ").append(option);
            addParameter(cmd, value);
        }
    }
}
