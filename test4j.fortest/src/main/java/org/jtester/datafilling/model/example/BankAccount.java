package org.jtester.datafilling.model.example;

import java.io.Serializable;

public class BankAccount implements Serializable {

	private static final long serialVersionUID = 1L;

	private int account;

	private String bank;

	private String sortCode;

	private double balance;

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
