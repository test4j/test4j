package org.jtester.module.spring.testedbeans.aop;

public class Cat implements Animal {
	public boolean hasFur() {
		return true;
	}

	public Integer getNumberPaws() {
		return 4;
	}

	public Boolean hasTail() {
		return true;
	}

	public Boolean hasHotBlood() {
		return true;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getSpecies() {
		return species;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	private String species, colour;
}
