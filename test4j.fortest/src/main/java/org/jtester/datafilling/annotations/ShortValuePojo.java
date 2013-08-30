package org.jtester.datafilling.annotations;

import org.jtester.datafilling.annotations.FillShort;
import org.jtester.datafilling.utils.FillDataTestConstants;

public class ShortValuePojo {
	@FillShort(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE)
	private short shortFieldWithMinValueOnly;

	@FillShort(max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private short shortFieldWithMaxValueOnly;

	@FillShort(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE, max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private short shortFieldWithMinAndMaxValue;

	@FillShort(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE)
	private Short shortObjectFieldWithMinValueOnly;

	@FillShort(max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private Short shortObjectFieldWithMaxValueOnly;

	@FillShort(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE, max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private Short shortObjectFieldWithMinAndMaxValue;

	@FillShort(value = FillDataTestConstants.SHORT_PRECISE_VALUE)
	private short shortFieldWithPreciseValue;

	/**
	 * @return the shortFieldWithMinValueOnly
	 */
	public short getShortFieldWithMinValueOnly() {
		return shortFieldWithMinValueOnly;
	}

	/**
	 * @param shortFieldWithMinValueOnly
	 *            the shortFieldWithMinValueOnly to set
	 */
	public void setShortFieldWithMinValueOnly(short shortFieldWithMinValueOnly) {
		this.shortFieldWithMinValueOnly = shortFieldWithMinValueOnly;
	}

	/**
	 * @return the shortFieldWithMaxValueOnly
	 */
	public short getShortFieldWithMaxValueOnly() {
		return shortFieldWithMaxValueOnly;
	}

	/**
	 * @param shortFieldWithMaxValueOnly
	 *            the shortFieldWithMaxValueOnly to set
	 */
	public void setShortFieldWithMaxValueOnly(short shortFieldWithMaxValueOnly) {
		this.shortFieldWithMaxValueOnly = shortFieldWithMaxValueOnly;
	}

	/**
	 * @return the shortFieldWithMinAndMaxValue
	 */
	public short getShortFieldWithMinAndMaxValue() {
		return shortFieldWithMinAndMaxValue;
	}

	/**
	 * @param shortFieldWithMinAndMaxValue
	 *            the shortFieldWithMinAndMaxValue to set
	 */
	public void setShortFieldWithMinAndMaxValue(
			short shortFieldWithMinAndMaxValue) {
		this.shortFieldWithMinAndMaxValue = shortFieldWithMinAndMaxValue;
	}

	/**
	 * @return the shortObjectFieldWithMinValueOnly
	 */
	public Short getShortObjectFieldWithMinValueOnly() {
		return shortObjectFieldWithMinValueOnly;
	}

	/**
	 * @param shortObjectFieldWithMinValueOnly
	 *            the shortObjectFieldWithMinValueOnly to set
	 */
	public void setShortObjectFieldWithMinValueOnly(
			Short shortObjectFieldWithMinValueOnly) {
		this.shortObjectFieldWithMinValueOnly = shortObjectFieldWithMinValueOnly;
	}

	/**
	 * @return the shortObjectFieldWithMaxValueOnly
	 */
	public Short getShortObjectFieldWithMaxValueOnly() {
		return shortObjectFieldWithMaxValueOnly;
	}

	/**
	 * @param shortObjectFieldWithMaxValueOnly
	 *            the shortObjectFieldWithMaxValueOnly to set
	 */
	public void setShortObjectFieldWithMaxValueOnly(
			Short shortObjectFieldWithMaxValueOnly) {
		this.shortObjectFieldWithMaxValueOnly = shortObjectFieldWithMaxValueOnly;
	}

	/**
	 * @return the shortObjectFieldWithMinAndMaxValue
	 */
	public Short getShortObjectFieldWithMinAndMaxValue() {
		return shortObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @param shortObjectFieldWithMinAndMaxValue
	 *            the shortObjectFieldWithMinAndMaxValue to set
	 */
	public void setShortObjectFieldWithMinAndMaxValue(
			Short shortObjectFieldWithMinAndMaxValue) {
		this.shortObjectFieldWithMinAndMaxValue = shortObjectFieldWithMinAndMaxValue;
	}

	public short getShortFieldWithPreciseValue() {
		return shortFieldWithPreciseValue;
	}

	public void setShortFieldWithPreciseValue(short shortFieldWithPreciseValue) {
		this.shortFieldWithPreciseValue = shortFieldWithPreciseValue;
	}
}
