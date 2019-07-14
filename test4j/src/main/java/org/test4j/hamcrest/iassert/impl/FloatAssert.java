package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IFloatAssert;

public class FloatAssert extends Assert<Float, IFloatAssert> implements IFloatAssert {
    public FloatAssert() {
        super(Float.class, IFloatAssert.class);
    }

    public FloatAssert(Float f) {
        super(f, Float.class, IFloatAssert.class);
    }
}
