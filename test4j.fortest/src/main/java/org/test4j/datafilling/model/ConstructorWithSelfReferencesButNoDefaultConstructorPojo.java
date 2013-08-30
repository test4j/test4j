package org.test4j.datafilling.model;

import java.io.Serializable;

import org.test4j.datafilling.annotations.FillConstructor;

@SuppressWarnings("serial")
public class ConstructorWithSelfReferencesButNoDefaultConstructorPojo implements Serializable {

	private int intField;

	private ConstructorWithSelfReferencesButNoDefaultConstructorPojo parent;

	private ConstructorWithSelfReferencesButNoDefaultConstructorPojo anotherParent;

	@FillConstructor
	public ConstructorWithSelfReferencesButNoDefaultConstructorPojo(int intField,
			ConstructorWithSelfReferencesButNoDefaultConstructorPojo parent,
			ConstructorWithSelfReferencesButNoDefaultConstructorPojo anotherParent) {
		super();
		this.intField = intField;
		this.parent = parent;
		this.anotherParent = anotherParent;
	}

	public int getIntField() {
		return intField;
	}

	public ConstructorWithSelfReferencesButNoDefaultConstructorPojo getParent() {
		return parent;
	}

	public ConstructorWithSelfReferencesButNoDefaultConstructorPojo getAnotherParent() {
		return anotherParent;
	}
}
