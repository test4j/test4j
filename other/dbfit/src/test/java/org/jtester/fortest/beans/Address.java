package org.jtester.fortest.beans;

public class Address implements java.io.Serializable {
	private static final long serialVersionUID = -8560639654630006910L;

	private long id;

	private String street;

	private String postcode;

	private String name;

	public Address(String street) {
		this.street = street;
	}

	public Address(long id, String street) {
		this.id = id;
		this.street = street;
	}

	public Address(String street, String postcode, String name) {
		this.street = street;
		this.postcode = postcode;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
