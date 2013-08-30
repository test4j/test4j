package org.jtester.datafilling.model;

import java.io.Serializable;

public class BankAccountTestPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String accountNbr;

	private String sortCode;

	private String bankName;

	private String branchName;

	public String getAccountNbr() {
		return accountNbr;
	}

	public String getSortCode() {
		return sortCode;
	}

	public String getBankName() {
		return bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((accountNbr == null) ? 0 : accountNbr.hashCode());
		result = (prime * result) + ((bankName == null) ? 0 : bankName.hashCode());
		result = (prime * result) + ((branchName == null) ? 0 : branchName.hashCode());
		result = (prime * result) + ((sortCode == null) ? 0 : sortCode.hashCode());
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
		BankAccountTestPojo other = (BankAccountTestPojo) obj;
		if (accountNbr == null) {
			if (other.accountNbr != null) {
				return false;
			}
		} else if (!accountNbr.equals(other.accountNbr)) {
			return false;
		}
		if (bankName == null) {
			if (other.bankName != null) {
				return false;
			}
		} else if (!bankName.equals(other.bankName)) {
			return false;
		}
		if (branchName == null) {
			if (other.branchName != null) {
				return false;
			}
		} else if (!branchName.equals(other.branchName)) {
			return false;
		}
		if (sortCode == null) {
			if (other.sortCode != null) {
				return false;
			}
		} else if (!sortCode.equals(other.sortCode)) {
			return false;
		}
		return true;
	}
}
