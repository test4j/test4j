package org.test4j.hamcrest.iassert.interal;


import org.hamcrest.Matcher;
import org.hamcrest.collection.IsArrayContaining;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsCollectionContaining;
import org.test4j.hamcrest.matcher.array.ListEveryItemMatcher;
import org.test4j.hamcrest.matcher.modes.ItemsMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MatcherHelper {
    public static <T> List<Matcher<? super T[]>> getHasItemMatchers(boolean isArr, Object item, Object... items) {
        List list = new ArrayList();
        list.add(buildArrayMatcher(isArr, item));

        if (items != null) {
            Arrays.stream(items)
                    .map(_item -> buildArrayMatcher(isArr, _item))
                    .forEach(list::add);
        }
        return list;
    }

    private static <T> Matcher buildArrayMatcher(boolean isArr, Object item) {
        return isArr ?
                IsArrayContaining.hasItemInArray(item) :
                IsCollectionContaining.hasItem(item);
    }

    public static <T> List<Matcher<? super T>> getItemsMatchers(ItemsMode itemsMode, Matcher matcher, Matcher... matchers) {
        List<Matcher<? super T>> list = new ArrayList<>();

        list.add(new ListEveryItemMatcher(matcher, itemsMode));
        if (matchers != null && matchers.length > 0) {
            Arrays.stream(matchers)
                    .forEach(m -> list.add(new ListEveryItemMatcher(m, itemsMode)));
        }
        return list;
    }

    public static <T> Matcher getAnyItemsMatchAll(Matcher matcher, Matcher... matchers) {
        List<Matcher<? super T>> list = new ArrayList();
        list.add(matcher);
        for (Matcher m : matchers) {
            list.add(m);
        }
        Matcher allItems = AllOf.allOf(list);
        return new ListEveryItemMatcher(allItems, ItemsMode.AnyItems);
    }


    public static <T> void assetCanComparable(T o) {
        if (o != null && !(o instanceof Comparable)) {
            throw new AssertionError("the object[" + o + "] isn't a comparable object.");
        }
    }
}
