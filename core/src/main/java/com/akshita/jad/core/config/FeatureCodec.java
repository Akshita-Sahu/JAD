package com.akshita.jad.core.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static com.akshita.jad.core.util.JADCheckUtils.isEquals;
import static com.akshita.jad.core.util.JADCheckUtils.isIn;
import static com.akshita.jad.core.util.StringUtils.isBlank;

/**
 * Feature()<br/>
 * <p/>
 * features/attribute
 * Created by dukun on 15/3/31.
 */
public class FeatureCodec {
    // 
    public final static FeatureCodec DEFAULT_COMMANDLINE_CODEC = new FeatureCodec(';', '=');

    /**
     * KV<br/>
     * KVKV，<span>;k1=v1;k2=v2;</span>
     * <b>;</b>KV
     */
    private final char kvSegmentSeparator;

    /**
     * KV<br/>
     * KVKVKV，<span>k1=v1</span>
     * <b>=</b>KV
     */
    private final char kvSeparator;

    /**
     * 
     */
    private static final char ESCAPE_PREFIX_CHAR = '\\';

    /**
     * KVFeatureParser<br/>
     *
     * @param kvSegmentSeparator KV
     * @param kvSeparator        KV
     */
    public FeatureCodec(final char kvSegmentSeparator, final char kvSeparator) {

        // 
        if (isIn(ESCAPE_PREFIX_CHAR, kvSegmentSeparator, kvSeparator)) {
            throw new IllegalArgumentException("separator can not init to '" + ESCAPE_PREFIX_CHAR + "'.");
        }

        this.kvSegmentSeparator = kvSegmentSeparator;
        this.kvSeparator = kvSeparator;
    }

    /**
     * mapfeature
     *
     * @param map map
     * @return feature
     */
    public String toString(final Map<String, String> map) {

        final StringBuilder featureSB = new StringBuilder().append(kvSegmentSeparator);

        if (null == map
                || map.isEmpty()) {
            return featureSB.toString();
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {

            featureSB
                    .append(escapeEncode(entry.getKey()))
                    .append(kvSeparator)
                    .append(escapeEncode(entry.getValue()))
                    .append(kvSegmentSeparator)
            ;

        }

        return featureSB.toString();
    }


    /**
     * featuremap
     *
     * @param featureString the feature string
     * @return the map
     */
    public Map<String, String> toMap(final String featureString) {

        final Map<String, String> map = new HashMap<String, String>();

        if (isBlank(featureString)) {
            return map;
        }

        for (String kv : escapeSplit(featureString, kvSegmentSeparator)) {

            if (isBlank(kv)) {
                // 
                continue;
            }

            final String[] ar = escapeSplit(kv, kvSeparator);
            if (ar.length != 2) {
                // K:V
                continue;
            }

            final String k = ar[0];
            final String v = ar[1];
            if (!isBlank(k)
                    && !isBlank(v)) {
                map.put(escapeDecode(k), escapeDecode(v));
            }

        }

        return map;
    }

    /**
     * 
     *
     * @param string 
     * @return 
     */
    private String escapeEncode(final String string) {
        final StringBuilder returnSB = new StringBuilder();
        for (final char c : string.toCharArray()) {
            if (isIn(c, kvSegmentSeparator, kvSeparator, ESCAPE_PREFIX_CHAR)) {
                returnSB.append(ESCAPE_PREFIX_CHAR);
            }
            returnSB.append(c);
        }

        return returnSB.toString();
    }

    /**
     * 
     *
     * @param string 
     * @return 
     */
    private String escapeDecode(String string) {

        final StringBuilder segmentSB = new StringBuilder();
        final int stringLength = string.length();

        for (int index = 0; index < stringLength; index++) {

            final char c = string.charAt(index);

            if (isEquals(c, ESCAPE_PREFIX_CHAR)
                    && index < stringLength - 1) {

                final char nextChar = string.charAt(++index);

                // 
                if (isIn(nextChar, kvSegmentSeparator, kvSeparator, ESCAPE_PREFIX_CHAR)) {
                    segmentSB.append(nextChar);
                }

                // ，
                else {
                    segmentSB.append(c);
                    segmentSB.append(nextChar);
                }
            } else {
                segmentSB.append(c);
            }

        }

        return segmentSB.toString();
    }

    /**
     * 
     *
     * @param string          
     * @param splitEscapeChar 
     * @return 
     */
    private String[] escapeSplit(String string, char splitEscapeChar) {

        final ArrayList<String> segmentArrayList = new ArrayList<String>();
        final Stack<Character> decodeStack = new Stack<Character>();
        final int stringLength = string.length();

        for (int index = 0; index < stringLength; index++) {

            boolean isArchive = false;

            final char c = string.charAt(index);

            // 
            if (isEquals(c, ESCAPE_PREFIX_CHAR)) {

                decodeStack.push(c);
                if (index < stringLength - 1) {
                    final char nextChar = string.charAt(++index);
                    decodeStack.push(nextChar);
                }

            }

            // 
            else if (isEquals(c, splitEscapeChar)) {
                isArchive = true;
            }

            // 
            else {
                decodeStack.push(c);
            }

            if (isArchive
                    || index == stringLength - 1) {
                final StringBuilder segmentSB = new StringBuilder(decodeStack.size());
                while (!decodeStack.isEmpty()) {
                    segmentSB.append(decodeStack.pop());
                }

                segmentArrayList.add(
                        segmentSB
                                .reverse()  // ,
                                .toString() // toString
                                .trim()     // ，
                );
            }

        }

        return segmentArrayList.toArray(new String[0]);
    }


}