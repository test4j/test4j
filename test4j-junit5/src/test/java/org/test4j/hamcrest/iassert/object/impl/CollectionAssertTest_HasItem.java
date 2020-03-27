package org.test4j.hamcrest.iassert.object.impl;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class CollectionAssertTest_HasItem extends Test4J {
    @Test
    public void hasItems_test1() {
        want.collection(Arrays.asList("aaa", "bbb", "ccc")).hasAllItems("aaa");
        want.collection(Arrays.asList("aaa", "bbb", "ccc")).hasAllItems("aaa", "ccc");
        want.collection(Arrays.asList(1, 2, 4)).sizeEq(3).hasAllItems(1, 4);

        want.collection(Arrays.asList(1, 2, 4)).hasAllItems(1, 4).sizeLt(4);
    }

    @Test
    public void hasItems_test2() {
        want.exception(() ->
                        want.collection(Arrays.asList("aaa", "bbb", "ccc")).hasAllItems("aaad"),
                AssertionError.class);
    }

    @Test
    public void hasItems_test3() {
        want.exception(() ->
                        want.collection(Arrays.asList("aaa", "bbb", "ccc")).hasAllItems("aaa", "ccc", "dddd"),
                AssertionError.class);
    }

    @Test
    public void hasItems_test4() {
        want.exception(() ->
                        want.collection(Arrays.asList("aaa", "bbb", "ccc")).hasItems("aaac"),
                AssertionError.class);
    }

    @Test
    public void hasItems_test5() {
        want.exception(() ->
                        want.collection(Arrays.asList("aaa", "bbb", "ccc")).hasAllItems("aaad", "ccc"),
                AssertionError.class);
    }

    @Test
    public void hasItems_test6() {
        want.exception(() ->
                        want.collection(Arrays.asList(1, 2, 4)).hasAllItems(1, 5),
                AssertionError.class);
    }

    @Test
    public void hasItems_bool() {
        want.array(new boolean[]{true, true}).hasAllItems(true);
    }

    @Test
    public void hasItems_boolFailure() {
        want.exception(() ->
                        want.array(new boolean[]{true, true}).hasAllItems(false),
                AssertionError.class);
    }

    @Test
    public void hasItems_byte() {
        want.array(new byte[]{Byte.MAX_VALUE, Byte.MIN_VALUE}).hasItems(Byte.MAX_VALUE);
    }

    @Test
    public void hasItems_byteFail() {
        want.exception(() ->
                        want.array(new byte[]{Byte.MAX_VALUE, Byte.MAX_VALUE}).hasItems(Byte.MIN_VALUE),
                AssertionError.class);
    }

    @Test
    public void hasItems_char() {
        want.array(new char[]{'a', 'b'}).hasItems('a');
    }

    @Test
    public void hasItems_charFail() {
        want.exception(() ->
                        want.array(new char[]{'a', 'b'}).hasItems('c'),
                AssertionError.class);
    }

    @Test
    public void hasItems_short() {
        want.array(new short[]{1, 2}).hasItems(1);
    }

    @Test
    public void hasItems_shortFail() {
        want.exception(() ->
                        want.array(new short[]{1, 2}).hasItems(3),
                AssertionError.class);
    }

    @Test
    public void hasItems_long() {
        want.array(new long[]{1L, 2L}).hasItems(1L);
    }

    @Test
    public void hasItems_longFail() {
        want.exception(() ->
                        want.array(new long[]{1, 2}).hasItems(3L),
                AssertionError.class);
    }

    @Test
    public void hasItems_float() {
        want.array(new float[]{1f, 2f}).hasItems(1f);
    }

    @Test
    public void hasItems_floatFail() {
        want.exception(() ->
                        want.array(new float[]{1f, 2f}).hasItems(3f),
                AssertionError.class);
    }

    @Test
    public void hasItems_double() {
        want.array(new double[]{1d, 2d}).hasItems(1d);
    }

    @Test
    public void hasItems_doubleFail() {
        want.exception(() ->
                        want.array(new double[]{1d, 2d}).hasItems(3d),
                AssertionError.class);
    }
}
