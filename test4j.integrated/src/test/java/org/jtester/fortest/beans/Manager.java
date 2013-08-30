package org.jtester.fortest.beans;

import java.io.Serializable;
import java.util.Date;

public class Manager extends Employee {
	private static final long serialVersionUID = 843725563822394654L;
	private Employee secretary;

	private Serializable phoneNumber;

	public Manager() {
		super();
	}

	public Manager(String name, double sarary) {
		super(name, sarary);
	}

	public Employee getSecretary() {
		return secretary;
	}

	public void setSecretary(Employee secretary) {
		this.secretary = secretary;
	}

	public Serializable getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Serializable phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static Manager mock() {
		Employee harry = new Employee("Harry Hacker", 50000);
		Manager manager = new Manager("Tony Tester", 80000);
		PhoneNumber phone = new PhoneNumber(571, "0571-88886666");
		manager.setSecretary(harry);
		manager.setPhoneNumber(phone);
		manager.setDate(new Date());
		return manager;
	}
}
