package org.jtester.datafilling.annotations;

import org.jtester.datafilling.annotations.FillInteger;
import org.jtester.datafilling.utils.FillDataTestConstants;

public class IntegerValuePojo {

	@FillInteger(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE)
	private int intFieldWithMinValueOnly;

	@FillInteger(value = FillDataTestConstants.INTEGER_PRECISE_VALUE)
	private int intFieldWithPreciseValue;

	@FillInteger(max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private int intFieldWithMaxValueOnly;

	@FillInteger(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE, max = FillDataTestConstants.NUMBER_INT_MAX_VALUE)
	private int intFieldWithMinAndMaxValue;

	@FillInteger(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE)
	private Integer integerObjectFieldWithMinValueOnly;

	@FillInteger(max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private Integer integerObjectFieldWithMaxValueOnly;

	@FillInteger(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE, max = FillDataTestConstants.NUMBER_INT_MAX_VALUE)
	private Integer integerObjectFieldWithMinAndMaxValue;

	@FillInteger(value = FillDataTestConstants.INTEGER_PRECISE_VALUE)
	private Integer integerObjectFieldWithPreciseValue;

	/**
	 * @return the intFieldWithMinValueOnly
	 */
	public int getIntFieldWithMinValueOnly() {
		return intFieldWithMinValueOnly;
	}

	/**
	 * @param intFieldWithMinValueOnly
	 *            the intFieldWithMinValueOnly to set
	 */
	public void setIntFieldWithMinValueOnly(int intFieldWithMinValueOnly) {
		this.intFieldWithMinValueOnly = intFieldWithMinValueOnly;
	}

	/**
	 * @return the intFieldWithMaxValueOnly
	 */
	public int getIntFieldWithMaxValueOnly() {
		return intFieldWithMaxValueOnly;
	}

	/**
	 * @param intFieldWithMaxValueOnly
	 *            the intFieldWithMaxValueOnly to set
	 */
	public void setIntFieldWithMaxValueOnly(int intFieldWithMaxValueOnly) {
		this.intFieldWithMaxValueOnly = intFieldWithMaxValueOnly;
	}

	/**
	 * @return the intFieldWithMinAndMaxValue
	 */
	public int getIntFieldWithMinAndMaxValue() {
		return intFieldWithMinAndMaxValue;
	}

	/**
	 * @param intFieldWithMinAndMaxValue
	 *            the intFieldWithMinAndMaxValue to set
	 */
	public void setIntFieldWithMinAndMaxValue(int intFieldWithMinAndMaxValue) {
		this.intFieldWithMinAndMaxValue = intFieldWithMinAndMaxValue;
	}

	/**
	 * @return the integerObjectFieldWithMinValueOnly
	 */
	public Integer getIntegerObjectFieldWithMinValueOnly() {
		return integerObjectFieldWithMinValueOnly;
	}

	/**
	 * @param integerObjectFieldWithMinValueOnly
	 *            the integerObjectFieldWithMinValueOnly to set
	 */
	public void setIntegerObjectFieldWithMinValueOnly(Integer integerObjectFieldWithMinValueOnly) {
		this.integerObjectFieldWithMinValueOnly = integerObjectFieldWithMinValueOnly;
	}

	/**
	 * @return the integerObjectFieldWithMaxValueOnly
	 */
	public Integer getIntegerObjectFieldWithMaxValueOnly() {
		return integerObjectFieldWithMaxValueOnly;
	}

	/**
	 * @param integerObjectFieldWithMaxValueOnly
	 *            the integerObjectFieldWithMaxValueOnly to set
	 */
	public void setIntegerObjectFieldWithMaxValueOnly(Integer integerObjectFieldWithMaxValueOnly) {
		this.integerObjectFieldWithMaxValueOnly = integerObjectFieldWithMaxValueOnly;
	}

	/**
	 * @return the integerObjectFieldWithMinAndMaxValue
	 */
	public Integer getIntegerObjectFieldWithMinAndMaxValue() {
		return integerObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @param integerObjectFieldWithMinAndMaxValue
	 *            the integerObjectFieldWithMinAndMaxValue to set
	 */
	public void setIntegerObjectFieldWithMinAndMaxValue(Integer integerObjectFieldWithMinAndMaxValue) {
		this.integerObjectFieldWithMinAndMaxValue = integerObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @return the intFieldWithPreciseValue
	 */
	public int getIntFieldWithPreciseValue() {
		return intFieldWithPreciseValue;
	}

	/**
	 * @param intFieldWithPreciseValue
	 *            the intFieldWithPreciseValue to set
	 */
	public void setIntFieldWithPreciseValue(int intFieldWithPreciseValue) {
		this.intFieldWithPreciseValue = intFieldWithPreciseValue;
	}

	/**
	 * @return the integerObjectFieldWithPreciseValue
	 */
	public Integer getIntegerObjectFieldWithPreciseValue() {
		return integerObjectFieldWithPreciseValue;
	}

	/**
	 * @param integerObjectFieldWithPreciseValue
	 *            the integerObjectFieldWithPreciseValue to set
	 */
	public void setIntegerObjectFieldWithPreciseValue(Integer integerObjectFieldWithPreciseValue) {
		this.integerObjectFieldWithPreciseValue = integerObjectFieldWithPreciseValue;
	}
}
