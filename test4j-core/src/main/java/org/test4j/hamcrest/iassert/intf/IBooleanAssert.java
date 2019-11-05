package org.test4j.hamcrest.iassert.intf;

import org.test4j.hamcrest.iassert.interal.IBaseAssert;

/**
 * 布尔值断言接口
 *
 * @author darui.wudr
 */
public interface IBooleanAssert
        extends IBaseAssert<Boolean, IBooleanAssert> {
    /**
     * 断言布尔值为bl
     *
     * @param bl
     * @return 断言自身
     */
    IBooleanAssert is(boolean bl);

    /**
     * 断言布尔值为bl
     *
     * @param description 描述
     * @param bl
     * @return 断言自身
     */
    IBooleanAssert is(String description, boolean bl);
}
