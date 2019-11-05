package org.test4j.hamcrest.iassert.interal;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsArrayContaining;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.IsCollectionContaining;
import org.test4j.hamcrest.matcher.modes.ItemsMode;
import org.test4j.hamcrest.matcher.modes.MatchMode;

import java.util.List;


/**
 * 数组或collection类型的对象容器断言
 *
 * @param <E>
 * @author darui.wudr
 */
@SuppressWarnings("rawtypes")
public interface IListHasItemsAssert<T, E extends IAssert> extends IAssert<T, E> {
    /**
     * want: the collection or the array should contain all items listed by
     * following arguments.
     *
     * @param item  expected item
     * @param items other expected items
     * @return 断言自身
     */
    default E hasAllItems(Object item, Object... items) {
        this.getAssertObject().assertTargetClassNotNull();
        boolean isArr = this.getAssertObject().valueIsArray();
        List<Matcher<? super T[]>> list = MatcherHelper.getHasItemMatchers(isArr, item, items);
        Matcher matcher = AllOf.allOf(list);
        return this.assertThat(matcher);
    }

    /**
     * want: the collection or the array should contain item specified by
     * argument.
     *
     * @param value
     * @return 断言自身
     */
    default E hasItems(Object value) {
        this.getAssertObject().assertTargetClassNotNull();
        if (this.getAssertObject().valueIsArray()) {
            return this.assertThat(IsArrayContaining.hasItemInArray(value));
        } else {
            return this.assertThat(IsCollectionContaining.hasItem(value));
        }
    }

    /**
     * want: the collection or the array should contain item specified by
     * argument.
     *
     * @param item
     * @param items
     * @return 断言自身
     */
    default E hasItems(Object item, Object... items) {
        return this.hasAllItems(item, items);
    }

    /**
     * want: the collection or the array should contain any items listed by
     * following arguments.
     *
     * @param item  expected item
     * @param items other expected items
     * @return 断言自身
     */
    default E hasAnyItems(Object item, Object... items) {
        this.getAssertObject().assertTargetClassNotNull();
        boolean isArr = this.getAssertObject().valueIsArray();
        List<Matcher<? super T[]>> list = MatcherHelper.getHasItemMatchers(isArr, item, items);
        Matcher matcher = AnyOf.anyOf(list);
        return this.assertThat(matcher);
    }

    /**
     * all of items in collection(array) should match all of matchers specified
     * by following arguments.
     *
     * @param matcher
     * @param matchers
     * @return 断言自身
     */
    default E allItemsMatchAll(Matcher matcher, Matcher... matchers) {
        List<Matcher<? super T>> list = MatcherHelper.getItemsMatchers(ItemsMode.AllItems, matcher, matchers);
        return this.assertThat(AllOf.allOf(list));
    }

    /**
     * all of items in collection(array) should match any of matchers specified
     * by following arguments.
     *
     * @param matcher
     * @param matchers
     * @return 断言自身
     */
    default E allItemsMatchAny(Matcher matcher, Matcher... matchers) {
        List<Matcher<? super T>> list = MatcherHelper.getItemsMatchers(ItemsMode.AllItems, matcher, matchers);
        return this.assertThat(AnyOf.anyOf(list));
    }

    /**
     * any of items in collection(array) should match all of matchers specified
     * by following arguments.
     *
     * @param matcher
     * @param matchers
     * @return 断言自身
     */
    default E anyItemsMatchAll(Matcher matcher, Matcher... matchers) {
        Matcher m = MatcherHelper.getAnyItemsMatchAll(matcher, matchers);
        return this.assertThat(m);
    }

    /**
     * any of items in collection(array) should match any of matchers specified
     * by following arguments.
     *
     * @param matcher
     * @param matchers
     * @return 断言自身
     */
    default E anyItemsMatchAny(Matcher matcher, Matcher... matchers) {
        List<Matcher<? super T>> list = MatcherHelper.getItemsMatchers(ItemsMode.AnyItems, matcher, matchers);
        return this.assertThat(AnyOf.anyOf(list));
    }

    /**
     * all of any(specified by {@link ItemsMode}) items(properties) should match
     * all or any(specified by {@link MatchMode}) matchers.<br>
     * when itemsMode==AllItems && matchMode==MatchAll, same as
     * {@link #allItemsMatchAll(Matcher, Matcher...)}<br>
     * when itemsMode==AllItems && matchMode==MatchAny, same as
     * {@link #allItemsMatchAny(Matcher, Matcher...)}<br>
     * when itemsMode==AnyItems && matchMode==MatchAll, same as
     * {@link #anyItemsMatchAll(Matcher, Matcher...)}<br>
     * when itemsMode==AnyItems && matchMode==MatchAny, same as
     * {@link #anyItemsMatchAny(Matcher, Matcher...)}<br>
     *
     * <br>
     * 数组中所有或任一（由ItemsMode决定）元素必须和下列全部或任一（由MatchMode决定）Matcher匹配。
     *
     * @param itemsMode
     * @param matchMode
     * @param matcher
     * @param matchers
     * @return 断言自身
     */
    default E match(ItemsMode itemsMode, MatchMode matchMode, Matcher matcher, Matcher... matchers) {
        if (itemsMode == ItemsMode.AllItems && matchMode == MatchMode.MatchAll) {
            return this.allItemsMatchAll(matcher, matchers);
        } else if (itemsMode == ItemsMode.AllItems && matchMode == MatchMode.MatchAny) {
            return this.allItemsMatchAny(matcher, matchers);
        } else if (itemsMode == ItemsMode.AnyItems && matchMode == MatchMode.MatchAll) {
            return this.anyItemsMatchAll(matcher, matchers);
        } else if (itemsMode == ItemsMode.AnyItems && matchMode == MatchMode.MatchAny) {
            return this.anyItemsMatchAny(matcher, matchers);
        } else {
            throw new RuntimeException("the arguments[ItmesMode and MatchMode] of items match API can't be null.");
        }
    }
}
