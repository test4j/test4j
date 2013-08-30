package org.jtester.fortest.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ComplexObject {
	private String name;

	private double sarary;

	private Date date;

	private Serializable phoneNumber;

	private User user;

	private Collection<String> addresses;

	private Map<String, Serializable> map;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Serializable getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Serializable phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Collection<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(Collection<String> addresses) {
		this.addresses = addresses;
	}

	public Map<String, Serializable> getMap() {
		return map;
	}

	public void setMap(Map<String, Serializable> map) {
		this.map = map;
	}

	public static ComplexObject instance() {
		ComplexObject co = new ComplexObject();

		co.setName("I am a test");
		co.setDate(new Date());
		co.setPhoneNumber(new PhoneNumber("0571", "88886666"));
		co.setSarary(2000d);
		co.setUser(new User("wu", "davey"));
		co.setAddresses(Arrays.asList("地址一", "地址二", "地址三"));
		co.setMap(new HashMap<String, Serializable>() {
			private static final long serialVersionUID = -6999560873523992661L;

			{
				this.put("key1", "value1");
				this.put("key2", new User("darui.wu"));
			}
		});

		return co;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("name=" + this.name);
		buffer.append("\n");
		buffer.append("sarary=" + this.sarary);
		buffer.append("\n");
		buffer.append("addresses=" + this.addresses.toString());
		return buffer.toString();
	}

	public static class PhoneNumber implements Serializable {
		private static final long serialVersionUID = 8567955906309667163L;

		private String areaNo;

		private String phone;

		public PhoneNumber(String areaNo, String phone) {
			this.areaNo = areaNo;
			this.phone = phone;
		}

		public String getAreaNo() {
			return areaNo;
		}

		public void setAreaNo(String areaNo) {
			this.areaNo = areaNo;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}
	}
}
