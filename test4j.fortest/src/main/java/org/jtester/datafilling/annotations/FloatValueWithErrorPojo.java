package org.jtester.datafilling.annotations;

import org.jtester.datafilling.annotations.FillFloat;

public class FloatValueWithErrorPojo {

	@FillFloat(value = "fafhakljhf")
	private float floatFieldWithErrorInAnnotation;

	/**
	 * @return the floatFieldWithErrorInAnnotation
	 */
	public float getFloatFieldWithErrorInAnnotation() {
		return floatFieldWithErrorInAnnotation;
	}

	/**
	 * @param floatFieldWithErrorInAnnotation
	 *            the floatFieldWithErrorInAnnotation to set
	 */
	public void setFloatFieldWithErrorInAnnotation(float floatFieldWithErrorInAnnotation) {
		this.floatFieldWithErrorInAnnotation = floatFieldWithErrorInAnnotation;
	}
}
