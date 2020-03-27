package org.test4j.hamcrest;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class TestStringAssert extends Test4J {
    @Test
    public void test11() {
        want.string("ddd").contains("d").contains("d");
        want.string("ddd").isEqualTo("ddd");
        want.string("ddd").eqIgnoreCase("dDD");
        want.string("eeeed").end("ed");
        want.string("eeeed").end("ed").start("eee");
    }

    @Test
    public void test12() {
        want.exception(() ->
                        want.string("ddd").contains("de").contains("d")
                , AssertionError.class);
    }

    @Test
    public void test13() {
        want.exception(() ->
                        want.string("ddd").contains("d").contains("de")
                , AssertionError.class);
    }

    @Test
    public void test21() {
        want.exception(() ->
                        want.string("abcd").any(the.string().contains("ad"), the.string().contains("de"))
                , AssertionError.class);
    }

    @Test
    public void test22() {
        want.string("abcd").any(the.string().contains("ab"), the.string().contains("cd"));
    }

    @Test
    public void test31() {
        want.string("abcd").all(the.string().contains("ab"), the.string().contains("cd"));
    }

    @Test
    public void test32() {
        want.exception(() ->
                        want.string("abcd").all(the.string().contains("ad"), the.string().contains("cd"))
                , AssertionError.class);
    }

    @Test
    public void eqIgnorBlank() {
        want.string(" ab ").eqIgnoreSpace("ab");
        want.string("abC").eqIgnoreCase("aBc");
    }
}
