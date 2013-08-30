package org.test4j.hamcrest.iassert.object.impl;

import org.test4j.hamcrest.iassert.common.impl.ReflectionAssert;
import org.test4j.hamcrest.iassert.object.intf.IObjectAssert;

public class ObjectAssert extends ReflectionAssert<Object, IObjectAssert> implements IObjectAssert {
	public ObjectAssert() {
		super(IObjectAssert.class);
		this.valueClaz = Object.class;
	}

	public ObjectAssert(Object bean) {
		super(bean, IObjectAssert.class);
		this.valueClaz = Object.class;
	}
}
