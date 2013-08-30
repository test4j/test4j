package org.jtester.hamcrest.iassert.common.intf;

import org.jtester.hamcrest.iassert.object.intf.IStringAssert;

import ext.jtester.hamcrest.Matcher;

/**
 * the basic asserting matcher
 * 
 * @author darui.wudr
 * 
 * @param <T>
 * @param <E>
 */
@SuppressWarnings("rawtypes")
public interface IBaseAssert<T, E extends IAssert> extends Matcher<T>, IAssert<T, E> {
	/**
	 * 对象的toString等于期望值
	 * 
	 * @param expected
	 * @return
	 */
	E eqToString(String expected);

	/**
	 * 对象的toString符合断言器判断
	 * 
	 * @param matcher
	 * @return
	 */
	E eqToString(IStringAssert matcher);

	/**
	 * 断言对象等于期望的值<br>
	 * same as method "isEqualTo(T)"
	 * 
	 * @param expected
	 *            期望值
	 * @return
	 */
	E eq(T expected);

	/**
	 * 断言对象等于期望的值<br>
	 * same as method "eq(T)"
	 * 
	 * @param expected
	 *            期望值
	 * @return
	 */
	E isEqualTo(T expected);

	/**
	 * 断言对象等于期望的值
	 * 
	 * @param message
	 *            错误信息
	 * @param expected
	 *            期望值
	 * @return
	 */
	E isEqualTo(String message, T expected);

	/**
	 * 断言对象不等于期望的值
	 * 
	 * @param expected
	 *            期望值
	 * @return
	 */
	E notEqualTo(T expected);

	/**
	 * 断言对象可以在期望值里面找到
	 * 
	 * @param values
	 *            期望值
	 * @return
	 */
	E in(T... values);

	/**
	 * 断言对象不可以在期望值里面找到
	 * 
	 * @param values
	 *            期望值
	 * @return
	 */
	E notIn(T... values);

	/**
	 * 断言对象的类型等于期望类型
	 * 
	 * @param claz
	 *            期望类型
	 * @return
	 */
	E clazIs(Class claz);

	/**
	 * 断言对象的类型是期望类型的子类
	 * 
	 * @param claz
	 * @return
	 */
	E clazIsSubFrom(Class claz);

	/**
	 * 断言对象符合任一个对象行为定义<br>
	 * same as "matchAny(...)"
	 * 
	 * @param matcher1
	 *            对象行为定义，具体定义参见 ext.jtester.hamcrest.Matcher
	 * @param matcher2
	 * @param matchers
	 * @return
	 */
	E any(E matcher, E... matchers);

	/**
	 * 断言对象符合所有的对象行为定义<br>
	 * same as "matchAll(Matcher...)"
	 * 
	 * @param matcher
	 * @param matchers
	 * @return
	 */
	E all(E matcher, E... matchers);

	/**
	 * 断言对象不符合matcher所定义的行为
	 * 
	 * @param matcher
	 *            对象行为定义，具体定义参见 ext.jtester.hamcrest.Matcher
	 * @return
	 */
	E not(E matcher);

	/**
	 * 没有一个断言器可以匹配实际对象，即每一个断言器都匹配失败
	 * 
	 * @param matcher
	 * @param matchers
	 * @return
	 */
	E notAny(Matcher matcher, Matcher... matchers);

	/**
	 * 不是所有的断言器都可以匹配实际对象，即至少有一个断言器失败
	 * 
	 * @param matcher
	 * @param matchers
	 * @return
	 */
	E notAll(Matcher matcher, Matcher... matchers);

	/**
	 * 断言对象和期望值是同一个对象
	 * 
	 * @param value
	 *            期望值
	 * @return
	 */
	E same(T value);

	/**
	 * 断言对象可以使任意的值
	 * 
	 * @return
	 */
	E any();

	/**
	 * 断言对象值等于null
	 * 
	 * @return
	 */
	E isNull();

	/**
	 * 断言对象值等于null
	 * 
	 * @return
	 */
	E isNull(String message);

	/**
	 * 断言对象值不等于null
	 * 
	 * @return
	 */
	E notNull();

	/**
	 * 断言对象值不等于null
	 * 
	 * @return
	 */
	E notNull(String message);
}
