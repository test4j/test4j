package org.test4j.hamcrest.iassert.object.impl;

import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit5.Test4J;

public class StringAssertTest extends Test4J {

    @Test
    public void testNotContain() {
        String tested = "i am tested string";
        want.string(tested).notContain("is");
    }

    @Test
    public void testNotContain_fail() {
        String tested = "i am tested string";
        want.exception(() -> {
            want.string(tested).contains("tested");
            want.string(tested).notContain("tested");
        }, AssertionError.class);
    }

    @Test
    public void testNotContain_fail2() {
        String tested = "i am tested string";
        want.exception(() ->
                want.string(tested).not(the.string().contains("tested")), AssertionError.class);
    }

    @Test
    public void testNotBlank() {
        String tested = "i am tested string";
        want.string(tested).notBlank();
    }

    @Test
    public void testNotBlank_fail() {
        String tested = "	\t \n";
        want.exception(() ->
                want.string(tested).notBlank(), AssertionError.class);
    }

    @Test
    public void testEqIgnorBlank() {
        want.string("i 	am  string").eqWithStripSpace("i  am string");
    }

    @Test
    public void testEqIgnoreSpace() {
        want.string("i am a string").eqIgnoreSpace("iama string");
    }

    @Test
    public void testContains() {
        want.string("===A\tb\nC====").contains("a b c", StringMode.IgnoreCase, StringMode.SameAsSpace);
    }

    @Test
    public void testContains_failure() {
        want.exception(() ->
                        want.string("===A\tb\nC====").contains("a b c", StringMode.IgnoreCase)
                , AssertionError.class);
    }

    /**
     * 在忽略空格和大小写的情况下，实际字符串以abc结尾
     */
    @Test
    public void testEnd() {
        want.string("=====a b C").end("abc", StringMode.IgnoreCase, StringMode.IgnoreSpace);
    }

    /**
     * 在忽略空格和大小写的情况下，实际字符串以abc开头
     */
    @Test
    public void testStart() {
        want.string("a B C=====").start("abc", StringMode.IgnoreCase, StringMode.IgnoreSpace);
    }

    @Test
    public void testEqIgnoreCase() {
        want.string("Abc").eqIgnoreCase("aBc");
    }

    @Test
    public void testIsEqualTo() {
        want.string("abc").isEqualTo("abc");
        want.string("aBc").isEqualTo("Abc", StringMode.IgnoreCase);
    }

    @Test
    public void testContainsInOrder() {
        want.string("abc cde 123, 456").containsInOrder("abc", "123", "456");
    }

    @Test
    public void testContainsInOrder_Failure() {
        want.exception(() ->
                        want.string("abc cde 123, 456").containsInOrder("abc", "456", "123"),
                AssertionError.class);
    }

    @Test
    public void testStringIgnoreSpace_ChineseChar() {
        want.string("我是        中文 ").isEqualTo("我是中文 ", StringMode.IgnoreSpace);
    }

    @Test
    public void testStringSameSpace_ChineseChar() {
        want.string("我是        中文").isEqualTo("我是     中文", StringMode.SameAsSpace);
    }

    /**
     * 希望字符串不包含子串"abc"和"efg"，但实际上包含了"abc"，断言会抛出错误
     */
    @Test
    public void testNotContains_Failure() {
        want.exception(() ->
                        want.string("abc cba").notContain(new String[]{"abc", "efg"}),
                AssertionError.class);
    }

    /**
     * 希望字符串不包含子串"acb"和"efg"，实际上也不包含，断言通过
     */
    @Test
    public void testNotContains() {
        want.string("abc cba").notContain(new String[]{"acb", "efg"});
    }
}
