package org.jtester.datafilling.annotations;

import org.jtester.datafilling.annotations.FillDouble;
import org.jtester.datafilling.utils.FillDataTestConstants;

public class DoubleValuePojo {

	@FillDouble(min = FillDataTestConstants.NUMBER_DOUBLE_MIN_VALUE)
	private double doubleFieldWithMinValueOnly;

	@FillDouble(max = FillDataTestConstants.NUMBER_DOUBLE_ONE_HUNDRED)
	private double doubleFieldWithMaxValueOnly;

	@FillDouble(min = FillDataTestConstants.NUMBER_DOUBLE_MIN_VALUE, max = FillDataTestConstants.NUMBER_DOUBLE_MAX_VALUE)
	private double doubleFieldWithMinAndMaxValue;

	@FillDouble(value = FillDataTestConstants.DOUBLE_PRECISE_VALUE)
	private double doubleFieldWithPreciseValue;

	@FillDouble(min = FillDataTestConstants.NUMBER_DOUBLE_MIN_VALUE)
	private Double doubleObjectFieldWithMinValueOnly;

	@FillDouble(max = FillDataTestConstants.NUMBER_DOUBLE_ONE_HUNDRED)
	private Double doubleObjectFieldWithMaxValueOnly;

	@FillDouble(min = FillDataTestConstants.NUMBER_DOUBLE_MIN_VALUE, max = FillDataTestConstants.NUMBER_DOUBLE_MAX_VALUE)
	private Double doubleObjectFieldWithMinAndMaxValue;

	@FillDouble(value = FillDataTestConstants.DOUBLE_PRECISE_VALUE)
	private Double doubleObjectFieldWithPreciseValue;

	/**
	 * @return the doubleFieldWithMinValueOnly
	 */
	public double getDoubleFieldWithMinValueOnly() {
		return doubleFieldWithMinValueOnly;
	}

	/**
	 * @param doubleFieldWithMinValueOnly
	 *            the doubleFieldWithMinValueOnly to set
	 */
	public void setDoubleFieldWithMinValueOnly(double doubleFieldWithMinValueOnly) {
		this.doubleFieldWithMinValueOnly = doubleFieldWithMinValueOnly;
	}

	/**
	 * @return the doubleFieldWithMaxValueOnly
	 */
	public double getDoubleFieldWithMaxValueOnly() {
		return doubleFieldWithMaxValueOnly;
	}

	/**
	 * @param doubleFieldWithMaxValueOnly
	 *            the doubleFieldWithMaxValueOnly to set
	 */
	public void setDoubleFieldWithMaxValueOnly(double doubleFieldWithMaxValueOnly) {
		this.doubleFieldWithMaxValueOnly = doubleFieldWithMaxValueOnly;
	}

	/**
	 * @return the doubleFieldWithMinAndMaxValue
	 */
	public double getDoubleFieldWithMinAndMaxValue() {
		return doubleFieldWithMinAndMaxValue;
	}

	/**
	 * @param doubleFieldWithMinAndMaxValue
	 *            the doubleFieldWithMinAndMaxValue to set
	 */
	public void setDoubleFieldWithMinAndMaxValue(double doubleFieldWithMinAndMaxValue) {
		this.doubleFieldWithMinAndMaxValue = doubleFieldWithMinAndMaxValue;
	}

	/**
	 * @return the doubleObjectFieldWithMinValueOnly
	 */
	public Double getDoubleObjectFieldWithMinValueOnly() {
		return doubleObjectFieldWithMinValueOnly;
	}

	/**
	 * @param doubleObjectFieldWithMinValueOnly
	 *            the doubleObjectFieldWithMinValueOnly to set
	 */
	public void setDoubleObjectFieldWithMinValueOnly(Double doubleObjectFieldWithMinValueOnly) {
		this.doubleObjectFieldWithMinValueOnly = doubleObjectFieldWithMinValueOnly;
	}

	/**
	 * @return the doubleObjectFieldWithMaxValueOnly
	 */
	public Double getDoubleObjectFieldWithMaxValueOnly() {
		return doubleObjectFieldWithMaxValueOnly;
	}

	/**
	 * @param doubleObjectFieldWithMaxValueOnly
	 *            the doubleObjectFieldWithMaxValueOnly to set
	 */
	public void setDoubleObjectFieldWithMaxValueOnly(Double doubleObjectFieldWithMaxValueOnly) {
		this.doubleObjectFieldWithMaxValueOnly = doubleObjectFieldWithMaxValueOnly;
	}

	/**
	 * @return the doubleObjectFieldWithMinAndMaxValue
	 */
	public Double getDoubleObjectFieldWithMinAndMaxValue() {
		return doubleObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @param doubleObjectFieldWithMinAndMaxValue
	 *            the doubleObjectFieldWithMinAndMaxValue to set
	 */
	public void setDoubleObjectFieldWithMinAndMaxValue(Double doubleObjectFieldWithMinAndMaxValue) {
		this.doubleObjectFieldWithMinAndMaxValue = doubleObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @return the doubleFieldWithPreciseValue
	 */
	public double getDoubleFieldWithPreciseValue() {
		return doubleFieldWithPreciseValue;
	}

	/**
	 * @param doubleFieldWithPreciseValue
	 *            the doubleFieldWithPreciseValue to set
	 */
	public void setDoubleFieldWithPreciseValue(double doubleFieldWithPreciseValue) {
		this.doubleFieldWithPreciseValue = doubleFieldWithPreciseValue;
	}

	/**
	 * @return the doubleObjectFieldWithPreciseValue
	 */
	public Double getDoubleObjectFieldWithPreciseValue() {
		return doubleObjectFieldWithPreciseValue;
	}

	/**
	 * @param doubleObjectFieldWithPreciseValue
	 *            the doubleObjectFieldWithPreciseValue to set
	 */
	public void setDoubleObjectFieldWithPreciseValue(Double doubleObjectFieldWithPreciseValue) {
		this.doubleObjectFieldWithPreciseValue = doubleObjectFieldWithPreciseValue;
	}
}
