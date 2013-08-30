package org.jtester.hamcrest.iassert.common.intf;

import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.module.ICore.DataMap;

import ext.jtester.hamcrest.Matcher;

/**
 * 针对对象属性进行断言
 * 
 * @author darui.wudr
 * 
 * @param <E>
 */

@SuppressWarnings("rawtypes")
public interface IReflectionAssert<E extends IAssert> {
	/**
	 * 断言对象指定的属性(property)值等于期望值<br>
	 * same as "eqByProperty(String, Object)"
	 * 
	 * @param property
	 *            对象属性名称
	 * @param expected
	 *            期望值
	 * @return
	 */
	E propertyEq(String property, Object expected, EqMode... modes);

	/**
	 * 断言对象指定的属性(property)值等于期望值字符串
	 * 
	 * @param property
	 * @param expected
	 * @param modes
	 * @return
	 */
	E propertyEq(String property, String expected, StringMode mode, StringMode... more);

	/**
	 * 断言对象的多个属性相等<br>
	 * same as "eqByProperties(String[],Object)"
	 * 
	 * @param properties
	 * @param expected
	 * @return
	 */
	E propertyEq(String[] properties, Object expected, EqMode... modes);

	/**
	 * 对象的属性值符合指定的断言器要求
	 * 
	 * @param property
	 * @param expected
	 * @return
	 */
	E propertyMatch(String property, Matcher matcher);

	/**
	 * Same as eqByReflect, will be @Deprecated in the future.
	 * 
	 * @See {@link #eqByReflect(Object, EqMode...)}
	 */
	E reflectionEq(Object expected, EqMode... modes);

	/**
	 * 通过反射比较实际值和期望值的属性时，二者的属性是相同的<br>
	 * 属性如果是复杂对象，递归反射比较
	 * 
	 * @param expected
	 *            期望对象
	 * @param modes
	 *            比较模式,详见org.jtester.hamcrest.matcher.property.reflection.EqMode
	 * @return
	 */
	E eqByReflect(Object expected, EqMode... modes);

	/**
	 * 在忽略期望值是null或默认值的情况下二者是相等的
	 * 
	 * @param expected
	 * @return
	 */
	E eqIgnoreDefault(Object expected);

	/**
	 * 在忽略顺序，默认值，日期的情况下二者是相等的
	 * 
	 * @param expected
	 * @return
	 */
	E eqIgnoreAll(Object expected);

	/**
	 * 在忽略元素顺序情况下二者是相等的
	 * 
	 * @param expected
	 * @return
	 */
	E eqIgnoreOrder(Object expected);

	/**
	 * Same as propertyEqMap, will be @deprecated in the future.
	 * 
	 * @See {@link #propertyEqMap(DataMap, EqMode...)}
	 * 
	 * @param expected
	 * @return
	 */
	E reflectionEqMap(DataMap expected, EqMode... modes);

	/**
	 * 把实际对象按照Map中的key值取出来，进行反射比较<br>
	 * 如果对象的属性不在Map中，则不进行比较<br>
	 * 功能和
	 * "propertyEq(String[] properties, Object expected, EqMode... modes)"类似，
	 * 无非这里是把属性写到了Map中，方便一一对应
	 * 
	 * @param expected
	 * @return
	 */
	E propertyEqMap(DataMap expected, EqMode... modes);
}
