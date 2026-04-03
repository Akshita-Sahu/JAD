package com.akshita.jad.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author earayu
 */
public class JADCheckUtilsTest {

    @Test
    public void testIsIn(){
        Assert.assertTrue(JADCheckUtils.isIn(1,1,2,3));
        Assert.assertFalse(JADCheckUtils.isIn(1,2,3,4));
        Assert.assertTrue(JADCheckUtils.isIn(null,1,null,2));
        Assert.assertFalse(JADCheckUtils.isIn(1,null));
        Assert.assertTrue(JADCheckUtils.isIn(1L,1L,2L,3L));
        Assert.assertFalse(JADCheckUtils.isIn(1L,2L,3L,4L));
        Assert.assertTrue(JADCheckUtils.isIn("foo","foo","bar"));
        Assert.assertFalse(JADCheckUtils.isIn("foo","bar","goo"));
    }


    @Test
    public void testIsEquals(){
        Assert.assertTrue(JADCheckUtils.isEquals(1,1));
        Assert.assertTrue(JADCheckUtils.isEquals(1L,1L));
        Assert.assertTrue(JADCheckUtils.isEquals("foo","foo"));
        Assert.assertFalse(JADCheckUtils.isEquals(1,2));
        Assert.assertFalse(JADCheckUtils.isEquals("foo","bar"));
    }
}
