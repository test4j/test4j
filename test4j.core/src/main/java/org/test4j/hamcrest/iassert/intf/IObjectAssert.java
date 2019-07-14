package org.test4j.hamcrest.iassert.intf;

import org.test4j.hamcrest.iassert.interal.IBaseAssert;
import org.test4j.hamcrest.iassert.interal.IReflectionAssert;

/**
 * 通用对象断言接口
 *
 * @author darui.wudr
 */
public interface IObjectAssert
        extends IBaseAssert<Object, IObjectAssert>,
        IReflectionAssert<Object, IObjectAssert> {

}
