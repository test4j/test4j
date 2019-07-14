package org.test4j.hamcrest.iassert.intf;

import org.test4j.hamcrest.iassert.interal.IBaseAssert;
import org.test4j.hamcrest.iassert.interal.ICharAssert;
import org.test4j.hamcrest.iassert.interal.IComparableAssert;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * 字符串对象断言接口
 *
 * @author darui.wudr
 */
public interface IStringAssert
        extends
        IBaseAssert<String, IStringAssert>,
        IComparableAssert<String, IStringAssert>,
        ICharAssert<String, IStringAssert> {

    /**
     * 断言字符串符合正则表达式${regex}
     *
     * @param regex 期望的正则表达式
     * @return
     */
    IStringAssert regular(String regex);

    /**
     * 断言子字符串sub出现times次
     *
     * @param times
     * @param sub
     * @return
     */
    IStringAssert occurs(int times, String sub);
}
