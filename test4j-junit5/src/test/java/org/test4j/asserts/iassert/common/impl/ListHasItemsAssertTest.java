package org.test4j.asserts.iassert.common.impl;

import org.junit.jupiter.api.Test;
import org.test4j.asserts.matcher.modes.ItemsMode;
import org.test4j.asserts.matcher.modes.MatchMode;
import org.test4j.junit5.Test4J;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ListHasItemsAssertTest extends Test4J {

    @Test
    public void testHasItems_objects() {
        want.collection(Arrays.asList("test1", "test2", "test3")).hasAllItems("test1", "test2");
    }

    @Test
    public void testHasAllItems_objects_failure() {
        want.exception(() ->
                        want.collection(Arrays.asList("test1", "test2", "test3")).hasAllItems("test1", "test4"),
                AssertionError.class);
    }

    @Test
    public void testHasAllItems_array() {
        want.collection(Arrays.asList("test1", "test2", "test3")).hasAllItems("test1", "test2");
    }

    @Test
    public void testHasAllItems_array_failure() {
        want.exception(() ->
                        want.collection(Arrays.asList("test1", "test2", "test3")).hasAllItems("test1", "test4"),
                AssertionError.class);
    }

    @Test
    public void testHasAllItems_collection() {
        want.collection(Arrays.asList("test1", "test2", "test3")).hasAllItems("test1", "test2");
    }

    @Test
    public void testHasAnyItems() {
        want.list(new int[]{2, 4, 5}).hasAnyItems(4, 6);
    }

    @Test
    public void testHasAnyItems_Failure() {
        want.exception(() ->
                        want.list(new int[]{2, 4, 5}).hasAnyItems(1, 6),
                AssertionError.class);
    }

    @Test
    public void testHasAllItems_collection_failure() {
        want.exception(() ->
                        want.collection(Arrays.asList("test1", "test2", "test3")).hasAllItems("test1", "test4"),
                AssertionError.class);
    }

    @Test
    public void testMatchAll_AllItems_MatchAll() {
        List list = Arrays.asList("test1", "test2", "test3");
        want.collection(list).allItemsMatchAll(the.string().regular("\\w{5}"), the.string().regular("test[\\d]"));
    }

    @Test
    public void testMatchAll_AllItems_MatchAll_Failure() {
        List list = Arrays.asList("test1", "test2", "test3");
        want.exception(() ->
                        want.collection(list).allItemsMatchAll(the.string().regular("\\w{5}"), the.string().regular("test2")),
                AssertionError.class);
    }

    @Test
    public void testMatchRegex_AnyItems_MatchAll() {
        List list = Arrays.asList("test1", "test2", "test3");
        want.collection(list).anyItemsMatchAll(the.string().regular("test1"), the.string().regular("test\\d"));
    }

    @Test
    public void testMatchAll_AnyItems_MatchAny() {
        List list = Arrays.asList("test1", "test2", "test3");
        want.collection(list).anyItemsMatchAll(the.string().regular("test.*"), the.string().regular("test2"));
    }

    @Test
    public void testMatchAll_AllItems_MatchAny() {
        List list = Arrays.asList("test1", "test2", "test3");
        want.collection(list).allItemsMatchAny(the.string().regular("test.*"), the.string().regular("test4"));
    }

    @Test
    public void testMatchAll_AnyItems_MatchAny_Failure() {
        List list = Arrays.asList("test1", "test2", "test3");
        want.exception(() ->
                        want.collection(list).allItemsMatchAny(the.string().regular("test1"), the.string().regular("test4")),
                AssertionError.class);
    }

    @Test
    public void testItemsAllMatch_And_Matcher() {
        want.array(new String[]{"ab345c", "ab345cd"}).match(ItemsMode.AllItems, MatchMode.MatchAll,
                the.string().regular("\\w+\\d+\\w+"));
    }

    @Test
    public void testItemsAllMatch_And_Matcher_Failure() {
        want.exception(() ->
                        want.array(new String[]{"ab345c", "abcd"}).match(ItemsMode.AllItems, MatchMode.MatchAll,
                                the.string().regular("\\w+\\d+\\w+")),
                AssertionError.class);
    }

    @Test
    public void testMatch_AnyItemMatcherAll() {
        want.array(new String[]{"abc", "ab345cd"}).match(ItemsMode.AnyItems, MatchMode.MatchAll,
                the.string().regular("\\w+\\d+\\w+"), the.string().isEqualTo("ab345cd"));
    }

    @Test
    public void testMatchAnyItem_OR_Matcher_Failure() {
        want.exception(() ->
                        want.array(new String[]{"ddd", "abcd"}).match(ItemsMode.AnyItems, MatchMode.MatchAll,
                                the.string().regular("\\w+\\d+\\w+")),
                AssertionError.class);
    }

    @Test
    public void testMatchAnyRegex() {
        String[] actual = new String[]{"test1", "test2"};
        want.list(actual).allItemsMatchAny(the.string().regular("xxxx"), the.string().regular("test\\d"));
    }

    @Test
    public void testMatchAnyRegex_Failure() {
        String[] actual = new String[]{"test1", "test2"};
        want.exception(() ->
                        want.list(actual).allItemsMatchAny(the.string().regular("txx"), the.string().regular(".*\\d{2}")),
                AssertionError.class);
    }

    @Test
    public void testGetItemsMatchers() {
        String[] list = new String[]{"test1", "test2", "test3"};
        want.list(list).match(ItemsMode.AnyItems, MatchMode.MatchAll, the.string().eq("test1"),
                the.string().start("test"), the.string().regular("\\w{4}\\d"));
    }

    @Test
    public void testMatch_AnyItemMatchAny() {
        String[] list = new String[]{"test1", "test2", "test3"};

        want.list(list).match(ItemsMode.AnyItems, MatchMode.MatchAny, the.string().eq("test1"),
                the.string().eq("test2"), the.string().eq("test4"));
    }

    @Test
    public void testMatchAllRegex() {
        String[] list = new String[]{"test1", "test2", "test3"};
        want.list(list).allItemsMatchAll(the.string().regular("\\w{4}\\d"), the.string().regular("test."),
                the.string().regular(".{4}[123]"));
    }
}
