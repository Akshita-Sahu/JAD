package com.akshita.jad.core.view;

import com.akshita.jad.core.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Created by vlinux on 15/5/26.
 */
public class TreeView implements View {

    private static final String STEP_FIRST_CHAR = "`---";
    private static final String STEP_NORMAL_CHAR = "+---";
    private static final String STEP_HAS_BOARD = "|   ";
    private static final String STEP_EMPTY_BOARD = "    ";
    private static final String TIME_UNIT = "ms";

    // 
    private final boolean isPrintCost;

    // 
    private final Node root;

    // 
    private Node current;

    // 
    private Node maxCost;


    public TreeView(boolean isPrintCost, String title) {
        this.root = new Node(title).markBegin().markEnd();
        this.current = root;
        this.isPrintCost = isPrintCost;
    }

    @Override
    public String draw() {

        findMaxCostNode(root);

        final StringBuilder treeSB = new StringBuilder();

        final Ansi highlighted = Ansi.ansi().fg(Ansi.Color.RED);

        recursive(0, true, "", root, new Callback() {

            @Override
            public void callback(int deep, boolean isLast, String prefix, Node node) {
                treeSB.append(prefix).append(isLast ? STEP_FIRST_CHAR : STEP_NORMAL_CHAR);
                if (isPrintCost && !node.isRoot()) {
                    if (node == maxCost) {
                        // the node with max cost will be highlighted
                        treeSB.append(highlighted.a(node.toString()).reset().toString());
                    } else {
                        treeSB.append(node.toString());
                    }
                }
                treeSB.append(node.data);
                if (!StringUtils.isBlank(node.mark)) {
                    treeSB.append(" [").append(node.mark).append(node.marks > 1 ? "," + node.marks : "").append("]");
                }
                treeSB.append("\n");
            }

        });

        return treeSB.toString();
    }

    /**
     * 
     */
    private void recursive(int deep, boolean isLast, String prefix, Node node, Callback callback) {
        callback.callback(deep, isLast, prefix, node);
        if (!node.isLeaf()) {
            final int size = node.children.size();
            for (int index = 0; index < size; index++) {
                final boolean isLastFlag = index == size - 1;
                final String currentPrefix = isLast ? prefix + STEP_EMPTY_BOARD : prefix + STEP_HAS_BOARD;
                recursive(
                        deep + 1,
                        isLastFlag,
                        currentPrefix,
                        node.children.get(index),
                        callback
                );
            }
        }
    }

    /**
     * ，
     * @param node
     */
    private void findMaxCostNode(Node node) {
        if (!node.isRoot() && !node.parent.isRoot()) {
            if (maxCost == null) {
                maxCost = node;
            } else if (maxCost.totalCost < node.totalCost) {
                maxCost = node;
            }
        }
        if (!node.isLeaf()) {
            for (Node n: node.children) {
                findMaxCostNode(n);
            }
        }
    }


    /**
     * 
     *
     * @param data 
     * @return this
     */
    public TreeView begin(String data) {
        Node n = current.find(data);
        if (n != null) {
            current = n;
        } else {
            current = new Node(current, data);
        }
        current.markBegin();
        return this;
    }

    /**
     * 
     *
     * @return this
     */
    public TreeView end() {
        if (current.isRoot()) {
            throw new IllegalStateException("current node is root.");
        }
        current.markEnd();
        current = current.parent;
        return this;
    }

    /**
     * ,
     *
     * @return this
     */
    public TreeView end(String mark) {
        if (current.isRoot()) {
            throw new IllegalStateException("current node is root.");
        }
        current.markEnd().mark(mark);
        current = current.parent;
        return this;
    }


    /**
     * 
     */
    private static class Node {

        /**
         * 
         */
        final Node parent;

        /**
         * 
         */
        final String data;

        /**
         * 
         */
        final List<Node> children = new ArrayList<Node>();

        final Map<String, Node> map = new HashMap<String, Node>();

        /**
         * 
         */
        private long beginTimestamp;

        /**
         * 
         */
        private long endTimestamp;

        /**
         * 
         */
        private String mark;

        /**
         * ()
         */
        private Node(String data) {
            this.parent = null;
            this.data = data;
        }

        /**
         * 
         *
         * @param parent 
         * @param data   
         */
        private Node(Node parent, String data) {
            this.parent = parent;
            this.data = data;
            parent.children.add(this);
            parent.map.put(data, this);
        }

        /**
         * 
         */
        Node find(String data) {
            return map.get(data);
        }

        /**
         * 
         *
         * @return true / false
         */
        boolean isRoot() {
            return null == parent;
        }

        /**
         * 
         *
         * @return true / false
         */
        boolean isLeaf() {
            return children.isEmpty();
        }

        Node markBegin() {
            beginTimestamp = System.nanoTime();
            return this;
        }

        Node markEnd() {
            endTimestamp = System.nanoTime();

            long cost = getCost();
            if (cost < minCost) {
                minCost = cost;
            }
            if (cost > maxCost) {
                maxCost = cost;
            }
            times++;
            totalCost += cost;

            return this;
        }

        Node mark(String mark) {
            this.mark = mark;
            marks++;
            return this;
        }

        long getCost() {
            return endTimestamp - beginTimestamp;
        }

        /**
         * convert nano-seconds to milli-seconds
         */
        double getCostInMillis(long nanoSeconds) {
            return nanoSeconds / 1000000.0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (times <= 1) {
                sb.append("[").append(getCostInMillis(getCost())).append(TIME_UNIT).append("] ");
            } else {
                sb.append("[min=").append(getCostInMillis(minCost)).append(TIME_UNIT).append(",max=")
                        .append(getCostInMillis(maxCost)).append(TIME_UNIT).append(",total=")
                        .append(getCostInMillis(totalCost)).append(TIME_UNIT).append(",count=")
                        .append(times).append("] ");
            }
            return sb.toString();
        }

        /**
         * ,\\
         */
        private long minCost = Long.MAX_VALUE;
        private long maxCost = Long.MIN_VALUE;
        private long totalCost = 0;
        private long times = 0;
        private long marks = 0;
    }


    /**
     * 
     */
    private interface Callback {

        void callback(int deep, boolean isLast, String prefix, Node node);

    }

}
