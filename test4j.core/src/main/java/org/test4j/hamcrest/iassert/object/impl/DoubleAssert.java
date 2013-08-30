package org.test4j.hamcrest.iassert.object.impl;

import org.test4j.hamcrest.iassert.object.intf.IDoubleAssert;

public class DoubleAssert extends NumberAssert<Double, IDoubleAssert> implements IDoubleAssert {

	public DoubleAssert() {
		super(IDoubleAssert.class);
		this.valueClaz = Double.class;
	}

	public DoubleAssert(Double dbl) {
		super(dbl, IDoubleAssert.class);
		this.valueClaz = Double.class;
	}
}
