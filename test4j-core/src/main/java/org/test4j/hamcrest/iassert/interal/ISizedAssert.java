package org.test4j.hamcrest.iassert.interal;

import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.test4j.hamcrest.matcher.array.SizeOrLengthMatcher;

@SuppressWarnings("rawtypes")
public interface ISizedAssert<T, E extends IAssert> extends IAssert<T, E> {
    /**
     * want: the size of map(collection/array) item is equal to the number size.<br>
     * same as {@link #sizeEq(int)} <br>
     * <br>
     * 数组长度或collection中元素个数等于期望值
     *
     * @param size 期望值
     * @return 断言自身
     */
    default E sizeIs(int size) {
        SizeOrLengthMatcher matcher = new SizeOrLengthMatcher(size, SizeOrLengthMatcher.SizeOrLengthMatcherType.EQ);
        return this.assertThat(matcher);
    }

    /**
     * want: the size of map(collection/array) item is equal to the number size.<br>
     * same as {@link #sizeIs(int)}<br>
     * <br>
     * 数组长度或collection中元素个数等于期望值
     *
     * @param size 期望值
     * @return 断言自身
     */
    default E sizeEq(int size) {
        SizeOrLengthMatcher matcher = new SizeOrLengthMatcher(size, SizeOrLengthMatcher.SizeOrLengthMatcherType.EQ);
        return this.assertThat(matcher);
    }

    /**
     * want: the size of map(collection/array) item is greater than the number
     * size.<br>
     * <br>
     * 数组长度或collection中元素个数大于期望值
     *
     * @param size 期望值
     * @return 断言自身
     */
    default E sizeGt(int size) {
        SizeOrLengthMatcher matcher = new SizeOrLengthMatcher(size, SizeOrLengthMatcher.SizeOrLengthMatcherType.GT);
        return this.assertThat(matcher);
    }

    /**
     * want: the size of map(collection/array) item is greater than or equal to
     * the number size.<br>
     * <br>
     * 数组长度或collection中元素个数大于等于期望值
     *
     * @param size 期望值
     * @return 断言自身
     */
    default E sizeGe(int size) {
        SizeOrLengthMatcher matcher = new SizeOrLengthMatcher(size, SizeOrLengthMatcher.SizeOrLengthMatcherType.GE);
        return this.assertThat(matcher);
    }

    /**
     * want: the size of map(collection/array) item is less than the number
     * size.<br>
     * <br>
     * 数组长度或collection中元素个数小于期望值
     *
     * @param size 期望值
     * @return 断言自身
     */
    default E sizeLt(int size) {
        SizeOrLengthMatcher matcher = new SizeOrLengthMatcher(size, SizeOrLengthMatcher.SizeOrLengthMatcherType.LT);
        return this.assertThat(matcher);
    }

    /**
     * want: the size of map(collection/array) item is less than or equal to the
     * number size.<br>
     * <br>
     * 数组长度或collection中元素个数小于等于期望值
     *
     * @param size 期望值
     * @return 断言自身
     */
    default E sizeLe(int size) {
        SizeOrLengthMatcher matcher = new SizeOrLengthMatcher(size, SizeOrLengthMatcher.SizeOrLengthMatcherType.LE);
        return this.assertThat(matcher);
    }

    /**
     * want: the size of map(collection/array) item is greater than or equal to
     * the number min, and is less than or equal to the number max.<br>
     * <br>
     *
     * @param min
     * @param max
     * @return 断言自身
     */
    default E sizeBetween(int min, int max) {
        SizeOrLengthMatcher geMatcher = new SizeOrLengthMatcher(min, SizeOrLengthMatcher.SizeOrLengthMatcherType.GE);
        SizeOrLengthMatcher leMatcher = new SizeOrLengthMatcher(max, SizeOrLengthMatcher.SizeOrLengthMatcherType.LE);
        Matcher matcher = AllOf.allOf(geMatcher, leMatcher);
        return this.assertThat(matcher);
    }

    /**
     * want: the size of map(collection/array) item isn't equal to the number
     * size.<br>
     * <br>
     * 数组长度或collection中元素个数不等于期望值
     *
     * @param size 期望值
     * @return 断言自身
     */
    default E sizeNe(int size) {
        SizeOrLengthMatcher matcher = new SizeOrLengthMatcher(size, SizeOrLengthMatcher.SizeOrLengthMatcherType.NE);
        return this.assertThat(matcher);
    }
}
