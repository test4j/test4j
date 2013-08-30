package org.jtester.hamcrest.iassert.object.intf;

import java.util.Collection;

import org.jtester.hamcrest.iassert.common.intf.IBaseAssert;
import org.jtester.hamcrest.iassert.common.intf.IListAssert;
import org.jtester.hamcrest.iassert.common.intf.IListHasItemsAssert;
import org.jtester.hamcrest.iassert.common.intf.IReflectionAssert;

/**
 * 集合类型对象断言接口
 * 
 * @author darui.wudr
 * 
 */

@SuppressWarnings("rawtypes")
public interface ICollectionAssert extends IBaseAssert<Collection, ICollectionAssert>,
		IReflectionAssert<ICollectionAssert>, IListHasItemsAssert<ICollectionAssert>,
		IListAssert<Collection, ICollectionAssert> {
}
