package org.test4j.datafilling.model;

import java.io.Serializable;
import java.util.List;

import org.test4j.datafilling.annotations.FillConstructor;

public class NoSetterWithCollectionInConstructorPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<String> strList;

	private final int intField;

	@FillConstructor
	public NoSetterWithCollectionInConstructorPojo(List<String> strList, int intField) {
		super();
		this.strList = strList;
		this.intField = intField;
	}

	public List<String> getStrList() {
		return strList;
	}

	public int getIntField() {
		return intField;
	}

}
