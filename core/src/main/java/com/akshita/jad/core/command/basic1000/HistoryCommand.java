package com.akshita.jad.core.command.basic1000;

import java.util.ArrayList;
import java.util.List;

import com.akshita.jad.core.command.Constants;
import com.akshita.jad.core.command.model.HistoryModel;
import com.akshita.jad.core.server.JADBootstrap;
import com.akshita.jad.core.shell.command.AnnotatedCommand;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.shell.history.HistoryManager;
import com.akshita.jad.core.shell.session.Session;
import com.akshita.jad.core.shell.term.impl.TermImpl;
import com.akshita_sahu.middleware.cli.annotations.Argument;
import com.akshita_sahu.middleware.cli.annotations.Description;
import com.akshita_sahu.middleware.cli.annotations.Name;
import com.akshita_sahu.middleware.cli.annotations.Option;
import com.akshita_sahu.middleware.cli.annotations.Summary;

import io.termd.core.readline.Readline;
import io.termd.core.util.Helper;

/**
 *
 * @author hengyunabc 2018-11-18
 *
 */
@Name("history")
@Summary("Display command history")
@Description(Constants.EXAMPLE + "  history\n" + "  history -c\n" + "  history 5\n")
public class HistoryCommand extends AnnotatedCommand {
    boolean clear = false;
    int n = -1;

    @Option(shortName = "c", longName = "clear", flag = true , acceptValue = false)
    @Description("clear history")
    public void setClear(boolean clear) {
        this.clear = clear;
    }

    @Argument(index = 0, argName = "n", required = false)
    @Description("how many history commands to display")
    public void setNumber(int n) {
        this.n = n;
    }

    @Override
    public void process(CommandProcess process) {
        Session session = process.session();
        //TODO term history，HistoryManager
        Object termObject = session.get(Session.TTY);
        if (termObject instanceof TermImpl) {
            TermImpl term = (TermImpl) termObject;
            Readline readline = term.getReadline();
            List<int[]> history = readline.getHistory();

            if (clear) {
                readline.setHistory(new ArrayList<int[]>());
            } else {
                StringBuilder sb = new StringBuilder();

                int size = history.size();
                if (n < 0 || n > size) {
                    n = size;
                }

                for (int i = 0; i < n; ++i) {
                    int[] line = history.get(n - i - 1);
                    sb.append(String.format("%5s  ", size - (n - i - 1)));
                    Helper.appendCodePoints(line, sb);
                    sb.append('\n');
                }

                process.write(sb.toString());
            }
        } else {
            //http api
            HistoryManager historyManager = JADBootstrap.getInstance().getHistoryManager();
            if (clear) {
                historyManager.clearHistory();
            } else {
                List<String> history = historyManager.getHistory();
                process.appendResult(new HistoryModel(new ArrayList<String>(history)));
            }
        }

        process.end();
    }
}
