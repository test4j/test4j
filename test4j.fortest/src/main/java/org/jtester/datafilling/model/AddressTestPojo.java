package org.jtester.datafilling.model;

import java.io.Serializable;

public class AddressTestPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String address1;

	private String address2;

	private String address3;

	private String city;

	private String province;

	private String zipCode;

	private String country;

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public String getAddress3() {
		return address3;
	}

	public String getCity() {
		return city;
	}

	public String getProvince() {
		return province;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getCountry() {
		return country;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((address1 == null) ? 0 : address1.hashCode());
		result = (prime * result) + ((address2 == null) ? 0 : address2.hashCode());
		result = (prime * result) + ((address3 == null) ? 0 : address3.hashCode());
		result = (prime * result) + ((city == null) ? 0 : city.hashCode());
		result = (prime * result) + ((country == null) ? 0 : country.hashCode());
		result = (prime * result) + ((province == null) ? 0 : province.hashCode());
		result = (prime * result) + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		AddressTestPojo other = (AddressTestPojo) obj;
		if (address1 == null) {
			if (other.address1 != null) {
				return false;
			}
		} else if (!address1.equals(other.address1)) {
			return false;
		}
		if (address2 == null) {
			if (other.address2 != null) {
				return false;
			}
		} else if (!address2.equals(other.address2)) {
			return false;
		}
		if (address3 == null) {
			if (other.address3 != null) {
				return false;
			}
		} else if (!address3.equals(other.address3)) {
			return false;
		}
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (country == null) {
			if (other.country != null) {
				return false;
			}
		} else if (!country.equals(other.country)) {
			return false;
		}
		if (province == null) {
			if (other.province != null) {
				return false;
			}
		} else if (!province.equals(other.province)) {
			return false;
		}
		if (zipCode == null) {
			if (other.zipCode != null) {
				return false;
			}
		} else if (!zipCode.equals(other.zipCode)) {
			return false;
		}
		return true;
	}
}
