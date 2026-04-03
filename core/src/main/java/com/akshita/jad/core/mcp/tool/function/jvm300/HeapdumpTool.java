package com.akshita.jad.core.mcp.tool.function.jvm300;

import com.akshita.jad.core.mcp.tool.function.AbstractJADTool;
import com.akshita.jad.mcp.server.tool.ToolContext;
import com.akshita.jad.mcp.server.tool.annotation.Tool;
import com.akshita.jad.mcp.server.tool.annotation.ToolParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HeapdumpTool extends AbstractJADTool {

    public static final String DEFAULT_DUMP_DIR = Paths.get("jad-output").toAbsolutePath().toString().replace("\\", "/");

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * heapdump 
     * :
     * - live:  dump  (--live)
     * - filePath: ，
     */
    @Tool(
            name = "heapdump",
            description = "Heapdump :  JVM heap dump， --live 。 JAD  heapdump 。"
    )
    public String heapdump(
            @ToolParam(description = " dump  (--live)", required = false)
            Boolean live,

            @ToolParam(description = "，jad-output.hprof", required = false)
            String filePath,

            ToolContext toolContext
    ) throws IOException {
        String finalFilePath;

        if (filePath != null && !filePath.trim().isEmpty()) {
            finalFilePath = filePath.trim().replace("\\", "/");
        } else {
            Path defaultDir = Paths.get(DEFAULT_DUMP_DIR);
            if (!Files.exists(defaultDir)) {
                Files.createDirectories(defaultDir);
            }

            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            String defaultFileName = String.format("heapdump_%s.hprof", timestamp);
            finalFilePath = Paths.get(DEFAULT_DUMP_DIR, defaultFileName).toString().replace("\\", "/");
        }

        StringBuilder cmd = buildCommand("heapdump");
        addFlag(cmd, "--live", live);
        cmd.append(" ").append(finalFilePath);

        return executeSync(toolContext, cmd.toString());
    }
}
