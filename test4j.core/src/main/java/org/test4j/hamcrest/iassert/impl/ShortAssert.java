package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IShortAssert;

public class ShortAssert extends Assert<Short, IShortAssert> implements IShortAssert {
    public ShortAssert() {
        super(Short.class, IShortAssert.class);
    }

    public ShortAssert(Short sht) {
        super(sht, Short.class, IShortAssert.class);
    }
}
