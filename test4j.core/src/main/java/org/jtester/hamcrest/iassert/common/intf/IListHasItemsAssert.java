package org.jtester.hamcrest.iassert.common.intf;

import org.jtester.hamcrest.matcher.modes.ItemsMode;
import org.jtester.hamcrest.matcher.modes.MatchMode;

import ext.jtester.hamcrest.Matcher;

/**
 * 数组或collection类型的对象容器断言
 * 
 * @author darui.wudr
 * 
 * @param <E>
 */

@SuppressWarnings("rawtypes")
public interface IListHasItemsAssert<E extends IAssert> {
	/**
	 * want: the collection or the array should contain all items listed by
	 * following arguments.
	 * 
	 * @param value
	 *            expected item
	 * @param values
	 *            other expected items
	 * @return
	 */
	E hasAllItems(Object value, Object... values);

	/**
	 * want: the collection or the array should contain item specified by
	 * argument.
	 * 
	 * @param value
	 * @return
	 */
	E hasItems(Object value);

	/**
	 * want: the collection or the array should contain item specified by
	 * argument.
	 * 
	 * @param item
	 * @param items
	 * @return
	 */
	E hasItems(Object item, Object... items);

	/**
	 * want: the collection or the array should contain any items listed by
	 * following arguments.
	 * 
	 * @param value
	 *            expected item
	 * @param values
	 *            other expected items
	 * @return
	 */
	E hasAnyItems(Object value, Object... values);

	/**
	 * all of items in collection(array) should match all of matchers specified
	 * by following arguments.<br>
	 * same as
	 * {@link #match(ItemsMode.AllItems, MatchMode.MatchAll, Matcher, Matcher...)}
	 * <br>
	 * 
	 * @param matcher
	 * @param matchers
	 * @return
	 */
	E allItemsMatchAll(Matcher matcher, Matcher... matchers);

	/**
	 * all of items in collection(array) should match any of matchers specified
	 * by following arguments.<br>
	 * same as
	 * {@link #match(ItemsMode.AllItems, MatchMode.MatchAny, Matcher, Matcher...)}
	 * 
	 * @param matcher
	 * @param matchers
	 * @return
	 */
	E allItemsMatchAny(Matcher matcher, Matcher... matchers);

	/**
	 * any of items in collection(array) should match all of matchers specified
	 * by following arguments.<br>
	 * same as
	 * {@link #match(ItemsMode.AnyItems, MatchMode.MatchAll, Matcher, Matcher...)}
	 * 
	 * @param matcher
	 * @param matchers
	 * @return
	 */
	E anyItemsMatchAll(Matcher matcher, Matcher... matchers);

	/**
	 * any of items in collection(array) should match any of matchers specified
	 * by following arguments.<br>
	 * same as
	 * {@link #match(ItemsMode.AnyItems, MatchMode.MatchAny, Matcher, Matcher...)}
	 * 
	 * @param matcher
	 * @param matchers
	 * @return
	 */
	E anyItemsMatchAny(Matcher matcher, Matcher... matchers);

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
	 * @return
	 */
	E match(ItemsMode itemsMode, MatchMode matchMode, Matcher matcher, Matcher... matchers);
}
