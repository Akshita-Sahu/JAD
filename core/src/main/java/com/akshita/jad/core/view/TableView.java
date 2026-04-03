package com.akshita.jad.core.view;

import com.akshita.jad.core.util.Constants;
import com.akshita.jad.core.util.StringUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.String.format;

/**
 * 
 * Created by vlinux on 15/5/7.
 */
public class TableView implements View {

    /**
     * 
     */
    public static final int BORDER_TOP = 1;

    /**
     * 
     */
    public static final int BORDER_BOTTOM = 1 << 1;

    // 
    private final ColumnDefine[] columnDefineArray;

    // 
    private boolean hasBorder;

    // 
    private int borders = BORDER_TOP | BORDER_BOTTOM;

    // 
    private int padding;

    public TableView(ColumnDefine[] columnDefineArray) {
        this.columnDefineArray = null == columnDefineArray
                ? new ColumnDefine[0]
                : columnDefineArray;
    }

    public TableView(int columnNum) {
        this.columnDefineArray = new ColumnDefine[columnNum];
        for (int index = 0; index < this.columnDefineArray.length; index++) {
            columnDefineArray[index] = new ColumnDefine();
        }
    }

    private boolean isAnyBorder(int... borders) {
        if (null == borders) {
            return false;
        }
        for (int b : borders) {
            if ((this.borders & b) == b) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     *
     * @return 
     */
    public int borders() {
        return borders;
    }

    /**
     * 
     *
     * @param border 
     * @return this
     */
    public TableView borders(int border) {
        this.borders = border;
        return this;
    }

    @Override
    public String draw() {
        final StringBuilder tableSB = new StringBuilder();

        // init width cache
        final int[] widthCacheArray = new int[getColumnCount()];
        for (int index = 0; index < widthCacheArray.length; index++) {
            widthCacheArray[index] = abs(columnDefineArray[index].getWidth());
        }

        final int tableHigh = getTableHigh();
        for (int rowIndex = 0; rowIndex < tableHigh; rowIndex++) {

            final boolean isFirstRow = rowIndex == 0;
            final boolean isLastRow = rowIndex == tableHigh - 1;

            // 
            if (isFirstRow
                    && hasBorder()
                    && isAnyBorder(BORDER_TOP)) {
                tableSB.append(drawSeparationLine(widthCacheArray)).append("\n");
            }

            // 
            if (!isFirstRow
                    && hasBorder()) {
                tableSB.append(drawSeparationLine(widthCacheArray)).append("\n");
            }

            // 
            tableSB.append(drawRow(widthCacheArray, rowIndex));


            // 
            if (isLastRow
                    && hasBorder()
                    && isAnyBorder(BORDER_BOTTOM)) {
                // 
                tableSB.append(drawSeparationLine(widthCacheArray)).append("\n");
            }

        }


        return tableSB.toString();
    }


    private String drawRow(int[] widthCacheArray, int rowIndex) {

        final StringBuilder rowSB = new StringBuilder();
        final Scanner[] scannerArray = new Scanner[getColumnCount()];
        try {
            boolean hasNext;
            do {

                hasNext = false;
                final StringBuilder segmentSB = new StringBuilder();

                for (int colIndex = 0; colIndex < getColumnCount(); colIndex++) {


                    final String borderChar = hasBorder() ? "|" : Constants.EMPTY_STRING;
                    final int width = widthCacheArray[colIndex];
                    final boolean isLastColOfRow = colIndex == widthCacheArray.length - 1;


                    if (null == scannerArray[colIndex]) {
                        scannerArray[colIndex] = new Scanner(
                                new StringReader(StringUtils.wrap(getData(rowIndex, columnDefineArray[colIndex]), width)));
                    }
                    final Scanner scanner = scannerArray[colIndex];

                    final String data;
                    if (scanner.hasNext()) {
                        data = scanner.nextLine();
                        hasNext = true;
                    } else {
                        data = Constants.EMPTY_STRING;
                    }

                    if (width > 0) {
                        final ColumnDefine columnDefine = columnDefineArray[colIndex];
                        final String dataFormat = getDataFormat(columnDefine, width);
                        final String paddingChar = StringUtils.repeat(" ", padding);
                        segmentSB.append(format(borderChar + paddingChar + dataFormat + paddingChar, data));
                    }

                    if (isLastColOfRow) {
                        segmentSB.append(borderChar).append("\n");
                    }

                }

                if (hasNext) {
                    rowSB.append(segmentSB);
                }

            } while (hasNext);

            return rowSB.toString();
        } finally {
            for (Scanner scanner : scannerArray) {
                if (null != scanner) {
                    scanner.close();
                }
            }
        }

    }

    private String getData(int rowIndex, ColumnDefine columnDefine) {
        return columnDefine.getHigh() <= rowIndex
                ? Constants.EMPTY_STRING
                : columnDefine.dataList.get(rowIndex);
    }

    private String getDataFormat(ColumnDefine columnDefine, int width) {
        switch (columnDefine.align) {
            case RIGHT: {
                return "%" + width + "s";
            }
            case LEFT:
            default: {
                return "%-" + width + "s";
            }
        }
    }

    /*
     * 
     */
    private int getTableHigh() {
        int tableHigh = 0;
        for (ColumnDefine columnDefine : columnDefineArray) {
            tableHigh = max(tableHigh, columnDefine.getHigh());
        }
        return tableHigh;
    }

    /*
     * 
     */
    private String drawSeparationLine(int[] widthCacheArray) {
        final StringBuilder separationLineSB = new StringBuilder();
        for (int width : widthCacheArray) {
            if (width > 0) {
                separationLineSB.append("+").append(StringUtils.repeat("-", width + 2 * padding));
            }
        }
        return separationLineSB
                .append("+")
                .toString();
    }

    /**
     * 
     *
     * @param columnDataArray 
     */
    public TableView addRow(Object... columnDataArray) {
        if (null == columnDataArray) {
            return this;
        }

        for (int index = 0; index < columnDefineArray.length; index++) {
            final ColumnDefine columnDefine = columnDefineArray[index];
            if (index < columnDataArray.length
                    && null != columnDataArray[index]) {
                columnDefine.dataList.add(StringUtils.replace(columnDataArray[index].toString(), "\t", "    "));
            } else {
                columnDefine.dataList.add(Constants.EMPTY_STRING);
            }
        }

        return this;
    }


    /**
     * 
     */
    public enum Align {
        LEFT,
        RIGHT
    }

    /**
     * 
     */
    public static class ColumnDefine {

        private final int width;
        private final boolean isAutoResize;
        private final Align align;
        private final List<String> dataList = new ArrayList<String>();

        public ColumnDefine(int width, boolean isAutoResize, Align align) {
            this.width = width;
            this.isAutoResize = isAutoResize;
            this.align = align;
        }

        public ColumnDefine(Align align) {
            this(0, true, align);
        }

        public ColumnDefine() {
            this(Align.LEFT);
        }

        /**
         * 
         *
         * @return 
         */
        public int getWidth() {

            if (!isAutoResize) {
                return width;
            }

            int maxWidth = 0;
            for (String data : dataList) {
                final Scanner scanner = new Scanner(new StringReader(data));
                try {
                    while (scanner.hasNext()) {
                        maxWidth = max(StringUtils.length(scanner.nextLine()), maxWidth);
                    }
                } finally {
                    scanner.close();
                }
            }

            return maxWidth;
        }

        /**
         * 
         *
         * @return 
         */
        public int getHigh() {
            return dataList.size();
        }

    }

    /**
     * 
     *
     * @param hasBorder true / false
     */
    public TableView hasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        return this;
    }

    /**
     * 
     *
     * @return true / false
     */
    public boolean hasBorder() {
        return hasBorder;
    }

    /**
     * 
     *
     * @param padding 
     */
    public TableView padding(int padding) {
        this.padding = padding;
        return this;
    }

    /**
     * 
     *
     * @return 
     */
    public int getColumnCount() {
        return columnDefineArray.length;
    }

}
