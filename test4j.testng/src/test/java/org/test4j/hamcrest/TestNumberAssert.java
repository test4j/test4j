package org.test4j.hamcrest;

import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "assertion" })
public class TestNumberAssert extends Test4J {
    @Test
    public void test1() {
        want.number(3).isBetween(2, 5);
        want.number(3).isGreaterEqual(3);
        want.number(3).isGreaterThan(2);
        want.number(3).isLessEqual(3);
        want.number(3).isLessThan(4);
        want.number(3).isEqualTo(3);
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void test2() {
        want.number(3).isBetween(5, 2);
    }

    public void test3() {
        want.number(5d).isGreaterEqual(4d);
    }

    // /**
    // * the.wanted(Class<T> claz)的类型转换
    // */
    // @SuppressWarnings( { "unchecked", "unused" })
    // @Test(expectedExceptions = Test4JException.class)
    // public void testTheNumber() {
    // Long l = the.doublenum().isEqualTo(3d).wanted(Long.class);
    // Double db = (Double) the.number().isEqualTo(3d).wanted(Double.class);
    // }
}
