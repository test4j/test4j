package org.jtester.datafilling.annotations;

import org.jtester.datafilling.annotations.FillFloat;
import org.jtester.datafilling.utils.FillDataTestConstants;

public class FloatValuePojo {

	@FillFloat(min = FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE)
	private float floatFieldWithMinValueOnly;

	@FillFloat(max = FillDataTestConstants.NUMBER_FLOAT_ONE_HUNDRED)
	private float floatFieldWithMaxValueOnly;

	@FillFloat(min = FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE, max = FillDataTestConstants.NUMBER_FLOAT_MAX_VALUE)
	private float floatFieldWithMinAndMaxValue;

	@FillFloat(value = FillDataTestConstants.FLOAT_PRECISE_VALUE)
	private float floatFieldWithPreciseValue;

	@FillFloat(min = FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE)
	private Float floatObjectFieldWithMinValueOnly;

	@FillFloat(max = FillDataTestConstants.NUMBER_FLOAT_ONE_HUNDRED)
	private Float floatObjectFieldWithMaxValueOnly;

	@FillFloat(min = FillDataTestConstants.NUMBER_FLOAT_MIN_VALUE, max = FillDataTestConstants.NUMBER_FLOAT_MAX_VALUE)
	private Float floatObjectFieldWithMinAndMaxValue;

	@FillFloat(value = FillDataTestConstants.FLOAT_PRECISE_VALUE)
	private Float floatObjectFieldWithPreciseValue;

	/**
	 * @return the floatFieldWithMinValueOnly
	 */
	public float getFloatFieldWithMinValueOnly() {
		return floatFieldWithMinValueOnly;
	}

	/**
	 * @param floatFieldWithMinValueOnly
	 *            the floatFieldWithMinValueOnly to set
	 */
	public void setFloatFieldWithMinValueOnly(float floatFieldWithMinValueOnly) {
		this.floatFieldWithMinValueOnly = floatFieldWithMinValueOnly;
	}

	/**
	 * @return the floatFieldWithMaxValueOnly
	 */
	public float getFloatFieldWithMaxValueOnly() {
		return floatFieldWithMaxValueOnly;
	}

	/**
	 * @param floatFieldWithMaxValueOnly
	 *            the floatFieldWithMaxValueOnly to set
	 */
	public void setFloatFieldWithMaxValueOnly(float floatFieldWithMaxValueOnly) {
		this.floatFieldWithMaxValueOnly = floatFieldWithMaxValueOnly;
	}

	/**
	 * @return the floatFieldWithMinAndMaxValue
	 */
	public float getFloatFieldWithMinAndMaxValue() {
		return floatFieldWithMinAndMaxValue;
	}

	/**
	 * @param floatFieldWithMinAndMaxValue
	 *            the floatFieldWithMinAndMaxValue to set
	 */
	public void setFloatFieldWithMinAndMaxValue(float floatFieldWithMinAndMaxValue) {
		this.floatFieldWithMinAndMaxValue = floatFieldWithMinAndMaxValue;
	}

	/**
	 * @return the floatObjectFieldWithMinValueOnly
	 */
	public Float getFloatObjectFieldWithMinValueOnly() {
		return floatObjectFieldWithMinValueOnly;
	}

	/**
	 * @param floatObjectFieldWithMinValueOnly
	 *            the floatObjectFieldWithMinValueOnly to set
	 */
	public void setFloatObjectFieldWithMinValueOnly(Float floatObjectFieldWithMinValueOnly) {
		this.floatObjectFieldWithMinValueOnly = floatObjectFieldWithMinValueOnly;
	}

	/**
	 * @return the floatObjectFieldWithMaxValueOnly
	 */
	public Float getFloatObjectFieldWithMaxValueOnly() {
		return floatObjectFieldWithMaxValueOnly;
	}

	/**
	 * @param floatObjectFieldWithMaxValueOnly
	 *            the floatObjectFieldWithMaxValueOnly to set
	 */
	public void setFloatObjectFieldWithMaxValueOnly(Float floatObjectFieldWithMaxValueOnly) {
		this.floatObjectFieldWithMaxValueOnly = floatObjectFieldWithMaxValueOnly;
	}

	/**
	 * @return the floatObjectFieldWithMinAndMaxValue
	 */
	public Float getFloatObjectFieldWithMinAndMaxValue() {
		return floatObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @param floatObjectFieldWithMinAndMaxValue
	 *            the floatObjectFieldWithMinAndMaxValue to set
	 */
	public void setFloatObjectFieldWithMinAndMaxValue(Float floatObjectFieldWithMinAndMaxValue) {
		this.floatObjectFieldWithMinAndMaxValue = floatObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @return the floatFieldWithPreciseValue
	 */
	public float getFloatFieldWithPreciseValue() {
		return floatFieldWithPreciseValue;
	}

	/**
	 * @param floatFieldWithPreciseValue
	 *            the floatFieldWithPreciseValue to set
	 */
	public void setFloatFieldWithPreciseValue(float floatFieldWithPreciseValue) {
		this.floatFieldWithPreciseValue = floatFieldWithPreciseValue;
	}

	/**
	 * @return the floatObjectFieldWithPreciseValue
	 */
	public Float getFloatObjectFieldWithPreciseValue() {
		return floatObjectFieldWithPreciseValue;
	}

	/**
	 * @param floatObjectFieldWithPreciseValue
	 *            the floatObjectFieldWithPreciseValue to set
	 */
	public void setFloatObjectFieldWithPreciseValue(Float floatObjectFieldWithPreciseValue) {
		this.floatObjectFieldWithPreciseValue = floatObjectFieldWithPreciseValue;
	}
}
