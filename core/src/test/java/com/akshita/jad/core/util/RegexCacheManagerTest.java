package com.akshita.jad.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * RegexCacheManager
 */
public class RegexCacheManagerTest {
    private RegexCacheManager cacheManager;

    @Before
    public void setUp() {
        // 
        cacheManager = RegexCacheManager.getInstance();
        // ，
        cacheManager.clearCache();
    }

    /**
     * 
     */
    @Test
    public void testBasicCacheFunctionality() {
        // 
        String regex1 = ".*Test.*";
        Pattern pattern1 = cacheManager.getPattern(regex1);
        Assert.assertNotNull(pattern1);
        Assert.assertEquals(1, cacheManager.getCacheSize());

        // 
        Pattern pattern1Cached = cacheManager.getPattern(regex1);
        Assert.assertNotNull(pattern1Cached);
        Assert.assertSame(pattern1, pattern1Cached); // 
        Assert.assertEquals(1, cacheManager.getCacheSize()); // 

        // 
        String regex2 = "^Test.*";
        Pattern pattern2 = cacheManager.getPattern(regex2);
        Assert.assertNotNull(pattern2);
        Assert.assertEquals(2, cacheManager.getCacheSize());

        // 
        Pattern nullPattern = cacheManager.getPattern(null);
        Assert.assertNull(nullPattern);

        Pattern emptyPattern = cacheManager.getPattern("");
        Assert.assertNotNull(emptyPattern);
        Assert.assertTrue(emptyPattern.matcher("").matches());
        Assert.assertFalse(emptyPattern.matcher("non-empty").matches());
        Assert.assertEquals(3, cacheManager.getCacheSize());
    }

    /**
     * LRU
     */
    @Test
    public void testLRUEvictionPolicy() {
        // ，
        int maxCacheSize = 100;
        for (int i = 0; i < maxCacheSize + 5; i++) {
            String regex = "TestRegex" + i;
            Pattern pattern = cacheManager.getPattern(regex);
            Assert.assertNotNull(pattern);
        }

        // 
        Assert.assertEquals(maxCacheSize, cacheManager.getCacheSize()); // 100 

        // ，LRU
        String firstRegex = "TestRegex0";

        // ，
        Pattern firstPattern = cacheManager.getPattern(firstRegex);
        Assert.assertNotNull(firstPattern);

        // ，
        String newRegex = "NewTestRegex";
        Pattern newPattern = cacheManager.getPattern(newRegex);
        Assert.assertNotNull(newPattern);

        // （）
        Pattern firstPatternAgain = cacheManager.getPattern(firstRegex);
        Assert.assertNotNull(firstPatternAgain);
    }

    /**
     * 
     */
    @Test
    public void testCacheClear() {
        // 
        cacheManager.getPattern(".*Test1");
        cacheManager.getPattern(".*Test2");
        Assert.assertTrue(cacheManager.getCacheSize() > 0);

        // 
        cacheManager.clearCache();
        Assert.assertEquals(0, cacheManager.getCacheSize());

        // 
        Pattern pattern = cacheManager.getPattern(".*Test3");
        Assert.assertNotNull(pattern);
        Assert.assertEquals(1, cacheManager.getCacheSize());
    }

    /**
     * 
     */
    @Test
    public void testInvalidRegexHandling() {
        // ，PatternSyntaxException
        String invalidRegex = "[a-z";
        try {
            cacheManager.getPattern(invalidRegex);
        } catch (Exception e) {
            // PatternSyntaxException
            Assert.assertTrue("Expected PatternSyntaxException but got " + e.getClass().getName(), e instanceof PatternSyntaxException);
        }
        
        // ，PatternSyntaxException
        String anotherInvalidRegex = "(a-z";
        try {
            cacheManager.getPattern(anotherInvalidRegex);
        } catch (Exception e) {
            // PatternSyntaxException
            Assert.assertTrue("Expected PatternSyntaxException but got " + e.getClass().getName(), e instanceof PatternSyntaxException);
        }
        
        // 
        Assert.assertEquals("", 0, cacheManager.getCacheSize());
    }

}
