package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.ILongAssert;

public class LongAssert extends Assert<Long, ILongAssert> implements ILongAssert {
    public LongAssert() {
        super(Long.class, ILongAssert.class);
    }

    public LongAssert(Long l) {
        super(l, Long.class, ILongAssert.class);
    }
}
