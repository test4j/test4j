package org.jtester.hamcrest.iassert.object.impl;

import org.jtester.hamcrest.iassert.common.impl.AllAssert;
import org.jtester.hamcrest.iassert.object.intf.IArrayAssert;

public class ArrayAssert extends AllAssert<Object[], IArrayAssert> implements IArrayAssert {
	public ArrayAssert() {
		super(IArrayAssert.class);
		this.valueClaz = Object[].class;
	}

	public <T extends Object> ArrayAssert(T value[]) {
		super(IArrayAssert.class);
		this.value = value == null ? null : value.clone();
		this.type = AssertType.AssertStyle;
		this.valueClaz = Object[].class;
	}
}
