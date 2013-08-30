package org.jtester.datafilling.annotations;

import org.jtester.datafilling.annotations.FillShort;

public class ShortValueWithErrorPojo  {

	@FillShort(value = "fajkfhaf")
	private short shortFieldWithErrorInAnnotation;

	/**
	 * @return the shortFieldWithErrorInAnnotation
	 */
	public short getShortFieldWithErrorInAnnotation() {
		return shortFieldWithErrorInAnnotation;
	}

	/**
	 * @param shortFieldWithErrorInAnnotation
	 *            the shortFieldWithErrorInAnnotation to set
	 */
	public void setShortFieldWithErrorInAnnotation(
			short shortFieldWithErrorInAnnotation) {
		this.shortFieldWithErrorInAnnotation = shortFieldWithErrorInAnnotation;
	}

}
