package org.test4j.hamcrest.iassert.object.intf;

import org.test4j.hamcrest.iassert.common.intf.IBaseAssert;
import org.test4j.hamcrest.iassert.common.intf.IReflectionAssert;

/**
 * 通用对象断言接口
 * 
 * @author darui.wudr
 * 
 */
public interface IObjectAssert extends IBaseAssert<Object, IObjectAssert>, IReflectionAssert<IObjectAssert> {

}
