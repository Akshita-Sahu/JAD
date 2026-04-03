package com.akshita.jad.core.command.express;

import org.junit.Assert;
import org.junit.Test;

public class OgnlExpressTest {

    @Test
    public void testValidOgnlExpr1() throws ExpressException {
        Express unpooledExpress = ExpressFactory.unpooledExpress(OgnlExpressTest.class.getClassLoader());
        Assert.assertEquals(unpooledExpress.get("\"test\".length() % 2 == 0 ? \"even length\" : \"odd length\""),
                "even length");
    }

    @Test
    public void testValidOgnlExpr2() throws ExpressException {
        System.setProperty("ognl.chain.short-circuit", String.valueOf(false));
        Express unpooledExpress = ExpressFactory.unpooledExpress(OgnlExpressTest.class.getClassLoader());
        Assert.assertEquals(unpooledExpress.get("4 in {1, 2, 3, 4}"), true);
        Assert.assertEquals(unpooledExpress.get("{1, 2, 3, 4}.{^ #this % 2 == 0}[$]"), 2);
        Assert.assertEquals(unpooledExpress.get("{1, 2, 3, 4}.{? #this % 2 == 0}[$]"), 4);
    }

    @Test
    public void testValidOgnlExpr3() throws ExpressException {
        Express unpooledExpress = ExpressFactory.unpooledExpress(OgnlExpressTest.class.getClassLoader());
        Assert.assertEquals(unpooledExpress.get("#factorial = :[#this <= 1 ? 1 : #this * #factorial(#this - 1)], #factorial(5)"),
                120);
    }

    @Test
    public void testValidOgnlExpr4() throws ExpressException {
        Express unpooledExpress = ExpressFactory.unpooledExpress(OgnlExpressTest.class.getClassLoader());
        System.setProperty("jad.test1", "jad");
        System.setProperty("jad.ognl.test2", "test");
        Assert.assertEquals(unpooledExpress.get("#value1=@System@getProperty(\"jad.test1\")," +
                        "#value2=@System@getProperty(\"jad.ognl.test2\"), {#value1, #value2}").toString(),
                "[jad, test]");
        System.clearProperty("jad.test1");
        System.clearProperty("jad.ognl.test2");
    }

    @Test
    public void testInvalidOgnlExpr() {
        try {
            Express unpooledExpress = ExpressFactory.unpooledExpress(OgnlExpressTest.class.getClassLoader());
            System.out.println(unpooledExpress.get("#value1=@System.getProperty(\"java.home\")," +
                            "#value2=@System@getProperty(\"java.runtime.name\"), {#value1, #value2}").toString());
        } catch (Exception e){
            Assert.assertTrue(e.getCause() instanceof ognl.ExpressionSyntaxException);
        }
    }
}
