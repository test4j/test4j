package org.jtester.datafilling.model;

import java.io.Serializable;

import org.jtester.datafilling.annotations.FillExclude;

public class ExcludeAnnotationPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int intField;

	@FillExclude
	private SimplePojoToTestSetters somePojo;

	public ExcludeAnnotationPojo() {
	}

	public int getIntField() {
		return intField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}

	public SimplePojoToTestSetters getSomePojo() {
		return somePojo;
	}

	public void setSomePojo(SimplePojoToTestSetters somePojo) {
		this.somePojo = somePojo;
	}

}
