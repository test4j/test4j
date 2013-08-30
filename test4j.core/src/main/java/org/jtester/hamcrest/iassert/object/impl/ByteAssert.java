package org.jtester.hamcrest.iassert.object.impl;

import org.jtester.hamcrest.iassert.common.impl.AllAssert;
import org.jtester.hamcrest.iassert.object.intf.IByteAssert;

public class ByteAssert extends AllAssert<Byte, IByteAssert> implements IByteAssert {

	public ByteAssert(Byte value) {
		super(value, IByteAssert.class);
		this.valueClaz = Byte.class;
	}

	public ByteAssert() {
		super(IByteAssert.class);
		this.valueClaz = Byte.class;
	}
}
