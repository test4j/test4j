package org.jtester.hamcrest.iassert.common.intf;

/**
 * 类型值可以比较大小的断言
 * 
 * @author darui.wudr
 * 
 *         T extends Comparable<T>
 * @param <E>
 */
@SuppressWarnings("rawtypes")
public interface IComparableAssert<T, E extends IAssert> {

	/**
	 * want the actual number is less than the expected number.<br>
	 * 断言对象小于期望值max
	 * 
	 * @param max
	 *            期望最大值
	 * @return
	 */
	E isLt(T max);

	/**
	 * want the actual number is less than or equal to the expected number.<br>
	 * 断言对象小于等于期望值max
	 * 
	 * @param max
	 *            期望最大值
	 * @return
	 */
	E isLe(T max);

	/**
	 * want the actual number is greater than the expected number.<br>
	 * 断言对象大于期望值min
	 * 
	 * @param min
	 *            期望最小值
	 * @return
	 */
	E isGt(T min);

	/**
	 * want the actual number is greater than or equal to the expected number.<br>
	 * 
	 * 断言对象大于等于期望值min
	 * 
	 * @param min
	 *            期望最小值
	 * @return
	 */
	E isGe(T min);

	/**
	 * 断言对象在最小值和最大值之间(包括最大值和最小值)
	 * 
	 * @param min
	 *            期望最小值
	 * @param max
	 *            期望最大值
	 * @return
	 */
	E isBetween(T min, T max);

	/**
	 * want the actual number is less than the expected number.<br>
	 * same as {@link #isLt(Object)}
	 */
	E isLessThan(T max);

	/**
	 * want the actual number is less than or equal to the expected number.<br>
	 * same as {@link #isLe(Object)}
	 */
	E isLessEqual(T max);

	/**
	 * want the actual number is greater than the expected number.<br>
	 * same as {@link #isGt(Object)}
	 */
	E isGreaterThan(T min);

	/**
	 * want the actual number is greater than or equal to the expected number.<br>
	 * same as {@link #isGe(Object)}
	 */
	E isGreaterEqual(T min);
}
