package org.test4j.hamcrest.iassert.impl;

import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IByteAssert;

public class ByteAssert extends Assert<Byte, IByteAssert> implements IByteAssert {

    public ByteAssert(Byte value) {
        super(value, Byte.class, IByteAssert.class);
    }

    public ByteAssert() {
        super(Byte.class, IByteAssert.class);
    }
}
