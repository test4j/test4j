package org.jtester.hamcrest.iassert.object.impl;

import org.jtester.hamcrest.iassert.object.intf.IFloatAssert;

public class FloatAssert extends NumberAssert<Float, IFloatAssert> implements IFloatAssert {
	public FloatAssert() {
		super(IFloatAssert.class);
		this.valueClaz = Float.class;
	}

	public FloatAssert(Float f) {
		super(f, IFloatAssert.class);
		this.valueClaz = Float.class;
	}
}
