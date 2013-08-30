package org.jtester.hamcrest.iassert.common.intf;

import java.util.List;

import org.jtester.hamcrest.matcher.modes.ItemsMode;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.module.ICore.DataMap;

import ext.jtester.hamcrest.Matcher;

/**
 * 
 * @author darui.wudr
 * 
 * @param <T>
 * @param <E>
 */
@SuppressWarnings("rawtypes")
public interface IListAssert<T, E extends IAssert> extends IAssert<T, E>, ISizedAssert<E> {
	/**
	 * want: the actual value should be equals to the expected value specified
	 * by argument.
	 * 
	 * @param expected
	 * @param modes
	 * @return
	 */
	E isEqualTo(Object expected, EqMode... modes);

	/**
	 * the property value of all/any(specified by ItemsMode) items in
	 * collection(array) should be matched by the matcher.
	 * 
	 * @param itemsMode
	 *            All or Any Items in Collection or Array.
	 * @param property
	 *            the property name of items in collection or array.
	 * @param matcher
	 *            Hamcrest Matcher
	 * @return
	 */
	E propertyMatch(ItemsMode itemsMode, String property, Matcher matcher);

	/**
	 * want: the actual collection(array) and the expected collection(array)
	 * they are equals when ignoring the items's order in both of them.<br>
	 * <br>
	 * 忽略顺序的情况下二者相等
	 * 
	 * @param expected
	 * @return
	 */
	E eqIgnoreOrder(Object expected);

	E reflectionEqMap(List<DataMap> expected, EqMode... modes);

	/**
	 * will be @deprecated in the future.<br>
	 * same as {@link #propertyEqMap(int, DataMap, EqMode...)}
	 * 
	 * @param count
	 * @param expected
	 * @param modes
	 * @return
	 */
	E reflectionEqMap(int count, DataMap expected, EqMode... modes);

	/**
	 * want: the value of properties specified by DataMap's keys of items in
	 * collection(array) should be equal to the value of corresponding key in
	 * DataMap.<br>
	 * If the property of items in collection doesn't be specified by DataMap,
	 * that willn't be compared. <br>
	 * <br>
	 * 把实际对象按照Map中的key值取出来，进行反射比较<br>
	 * 如果对象的属性不在Map中，则不进行比较<br>
	 * 
	 * @param count
	 * @param expected
	 * @param modes
	 * @return
	 */
	E propertyEqMap(int count, DataMap expected, EqMode... modes);
}
