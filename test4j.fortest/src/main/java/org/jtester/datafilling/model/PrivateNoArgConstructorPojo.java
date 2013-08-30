package org.jtester.datafilling.model;

import java.io.Serializable;

public class PrivateNoArgConstructorPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int intField;

	private PrivateNoArgConstructorPojo() {

	}

	public int getIntField() {
		return intField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}

}
