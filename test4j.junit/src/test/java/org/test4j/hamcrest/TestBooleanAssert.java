package org.test4j.hamcrest;

import org.junit.Test;
import org.test4j.junit.JTester;

public class TestBooleanAssert implements JTester {

    @Test
    public void test1() {
        want.bool(true).isEqualTo(true);
        want.bool(true).is(true);
    }

    @Test(expected = AssertionError.class)
    public void test2() {
        want.bool(true).is(false);
    }

    @Test(expected = AssertionError.class)
    public void test3() {
        // fail();
        want.fail();
    }
}
