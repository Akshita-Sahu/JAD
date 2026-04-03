package com.akshita.jad.core.command.monitor200;

import com.akshita.jad.core.command.model.TraceModel;
import com.akshita.jad.core.command.model.TraceTree;
import com.akshita.jad.core.util.ThreadUtil;

/**
 * ThreadLocal
 * @author ralf0131 2017-01-05 14:05.
 */
public class TraceEntity {

    protected TraceTree tree;
    protected int deep;

    public TraceEntity(ClassLoader loader) {
        this.tree = createTraceTree(loader);
        this.deep = 0;
    }

    private TraceTree createTraceTree(ClassLoader loader) {
        return new TraceTree(ThreadUtil.getThreadNode(loader, Thread.currentThread()));
    }

    public TraceModel getModel() {
        tree.trim();
        return new TraceModel(tree.getRoot(), tree.getNodeCount());
    }
}
