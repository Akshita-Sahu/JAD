package com.akshita.jad.core.shell.history;

import java.util.List;

/**
 * @author gongdewei 2020/4/8
 */
public interface HistoryManager {

    void addHistory(String commandLine);

    List<String> getHistory();

    void setHistory(List<String> history);

    void saveHistory();

    void loadHistory();

    void clearHistory();
}
