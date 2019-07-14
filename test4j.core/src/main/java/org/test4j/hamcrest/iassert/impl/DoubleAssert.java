package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IDoubleAssert;

public class DoubleAssert extends Assert<Double, IDoubleAssert> implements IDoubleAssert {

    public DoubleAssert() {
        super(Double.class, IDoubleAssert.class);
    }

    public DoubleAssert(Double dbl) {
        super(dbl, Double.class, IDoubleAssert.class);
    }
}
