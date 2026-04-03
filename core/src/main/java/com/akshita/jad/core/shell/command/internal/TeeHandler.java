package com.akshita.jad.core.shell.command.internal;

import com.akshita.jad.core.command.basic1000.TeeCommand;
import com.akshita.jad.core.shell.cli.CliToken;
import com.akshita.jad.core.util.StringUtils;
import com.akshita_sahu.middleware.cli.CLI;
import com.akshita_sahu.middleware.cli.CommandLine;
import com.akshita_sahu.middleware.cli.annotations.CLIConfigurator;

import java.io.*;
import java.util.List;

/**
 * @author min.yang
 */
public class TeeHandler extends StdoutHandler implements CloseFunction {
    public static final String NAME = "tee";
    private PrintWriter out;
    private static CLI cli = null;

    public TeeHandler(String filePath, boolean append) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);

        if (file.isDirectory()) {
            throw new IOException(filePath + ": Is a directory");
        }

        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
        }
        out = new PrintWriter(new BufferedWriter(new FileWriter(file, append)));
    }

    public static StdoutHandler inject(List<CliToken> tokens) {
        List<String> args = StdoutHandler.parseArgs(tokens, NAME);

        TeeCommand teeCommand = new TeeCommand();
        if (cli == null) {
            cli = CLIConfigurator.define(TeeCommand.class);
        }
        CommandLine commandLine = cli.parse(args, true);

        try {
            CLIConfigurator.inject(commandLine, teeCommand);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        String filePath = teeCommand.getFilePath();
        boolean append = teeCommand.isAppend();
        try {
            return new TeeHandler(filePath, append);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String apply(String data) {
        data = super.apply(data);
        if (out != null) {
            out.write(data);
            out.flush();
        }
        return data;
    }

    @Override
    public void close() {
        if (out != null) {
            out.close();
        }
    }
}
