package org.jtester.fortest.beans;

import java.util.ArrayList;
import java.util.List;

public class User implements java.io.Serializable {
	private static final long serialVersionUID = 1145595282412714496L;
	private long id;
	private String first;
	private String last;

	private String postcode;
	private double sarary;

	private Address address;

	private List<Address> addresses;

	private String[] phones;

	private User assistor;

	public User() {
	}

	public User(long id) {
		this.id = id;
	}

	public User(String first) {
		this.first = first;
	}

	public User(String first, String last) {
		this.first = first;
		this.last = last;
	}

	public User(long id, String first, String last) {
		this.id = id;
		this.first = first;
		this.last = last;
	}

	public User(long id, double sarary) {
		this.id = id;
		this.sarary = sarary;
	}

	public User(String first, String last, Address address) {
		this.first = first;
		this.last = last;
		this.address = address;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirst() {
		return first;
	}

	public User setFirst(String first) {
		this.first = first;
		return this;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public double getSarary() {
		return sarary;
	}

	public void setSarary(Double sarary) {
		this.sarary = sarary == null ? 0.0d : sarary;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public User setAddresses(List<Address> addresses) {
		this.addresses = addresses;
		return this;
	}

	public String[] getPhones() {
		return phones;
	}

	public void setPhones(String[] phones) {
		this.phones = phones;
	}

	public User getAssistor() {
		return assistor;
	}

	public void setAssistor(User assistor) {
		this.assistor = assistor;
	}

	public static User newUser(String name, String[] phones) {
		User user = new User();
		user.first = name;
		user.setPhones(phones);
		return user;
	}

	public static User mock() {
		User user = new User(1, "wu", "darui");
		user.setAddresses(new ArrayList<Address>() {

			private static final long serialVersionUID = 516532764093459888L;
			{
				this.add(new Address(2, "stree2"));
				this.add(new Address(3, "stree3"));
			}
		});
		return user;
	}
}
