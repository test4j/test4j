package org.test4j.hamcrest.iassert.object.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class ObjectContainerAssertTest_ItemMatch extends Test4J {
    private final List<String> list = new ArrayList<String>();

    @BeforeEach
    public void setup() {
        list.clear();
        list.add("test.hello.one");
        list.add("test.hello.two");
        list.add("test.hello.three");
    }

    @Test
    public void allItemMatch() {
        want.bool("test.hello.three".matches(".*hello.*")).is(true);

        want.collection(list).sizeIs(3)
                .allItemsMatchAll(the.string().regular("test.*"), the.string().regular(".*hello.*"));
    }

    @Test
    public void allItemMatch_2() {
        want.collection(list).sizeIs(3).allItemsMatchAll(the.string().regular(".*hello.*"));
    }

    @Test
    public void allItemMatch_3() {
        want.exception(() ->
                        want.collection(list).sizeIs(3)
                                .allItemsMatchAll(the.string().regular(".*hello.*"), the.string().regular("test1.*"))
                , AssertionError.class);
    }

    @Test
    public void hasItemMatch() {
        want.collection(list).sizeIs(3).anyItemsMatchAny(the.string().regular(".*one"), the.string().regular(".*two"));
    }

    @Test
    public void hasItemMatch_2() {
        want.collection(list).sizeIs(3).anyItemsMatchAll(the.string().regular(".*three"));
    }

    @Test
    public void hasItemMatch_3() {
        want.exception(() ->
                        want.collection(list).sizeIs(3).anyItemsMatchAll(the.string().regular(".*four"))
                , AssertionError.class);
    }

    @Test
    public void hasItemMatch_4() {
        want.exception(() ->
                        want.collection(list).sizeIs(3).anyItemsMatchAll(the.string().regular("test1"), the.string().regular(".*four"))
                , AssertionError.class);
    }

    @Test
    public void hasItemMatch_arraytest1() {
        want.exception(() ->
                        want.array(new String[]{"hello.one", "hello.two"}).sizeIs(2)
                                .anyItemsMatchAll(the.string().regular(".*one"), the.string().regular(".*two")),
                AssertionError.class);
    }

    @Test
    public void hasItemMatch_arraytest2() {
        want.exception(() ->
                        want.array(new String[]{"hello.one", "hello.two"}).sizeIs(2)
                                .anyItemsMatchAll(the.string().regular("test1"), the.string().regular(".*four"))
                , AssertionError.class);
    }

    @Test
    public void allItemMatch_arraytest1() {
        want.exception(() ->
                        want.array(new String[]{"hello.one", "heollo.two"}).sizeIs(2)
                                .allItemsMatchAll(the.string().regular("hello.*"))
                , AssertionError.class);
    }

    @Test
    public void allItemMatch_arraytest3() {
        want.array(new String[]{"hello.one", "hello.two"}).sizeIs(2)
                .allItemsMatchAll(the.string().regular("hello.*"));
    }

    @Test
    public void allItemMatch_arraytest2() {
        want.exception(() ->
                        want.array(new String[]{"hello.one", "hello.two"}).sizeIs(2).allItemsMatchAll(the.string().regular(".*one"))
                , AssertionError.class);
    }
}
