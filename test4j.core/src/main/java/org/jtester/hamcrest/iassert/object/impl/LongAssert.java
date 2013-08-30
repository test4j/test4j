package org.jtester.hamcrest.iassert.object.impl;

import org.jtester.hamcrest.iassert.object.intf.ILongAssert;

public class LongAssert extends NumberAssert<Long, ILongAssert> implements ILongAssert {
	public LongAssert() {
		super(ILongAssert.class);
		this.valueClaz = Long.class;
	}

	public LongAssert(Long l) {
		super(l, ILongAssert.class);
		this.valueClaz = Long.class;
	}
}
