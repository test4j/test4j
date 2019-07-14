package org.test4j.hamcrest.iassert.intf;

import org.test4j.hamcrest.iassert.interal.IAssert;

public interface IJSONAssert extends IAssert<Object, IJSONAssert> {
    /**
     * json对象是集合
     *
     * @return
     */
    ICollectionAssert isJSONArray();

    /**
     * json对象是key-value对象
     *
     * @return
     */
    IMapAssert isJSONMap();

    /**
     * json对象是个简单对象
     *
     * @return
     */
    IStringAssert isSimple();
}
