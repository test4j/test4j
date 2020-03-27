package org.test4j.hamcrest.matcher.string;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class StringContainsInOrderTest extends Test4J {

    @Test
    public void testMatchesSafely() {
        String actual = "abcefg";
        want.string(actual).containsInOrder("abc", "efg");
    }

    @Test
    public void testMatchesSafely_failure() {
        String actual = "abcefg";
        want.exception(() ->
                        want.string(actual).containsInOrder("abc", "bce")
                , AssertionError.class);
    }

    /**
     * 在不忽略大小写的情况下,字符串"Abc Efg"并包含子串"abc","efg"
     */
    @Test
    public void testContainsInOrder_NoModes() {
        String actual = "Abc Efg";
        want.exception(() ->
                        want.string(actual).containsInOrder(new String[]{"abc", "efg"})
                , AssertionError.class);
    }

    /**
     * 在忽略大小写的情况下,字符串"Abc Efg"包含子串"abc","efg"
     */
    @Test
    public void testContainsInOrder_HasModes() {
        String actual = "Abc Efg";
        want.string(actual).containsInOrder(new String[]{"abc", "efg"}, StringMode.IgnoreCase);
    }

    @Test
    public void testContainInOrder_ActualStringCanNotBeNull() {
        want.exception(() ->
                        want.string(null).contains("")
                , AssertionError.class);
    }

    @Test
    public void testContainInOrder_SubStringCanNotBeNull() {
        try {
            want.string("").contains((String) null);
            throw new RuntimeException("error");
        } catch (AssertionError e) {
            String message = e.getMessage();
            want.string(message).contains("the sub string can't be null");
        }
    }
}
