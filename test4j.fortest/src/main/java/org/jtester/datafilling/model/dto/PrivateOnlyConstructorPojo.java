package org.jtester.datafilling.model.dto;

import java.io.Serializable;

public class PrivateOnlyConstructorPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstName;

	private int intField;

	private PrivateOnlyConstructorPojo() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getIntField() {
		return intField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}
}
