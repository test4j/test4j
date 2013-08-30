package org.jtester.hamcrest.iassert.object.impl;

import org.jtester.hamcrest.iassert.object.intf.IIntegerAssert;

public class IntegerAssert extends NumberAssert<Integer, IIntegerAssert> implements IIntegerAssert {
	public IntegerAssert() {
		super(IIntegerAssert.class);
		this.valueClaz = Integer.class;
	}

	public IntegerAssert(Integer i) {
		super(i, IIntegerAssert.class);
		this.valueClaz = Integer.class;
	}
}
