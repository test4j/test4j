package org.jtester.datafilling.annotations;

import org.jtester.datafilling.annotations.FillDouble;

public class DoubleValueWithErrorPojo {
	@FillDouble(value = "fajhfakh")
	private double doubleFieldWithErrorInAnnotation;

	/**
	 * @return the doubleFieldWithErrorInAnnotation
	 */
	public double getDoubleFieldWithErrorInAnnotation() {
		return doubleFieldWithErrorInAnnotation;
	}

	/**
	 * @param doubleFieldWithErrorInAnnotation
	 *            the doubleFieldWithErrorInAnnotation to set
	 */
	public void setDoubleFieldWithErrorInAnnotation(double doubleFieldWithErrorInAnnotation) {
		this.doubleFieldWithErrorInAnnotation = doubleFieldWithErrorInAnnotation;
	}
}
