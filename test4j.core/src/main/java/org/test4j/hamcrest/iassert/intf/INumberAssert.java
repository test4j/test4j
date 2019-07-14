package org.test4j.hamcrest.iassert.intf;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.test4j.hamcrest.iassert.interal.IBaseAssert;
import org.test4j.hamcrest.iassert.interal.IComparableAssert;

/**
 * 数值型对象断言接口
 *
 * @param <T>
 * @param <E>
 * @author darui.wudr
 */
public interface INumberAssert<T extends Number & Comparable<T>, E extends INumberAssert<T, ?>>
        extends
        IBaseAssert<T, E>,
        IComparableAssert<T, E> {
    default E isEqualTo(Number expected) {
        Object _expected = getAssertObject().isNumberAndConvert(expected);
        Matcher<? super T> matcher = IsEqual.equalTo(_expected);
        return this.assertThat(matcher);
    }
}
