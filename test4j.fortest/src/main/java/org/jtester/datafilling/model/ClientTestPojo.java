package org.jtester.datafilling.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientTestPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstName;

	private String lastName;

	private Calendar dateCreated;

	private Calendar dateLastUpdated;

	private AddressTestPojo address;

	private final List<BankAccountTestPojo> bankAccounts = new ArrayList<BankAccountTestPojo>();

	public ClientTestPojo() {
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Calendar getDateCreated() {
		return dateCreated;
	}

	public Calendar getDateLastUpdated() {
		return dateLastUpdated;
	}

	public AddressTestPojo getAddress() {
		return address;
	}

	public List<BankAccountTestPojo> getBankAccounts() {
		return bankAccounts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((address == null) ? 0 : address.hashCode());
		result = (prime * result) + ((bankAccounts == null) ? 0 : bankAccounts.hashCode());
		result = (prime * result) + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = (prime * result) + ((dateLastUpdated == null) ? 0 : dateLastUpdated.hashCode());
		result = (prime * result) + ((firstName == null) ? 0 : firstName.hashCode());
		result = (prime * result) + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		ClientTestPojo other = (ClientTestPojo) obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
			return false;
		}
		if (bankAccounts == null) {
			if (other.bankAccounts != null) {
				return false;
			}
		} else if (!bankAccounts.equals(other.bankAccounts)) {
			return false;
		}
		if (dateCreated == null) {
			if (other.dateCreated != null) {
				return false;
			}
		} else if (!dateCreated.equals(other.dateCreated)) {
			return false;
		}
		if (dateLastUpdated == null) {
			if (other.dateLastUpdated != null) {
				return false;
			}
		} else if (!dateLastUpdated.equals(other.dateLastUpdated)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		return true;
	}
}
