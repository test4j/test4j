package org.test4j.hamcrest.iassert.intf;

import java.util.Collection;

import org.test4j.hamcrest.iassert.interal.*;

/**
 * 集合类型对象断言接口
 *
 * @author darui.wudr
 */

@SuppressWarnings("rawtypes")
public interface ICollectionAssert
        extends
        IBaseAssert<Collection, ICollectionAssert>,
        IReflectionAssert<Collection, ICollectionAssert>,
        IListHasItemsAssert<Collection, ICollectionAssert>,
        IListAssert<Collection, ICollectionAssert>,
        ISizedAssert<Collection, ICollectionAssert> {
}
