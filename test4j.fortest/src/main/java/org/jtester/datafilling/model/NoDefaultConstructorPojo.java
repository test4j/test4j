package org.jtester.datafilling.model;

import java.io.Serializable;

public class NoDefaultConstructorPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int intField;

	public NoDefaultConstructorPojo(int intField) {
		super();
		this.intField = intField;
	}

	public int getIntField() {
		return intField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}
}
