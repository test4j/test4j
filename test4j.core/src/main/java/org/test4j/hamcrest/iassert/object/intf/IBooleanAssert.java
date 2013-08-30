package org.test4j.hamcrest.iassert.object.intf;

import org.test4j.hamcrest.iassert.common.intf.IBaseAssert;

/**
 * 布尔值断言接口
 * 
 * @author darui.wudr
 * 
 */
public interface IBooleanAssert extends IBaseAssert<Boolean, IBooleanAssert> {
	IBooleanAssert is(boolean bl);

	IBooleanAssert is(String description, boolean bl);
}
