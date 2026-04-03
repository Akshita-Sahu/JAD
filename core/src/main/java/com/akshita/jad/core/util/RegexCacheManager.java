package com.akshita.jad.core.util;

import com.akshita.jad.core.shell.term.impl.http.session.LRUCache;
import java.util.regex.Pattern;

/**
 * 
 * ，
 */
public class RegexCacheManager {
    private static final RegexCacheManager INSTANCE = new RegexCacheManager();
    
    // LRUCache
    private final LRUCache<String, Pattern> regexCache;
    
    // 
    private static final int MAX_CACHE_SIZE = 100;
    
    private RegexCacheManager() {
        // LRUCache，
        this.regexCache = new LRUCache<>(MAX_CACHE_SIZE);
    }

    public static RegexCacheManager getInstance() {
        return INSTANCE;
    }

    /**
     * Pattern，，
     */
    public Pattern getPattern(String regex) {
        if (regex == null) {
            return null;
        }
        
        // LRUCache
        Pattern pattern = regexCache.get(regex);
        if (pattern != null) {
            return pattern;
        }

        // ，
        // PatternSyntaxException，，
        pattern = Pattern.compile(regex);
        // 
        regexCache.put(regex, pattern);
        
        return pattern;
    }
    
    /**
     * 
     */
    public void clearCache() {
        regexCache.clear();
    }
    
    /**
     * 
     */
    public int getCacheSize() {
        return regexCache.usedEntries();
    }

}
