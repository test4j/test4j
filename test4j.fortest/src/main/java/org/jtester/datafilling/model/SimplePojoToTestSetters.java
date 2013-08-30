package org.jtester.datafilling.model;

import java.io.Serializable;

public class SimplePojoToTestSetters implements Serializable {

	private static final long serialVersionUID = 1L;

	private String stringField;

	private int intField;

	public SimplePojoToTestSetters() {
	}

	public String getStringField() {
		return stringField;
	}

	public void setStringField(String stringField) {
		this.stringField = stringField;
	}

	public int getIntField() {
		return intField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + intField;
		result = prime * result + ((stringField == null) ? 0 : stringField.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePojoToTestSetters other = (SimplePojoToTestSetters) obj;
		if (intField != other.intField)
			return false;
		if (stringField == null) {
			if (other.stringField != null)
				return false;
		} else if (!stringField.equals(other.stringField))
			return false;
		return true;
	}

}
