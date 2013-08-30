package org.jtester.hamcrest;

import org.jtester.junit.JTester;
import org.junit.Test;

public class TestStringAssert implements JTester {
    @Test
    public void test11() {
        want.string("ddd").contains("d").contains("d");
        want.string("ddd").isEqualTo("ddd");
        want.string("ddd").eqIgnoreCase("dDD");
        want.string("eeeed").end("ed");
        want.string("eeeed").end("ed").start("eee");
    }

    @Test(expected = AssertionError.class)
    public void test12() {
        want.string("ddd").contains("de").contains("d");
    }

    @Test(expected = AssertionError.class)
    public void test13() {
        want.string("ddd").contains("d").contains("de");
    }

    @Test(expected = AssertionError.class)
    public void test21() {
        want.string("abcd").any(the.string().contains("ad"), the.string().contains("de"));
    }

    @Test
    public void test22() {
        want.string("abcd").any(the.string().contains("ab"), the.string().contains("cd"));
    }

    @Test
    public void test31() {
        want.string("abcd").all(the.string().contains("ab"), the.string().contains("cd"));
    }

    @Test(expected = AssertionError.class)
    public void test32() {
        want.string("abcd").all(the.string().contains("ad"), the.string().contains("cd"));
    }

    @Test
    public void eqIgnorBlank() {
        want.string(" ab ").eqIgnoreSpace("ab");
        want.string("abC").eqIgnoreCase("aBc");
    }
}
