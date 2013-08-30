package org.jtester.hamcrest.iassert.object.intf;

import org.jtester.hamcrest.iassert.common.intf.IBaseAssert;
import org.jtester.hamcrest.iassert.common.intf.IReflectionAssert;

/**
 * 通用对象断言接口
 * 
 * @author darui.wudr
 * 
 */
public interface IObjectAssert extends IBaseAssert<Object, IObjectAssert>, IReflectionAssert<IObjectAssert> {

}
