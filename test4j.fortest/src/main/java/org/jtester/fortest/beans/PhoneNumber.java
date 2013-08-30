package org.jtester.fortest.beans;

public class PhoneNumber implements java.io.Serializable {
	private static final long serialVersionUID = 5646650408028947175L;
	private int code;
	private String number;

	public PhoneNumber() {

	}

	public PhoneNumber(int code, String number) {
		this.code = code;
		this.number = number;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
