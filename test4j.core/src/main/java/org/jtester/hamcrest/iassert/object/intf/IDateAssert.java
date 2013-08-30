package org.jtester.hamcrest.iassert.object.intf;

import java.util.Calendar;

import org.jtester.hamcrest.iassert.common.intf.IBaseAssert;
import org.jtester.hamcrest.iassert.common.intf.IComparableAssert;

/**
 * 日历类型对象断言接口
 * 
 * @author darui.wudr
 * 
 */
public interface IDateAssert<T> extends IBaseAssert<T, IDateAssert<T>>, IComparableAssert<T, IDateAssert<T>> {
	/**
	 * 和长整型时间比较
	 * 
	 * @param time
	 * @return
	 */
	IDateAssert<T> isEqualTo(long time);

	/**
	 * 和calendar比较
	 * 
	 * @param time
	 * @return
	 */
	IDateAssert<T> isEqualTo(Calendar calendar);

	/**
	 * 日历值的年等于期望值
	 * 
	 * @param year
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isYear(int year);

	/**
	 * 日历值的年等于期望值
	 * 
	 * @param year
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isYear(String year);

	/**
	 * 日历值的月份等于期望值
	 * 
	 * @param month
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isMonth(int month);

	/**
	 * 日历值的月份等于期望值
	 * 
	 * @param month
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isMonth(String month);

	/**
	 * 日历值的日期等于期望值
	 * 
	 * @param day
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isDay(int day);

	/**
	 * 日历值的日期等于期望值
	 * 
	 * @param day
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isDay(String day);

	/**
	 * 日历值的小时(24小时制)等于期望值
	 * 
	 * @param hour
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isHour(int hour);

	/**
	 * 日历值的小时(24小时制)等于期望值
	 * 
	 * @param hour
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isHour(String hour);

	/**
	 * 日历值的分钟等于期望值
	 * 
	 * @param minute
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isMinute(int minute);

	/**
	 * 日历值的分钟等于期望值
	 * 
	 * @param minute
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isMinute(String minute);

	/**
	 * 日历值的秒等于期望值
	 * 
	 * @param second
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isSecond(int second);

	/**
	 * 日历值的秒等于期望值
	 * 
	 * @param second
	 *            期望值
	 * @return
	 */
	IDateAssert<T> isSecond(String second);

	/**
	 * 期望日期格式化字符串等于expected
	 * 
	 * @param expected
	 * @param format
	 * @return
	 */
	IDateAssert<T> eqByFormat(String expected, String format);

	/**
	 * 期望日期默认格式化字符串等于expected<br>
	 * 默认格式为: yyyy-MM-dd 或 yyyy-MM-dd hh24:mm:ss
	 * 
	 * @param expected
	 * @return
	 */
	IDateAssert<T> eqByFormat(String expected);
}
