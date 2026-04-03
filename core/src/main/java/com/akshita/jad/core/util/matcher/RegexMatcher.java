package com.akshita.jad.core.util.matcher;

import com.akshita.jad.core.util.RegexCacheManager;
import java.util.regex.Pattern;

/**
 * regex matcher
 * @author ralf0131 2017-01-06 13:16.
 */
public class RegexMatcher implements Matcher<String> {

    private final String pattern;
    private volatile Pattern compiledPattern;

    public RegexMatcher(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matching(String target) {
        if (null == target || null == pattern) {
            return false;
        }

        // matching
        if (compiledPattern == null) {
            compiledPattern = RegexCacheManager.getInstance().getPattern(pattern);
        }
        
        return compiledPattern != null && compiledPattern.matcher(target).matches();
    }
}