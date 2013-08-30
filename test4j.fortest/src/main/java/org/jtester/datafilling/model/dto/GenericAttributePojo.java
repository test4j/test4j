package org.jtester.datafilling.model.dto;

public class GenericAttributePojo {

	private GenericPojo<String, Long> genericPojo;

	public GenericPojo<String, Long> getGenericPojo() {
		return genericPojo;
	}

	public void setGenericPojo(GenericPojo<String, Long> genericPojo) {
		this.genericPojo = genericPojo;
	}
}
