package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IArrayAssert;

public class ArrayAssert extends Assert<Object[], IArrayAssert> implements IArrayAssert {
    public ArrayAssert() {
        super(Object[].class, IArrayAssert.class);
    }

    public <T extends Object> ArrayAssert(T value[]) {
        super(value == null ? null : value.clone(), Object[].class, IArrayAssert.class);
    }
}
