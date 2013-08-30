package org.jtester.hamcrest.iassert.common.intf;

import ext.jtester.hamcrest.Matcher;

/**
 * jtester断言超基类接口
 * 
 * @author darui.wudr
 * 
 * @param <T>
 * @param <E>
 */
@SuppressWarnings("rawtypes")
public interface IAssert<T, E extends IAssert> extends Matcher<T> {
	/**
	 * 用于jmock expected参数断言的结尾<br>
	 * 结束断言返回一个符合参数类型的值以满足编译
	 * 
	 * 
	 * @return 返回参数类型的默认值
	 */
	T wanted();

	/**
	 * 用于jmock expected参数断言的结尾<br>
	 * 结束断言返回一个符合参数类型的值以满足编译
	 * 
	 * @param <F>
	 * @param claz
	 * @return
	 */
	<F> F wanted(Class<F> claz);
}
