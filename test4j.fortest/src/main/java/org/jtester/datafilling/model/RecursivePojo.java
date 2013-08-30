package org.jtester.datafilling.model;

import java.io.Serializable;

public class RecursivePojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int intField;

	private RecursivePojo parent;

	public RecursivePojo() {
	}

	public int getIntField() {
		return intField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}

	public RecursivePojo getParent() {
		return parent;
	}

	public void setParent(RecursivePojo parent) {
		this.parent = parent;
	}

}
