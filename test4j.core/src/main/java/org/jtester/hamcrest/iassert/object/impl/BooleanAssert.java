package org.jtester.hamcrest.iassert.object.impl;

import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.core.IsEqual;
import org.jtester.hamcrest.iassert.common.impl.AllAssert;
import org.jtester.hamcrest.iassert.object.intf.IBooleanAssert;

public class BooleanAssert extends AllAssert<Boolean, IBooleanAssert> implements IBooleanAssert {

	public BooleanAssert() {
		super(IBooleanAssert.class);
		this.valueClaz = Boolean.class;
	}

	public BooleanAssert(Boolean value) {
		super(value, IBooleanAssert.class);
		this.valueClaz = Boolean.class;
	}

	public IBooleanAssert is(boolean bl) {
		return super.isEqualTo(bl);
	}

	public IBooleanAssert is(String message, boolean bl) {
		Matcher<?> matcher = IsEqual.equalTo(bl);
		return this.assertThat(message, matcher);
	}
}
