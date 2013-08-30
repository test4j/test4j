package org.jtester.datafilling.model.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jtester.datafilling.annotations.FillList;
import org.jtester.datafilling.annotations.FillString;

public class Client implements Serializable {

	private static final long serialVersionUID = 1L;

	@FillString(value = "Michael")
	private String firstName;

	private String lastName;

	private Calendar dateCreated;

	@FillList(size = 3)
	private List<Order> orders = new ArrayList<Order>();

	@FillList(size = 2)
	private List<Address> addresses = new ArrayList<Address>();

	private List<BankAccount> bankAccounts = new ArrayList<BankAccount>();

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Calendar getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Calendar dateCreated) {
		this.dateCreated = dateCreated;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

}
