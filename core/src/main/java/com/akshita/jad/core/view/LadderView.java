package com.akshita.jad.core.view;

import com.akshita.jad.core.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Created by vlinux on 15/5/8.
 */
public class LadderView implements View {

    // 
    private static final String LADDER_CHAR = "`-";

    // 
    private static final String STEP_CHAR = " ";

    // 
    private static final int INDENT_STEP = 2;

    private final List<String> items = new ArrayList<String>();


    @Override
    public String draw() {
        final StringBuilder ladderSB = new StringBuilder();
        int deep = 0;
        for (String item : items) {

            // 
            if (deep == 0) {
                ladderSB
                        .append(item)
                        .append("\n");
            }

            // 
            else {
                ladderSB
                        .append(StringUtils.repeat(STEP_CHAR, deep * INDENT_STEP))
                        .append(LADDER_CHAR)
                        .append(item)
                        .append("\n");
            }

            deep++;

        }
        return ladderSB.toString();
    }

    /**
     * 
     *
     * @param item 
     * @return this
     */
    public LadderView addItem(String item) {
        items.add(item);
        return this;
    }

}
