package org.test4j.hamcrest;

import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "assertion" })
public class TestBooleanAssert extends Test4J {

    @Test
    public void test1() {
        want.bool(true).isEqualTo(true);
        want.bool(true).is(true);
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void test2() {
        want.bool(true).is(false);
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void test3() {
        // fail();
        want.fail();
    }
}
