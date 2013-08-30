package org.test4j.hamcrest.iassert.object.intf;

import org.test4j.hamcrest.iassert.common.intf.IBaseAssert;
import org.test4j.hamcrest.iassert.common.intf.IListAssert;
import org.test4j.hamcrest.iassert.common.intf.IListHasItemsAssert;
import org.test4j.hamcrest.iassert.common.intf.IReflectionAssert;

/**
 *数组对象断言接口
 * 
 * @author darui.wudr
 * 
 */
public interface IArrayAssert extends IBaseAssert<Object[], IArrayAssert>, IListHasItemsAssert<IArrayAssert>,
		IReflectionAssert<IArrayAssert>, IListAssert<Object[], IArrayAssert> {
	
}
