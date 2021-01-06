package org.test4j.asserts;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

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

    @Test
    public void test2() {
        want.exception(() ->
                        want.number(3).isBetween(5, 2)
                , AssertionError.class);
    }

    @Test
    public void test3() {
        want.number(5d).isGreaterEqual(4d);
    }
}
