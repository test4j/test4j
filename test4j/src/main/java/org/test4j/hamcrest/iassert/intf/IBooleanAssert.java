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
     * @param bl
     * @return
     */
    IBooleanAssert is(boolean bl);

    IBooleanAssert is(String description, boolean bl);
}
