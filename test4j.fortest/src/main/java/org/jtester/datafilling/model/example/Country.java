package org.jtester.datafilling.model.example;

import java.io.Serializable;

import org.jtester.datafilling.annotations.FillConstructor;
import org.jtester.datafilling.annotations.FillString;

public class Country implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int countryId;

	private final String countryCode;

	private final String description;

	@FillConstructor(comment = "Immutable-like POJOs must be annotated with @PodamConstructor")
	public Country(int countryId, @FillString(length = 2) String countryCode, String description) {
		super();
		this.countryId = countryId;
		this.countryCode = countryCode;
		this.description = description;
	}

	public int getCountryId() {
		return countryId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getDescription() {
		return description;
	}

}
