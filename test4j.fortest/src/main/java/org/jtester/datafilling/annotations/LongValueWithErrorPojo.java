package org.jtester.datafilling.annotations;

import org.test4j.datafilling.annotations.FillLong;

public class LongValueWithErrorPojo {

	@FillLong(value = "afhafhakflh")
	private long longFieldWithErrorInAnnotation;


	/**
	 * @return the longFieldWithErrorInAnnotation
	 */
	public long getLongFieldWithErrorInAnnotation() {
		return longFieldWithErrorInAnnotation;
	}

	/**
	 * @param longFieldWithErrorInAnnotation
	 *            the longFieldWithErrorInAnnotation to set
	 */
	public void setLongFieldWithErrorInAnnotation(
			long longFieldWithErrorInAnnotation) {
		this.longFieldWithErrorInAnnotation = longFieldWithErrorInAnnotation;
	}
}
