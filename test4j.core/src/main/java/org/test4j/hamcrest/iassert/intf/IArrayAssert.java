package org.test4j.hamcrest.iassert.intf;

import org.test4j.hamcrest.iassert.interal.*;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.tools.datagen.IDataMap;

/**
 * 数组对象断言接口
 *
 * @author darui.wudr
 */
public interface IArrayAssert
        extends
        IBaseAssert<Object[], IArrayAssert>,
        IListHasItemsAssert<Object[], IArrayAssert>,
        IReflectionAssert<Object[], IArrayAssert>,
        IListAssert<Object[], IArrayAssert>,
        ISizedAssert<Object[], IArrayAssert> {
    @Override
    default IArrayAssert eqDataMap(IDataMap expected, EqMode... modes) {
        return IReflectionAssert.super.eqDataMap(expected, modes);
    }
}
