package org.jtester.fortest.beans;

import java.util.Date;

public class Employee implements java.io.Serializable {
	private static final long serialVersionUID = -7583085914565894622L;

	private String name;

	private transient double sarary;

	private Date date;

	public Employee() {
		super();
	}
	
	public Employee(String name, double sarary) {
		this.name = name;
		this.sarary = sarary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSarary() {
		return sarary;
	}

	public void setSarary(double sarary) {
		this.sarary = sarary;
	}

	public void raiseSalary(double raise) {
		this.sarary += raise;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
