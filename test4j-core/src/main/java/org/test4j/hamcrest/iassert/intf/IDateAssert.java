package org.test4j.hamcrest.iassert.intf;

import org.test4j.hamcrest.iassert.interal.IBaseAssert;
import org.test4j.hamcrest.iassert.interal.IComparableAssert;

import java.util.Calendar;

/**
 * 日历类型对象断言接口
 *
 * @author darui.wudr
 */
public interface IDateAssert<T>
        extends
        IBaseAssert<T, IDateAssert<T>>,
        IComparableAssert<T, IDateAssert<T>> {
    /**
     * 和长整型时间比较
     *
     * @param time
     * @return 断言自身
     */
    IDateAssert<T> isEqualTo(long time);

    /**
     * 和calendar比较
     *
     * @param calendar
     * @return 断言自身
     */
    IDateAssert<T> isEqualTo(Calendar calendar);

    /**
     * 日历值的年等于期望值
     *
     * @param year 期望值
     * @return 断言自身
     */
    IDateAssert<T> isYear(int year);

    /**
     * 日历值的年等于期望值
     *
     * @param year 期望值
     * @return 断言自身
     */
    IDateAssert<T> isYear(String year);

    /**
     * 日历值的月份等于期望值
     *
     * @param month 期望值
     * @return 断言自身
     */
    IDateAssert<T> isMonth(int month);

    /**
     * 日历值的月份等于期望值
     *
     * @param month 期望值
     * @return 断言自身
     */
    IDateAssert<T> isMonth(String month);

    /**
     * 日历值的日期等于期望值
     *
     * @param day 期望值
     * @return 断言自身
     */
    IDateAssert<T> isDay(int day);

    /**
     * 日历值的日期等于期望值
     *
     * @param day 期望值
     * @return 断言自身
     */
    IDateAssert<T> isDay(String day);

    /**
     * 日历值的小时(24小时制)等于期望值
     *
     * @param hour 期望值
     * @return 断言自身
     */
    IDateAssert<T> isHour(int hour);

    /**
     * 日历值的小时(24小时制)等于期望值
     *
     * @param hour 期望值
     * @return 断言自身
     */
    IDateAssert<T> isHour(String hour);

    /**
     * 日历值的分钟等于期望值
     *
     * @param minute 期望值
     * @return 断言自身
     */
    IDateAssert<T> isMinute(int minute);

    /**
     * 日历值的分钟等于期望值
     *
     * @param minute 期望值
     * @return 断言自身
     */
    IDateAssert<T> isMinute(String minute);

    /**
     * 日历值的秒等于期望值
     *
     * @param second 期望值
     * @return 断言自身
     */
    IDateAssert<T> isSecond(int second);

    /**
     * 日历值的秒等于期望值
     *
     * @param second 期望值
     * @return 断言自身
     */
    IDateAssert<T> isSecond(String second);

    /**
     * 期望日期格式化字符串等于expected
     *
     * @param expected
     * @param format
     * @return 断言自身
     */
    IDateAssert<T> eqByFormat(String expected, String format);

    /**
     * 期望日期默认格式化字符串等于expected<br>
     * 默认格式为: yyyy-MM-dd 或 yyyy-MM-dd hh24:mm:ss
     *
     * @param expected
     * @return 断言自身
     */
    IDateAssert<T> eqByFormat(String expected);
}
