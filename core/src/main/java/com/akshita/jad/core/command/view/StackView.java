package com.akshita.jad.core.command.view;

import com.akshita.jad.core.command.model.StackModel;
import com.akshita.jad.core.shell.command.CommandProcess;
import com.akshita.jad.core.util.DateUtils;
import com.akshita.jad.core.util.ThreadUtil;

/**
 * Term view for StackModel
 * @author gongdewei 2020/4/13
 */
public class StackView extends ResultView<StackModel> {

    @Override
    public void draw(CommandProcess process, StackModel result) {
        StringBuilder sb = new StringBuilder();
        sb.append(ThreadUtil.getThreadTitle(result)).append("\n");

        StackTraceElement[] stackTraceElements = result.getStackTrace();
        StackTraceElement locationStackTraceElement = stackTraceElements[0];
        String locationString = String.format("    @%s.%s()", locationStackTraceElement.getClassName(),
                locationStackTraceElement.getMethodName());
        sb.append(locationString).append("\n");

        int skip = 1;
        for (int index = skip; index < stackTraceElements.length; index++) {
            StackTraceElement ste = stackTraceElements[index];
            sb.append("        at ")
                    .append(ste.getClassName())
                    .append(".")
                    .append(ste.getMethodName())
                    .append("(")
                    .append(ste.getFileName())
                    .append(":")
                    .append(ste.getLineNumber())
                    .append(")\n");
        }
        process.write("ts=" + DateUtils.formatDateTime(result.getTs()) + ";" + sb.toString() + "\n");
    }

}
