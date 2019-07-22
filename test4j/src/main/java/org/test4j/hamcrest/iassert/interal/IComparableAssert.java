package org.test4j.hamcrest.iassert.interal;

import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.test4j.hamcrest.matcher.mockito.GreaterOrEqual;
import org.test4j.hamcrest.matcher.mockito.GreaterThan;
import org.test4j.hamcrest.matcher.mockito.LessOrEqual;
import org.test4j.hamcrest.matcher.mockito.LessThan;

/**
 * 类型值可以比较大小的断言
 *
 * @param <E>
 * @author darui.wudr
 * <p>
 * T extends Comparable<T>
 */
@SuppressWarnings("rawtypes")
public interface IComparableAssert<T, E extends IComparableAssert> extends IAssert<T, E> {

    /**
     * want the actual number is less than the expected number.<br>
     * 断言对象小于期望值max
     *
     * @param max 期望最大值
     * @return 断言自身
     */
    default E isLt(T max) {
        MatcherHelper.assetCanComparable(max);
        LessThan matcher = new LessThan((Comparable) max);
        return this.assertThat(matcher);
    }

    /**
     * want the actual number is less than or equal to the expected number.<br>
     * 断言对象小于等于期望值max
     *
     * @param max 期望最大值
     * @return 断言自身
     */
    default E isLe(T max) {
        MatcherHelper.assetCanComparable(max);
        LessOrEqual matcher = new LessOrEqual((Comparable) max);
        return this.assertThat(matcher);
    }

    /**
     * want the actual number is greater than the expected number.<br>
     * 断言对象大于期望值min
     *
     * @param min 期望最小值
     * @return 断言自身
     */
    default E isGt(T min) {
        MatcherHelper.assetCanComparable(min);
        GreaterThan matcher = new GreaterThan((Comparable) min);
        return this.assertThat(matcher);
    }

    /**
     * want the actual number is greater than or equal to the expected number.<br>
     * <p>
     * 断言对象大于等于期望值min
     *
     * @param min 期望最小值
     * @return 断言自身
     */
    default E isGe(T min) {
        MatcherHelper.assetCanComparable(min);
        GreaterOrEqual matcher = new GreaterOrEqual((Comparable) min);
        return this.assertThat(matcher);
    }

    /**
     * 断言对象在最小值和最大值之间(包括最大值和最小值)
     *
     * @param min 期望最小值
     * @param max 期望最大值
     * @return 断言自身
     */
    default E isBetween(T min, T max) {
        MatcherHelper.assetCanComparable(min);
        MatcherHelper.assetCanComparable(max);
        if (((Comparable) min).compareTo((Comparable) max) > 0) {
            throw new AssertionError(String.format("arg1[%s] must less than arg2[%s]", min, max));
        }
        GreaterOrEqual geq = new GreaterOrEqual((Comparable) min);
        LessOrEqual leq = new LessOrEqual((Comparable) max);
        Matcher<?> matcher = AllOf.allOf(geq, leq);
        return this.assertThat(matcher);
    }

    /**
     * want the actual number is less than the expected number.<br>
     * same as {@link #isLt(Object)}
     *
     * @param max
     * @return 断言自身
     */
    default E isLessThan(T max) {
        return this.isLt(max);
    }

    /**
     * want the actual number is less than or equal to the expected number.<br>
     * same as {@link #isLe(Object)}
     *
     * @param max
     * @return 断言自身
     */
    default E isLessEqual(T max) {
        return this.isLe(max);
    }

    /**
     * want the actual number is greater than the expected number.<br>
     * same as {@link #isGt(Object)}
     *
     * @param min
     * @return 断言自身
     */
    default E isGreaterThan(T min) {
        return this.isGt(min);
    }

    /**
     * want the actual number is greater than or equal to the expected number.<br>
     * same as {@link #isGe(Object)}
     *
     * @param min
     * @return 断言自身
     */
    default E isGreaterEqual(T min) {
        return this.isGe(min);
    }
}
