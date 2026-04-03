package com.akshita.jad.core.util;

import java.io.File;

import com.akshita.jad.common.PidUtils;
import com.akshita.jad.core.view.Ansi;

/**
 * @author ralf0131 2016-12-28 16:20.
 */
public class Constants {

    private Constants() {
    }

    /**
     * 
     */
    public static final String Q_OR_CTRL_C_ABORT_MSG = "Press Q or Ctrl+C to abort.";

    /**
     * 
     */
    public static final String EMPTY_STRING = "";

    /**
     * 
     */
    public static final String DEFAULT_PROMPT = "$ ";

    /**
     * 
     * raw string: "[33m$ [m"
     */
    public static final String COLOR_PROMPT = Ansi.ansi().fg(Ansi.Color.YELLOW).a(DEFAULT_PROMPT).reset().toString();

    /**
     * 
     */
    public static final String COST_VARIABLE = "cost";

    public static final String CMD_HISTORY_FILE = System.getProperty("user.home") + File.separator + ".jad" + File.separator + "history";

    /**
     * PID
     */
    public static final String PID = PidUtils.currentPid();

}
