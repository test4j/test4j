package org.jtester.hamcrest.iassert.object.impl;

import org.jtester.hamcrest.iassert.object.intf.IShortAssert;

public class ShortAssert extends NumberAssert<Short, IShortAssert> implements IShortAssert {
	public ShortAssert() {
		super(IShortAssert.class);
		this.valueClaz = Short.class;
	}

	public ShortAssert(Short sht) {
		super(sht, IShortAssert.class);
		this.valueClaz = Short.class;
	}
}
