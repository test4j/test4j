package org.test4j.hamcrest;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class TestBooleanAssert extends Test4J {

    @Test
    public void test1() {
        want.bool(true).isEqualTo(true);
        want.bool(true).is(true);
    }

    @Test
    public void test2() {
        want.exception(() ->
                        want.bool(true).is(false)
                , AssertionError.class);
    }

    @Test
    public void test3() {
        want.exception(() ->
                        want.fail()
                , AssertionError.class);
    }
}
