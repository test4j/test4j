package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IObjectAssert;

public class ObjectAssert extends Assert<Object, IObjectAssert> implements IObjectAssert {
    public ObjectAssert() {
        super(Object.class, IObjectAssert.class);
    }

    public ObjectAssert(Object bean) {
        super(bean, Object.class, IObjectAssert.class);
    }
}
