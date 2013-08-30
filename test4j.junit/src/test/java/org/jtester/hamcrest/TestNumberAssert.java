package org.jtester.hamcrest;

import org.jtester.junit.JTester;
import org.junit.Test;

public class TestNumberAssert implements JTester {
    @Test
    public void test1() {
        want.number(3).isBetween(2, 5);
        want.number(3).isGreaterEqual(3);
        want.number(3).isGreaterThan(2);
        want.number(3).isLessEqual(3);
        want.number(3).isLessThan(4);
        want.number(3).isEqualTo(3);
    }

    @Test(expected = AssertionError.class)
    public void test2() {
        want.number(3).isBetween(5, 2);
    }

    @Test
    public void test3() {
        want.number(5d).isGreaterEqual(4d);
    }
}
