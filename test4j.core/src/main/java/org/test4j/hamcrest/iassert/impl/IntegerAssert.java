package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IIntegerAssert;

public class IntegerAssert extends Assert<Integer, IIntegerAssert> implements IIntegerAssert {
    public IntegerAssert() {
        super(Integer.class, IIntegerAssert.class);
    }

    public IntegerAssert(Integer i) {
        super(i, Integer.class, IIntegerAssert.class);
    }
}
