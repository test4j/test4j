package org.jtester.datafilling.annotations;

import org.jtester.datafilling.annotations.FillLong;
import org.jtester.datafilling.utils.FillDataTestConstants;

public class LongValuePojo  {
	@FillLong(min = 0)
	private long longFieldWithMinValueOnly;

	@FillLong(max = 100)
	private long longFieldWithMaxValueOnly;

	@FillLong(min = 0, max = 1000)
	private long longFieldWithMinAndMaxValue;

	@FillLong(value = FillDataTestConstants.LONG_PRECISE_VALUE)
	private long longFieldWithPreciseValue;

	@FillLong(min = 0)
	private Long longObjectFieldWithMinValueOnly;

	@FillLong(max = 100)
	private Long longObjectFieldWithMaxValueOnly;

	@FillLong(min = 0, max = 1000)
	private Long longObjectFieldWithMinAndMaxValue;

	@FillLong(value = FillDataTestConstants.LONG_PRECISE_VALUE)
	private Long longObjectFieldWithPreciseValue;

	/**
	 * @return the longFieldWithMinValueOnly
	 */
	public long getLongFieldWithMinValueOnly() {
		return longFieldWithMinValueOnly;
	}

	/**
	 * @param longFieldWithMinValueOnly
	 *            the longFieldWithMinValueOnly to set
	 */
	public void setLongFieldWithMinValueOnly(long longFieldWithMinValueOnly) {
		this.longFieldWithMinValueOnly = longFieldWithMinValueOnly;
	}

	/**
	 * @return the longFieldWithMaxValueOnly
	 */
	public long getLongFieldWithMaxValueOnly() {
		return longFieldWithMaxValueOnly;
	}

	/**
	 * @param longFieldWithMaxValueOnly
	 *            the longFieldWithMaxValueOnly to set
	 */
	public void setLongFieldWithMaxValueOnly(long longFieldWithMaxValueOnly) {
		this.longFieldWithMaxValueOnly = longFieldWithMaxValueOnly;
	}

	/**
	 * @return the longFieldWithMinAndMaxValue
	 */
	public long getLongFieldWithMinAndMaxValue() {
		return longFieldWithMinAndMaxValue;
	}

	/**
	 * @param longFieldWithMinAndMaxValue
	 *            the longFieldWithMinAndMaxValue to set
	 */
	public void setLongFieldWithMinAndMaxValue(long longFieldWithMinAndMaxValue) {
		this.longFieldWithMinAndMaxValue = longFieldWithMinAndMaxValue;
	}

	/**
	 * @return the longObjectFieldWithMinValueOnly
	 */
	public Long getLongObjectFieldWithMinValueOnly() {
		return longObjectFieldWithMinValueOnly;
	}

	/**
	 * @param longObjectFieldWithMinValueOnly
	 *            the longObjectFieldWithMinValueOnly to set
	 */
	public void setLongObjectFieldWithMinValueOnly(
			Long longObjectFieldWithMinValueOnly) {
		this.longObjectFieldWithMinValueOnly = longObjectFieldWithMinValueOnly;
	}

	/**
	 * @return the longObjectFieldWithMaxValueOnly
	 */
	public Long getLongObjectFieldWithMaxValueOnly() {
		return longObjectFieldWithMaxValueOnly;
	}

	/**
	 * @param longObjectFieldWithMaxValueOnly
	 *            the longObjectFieldWithMaxValueOnly to set
	 */
	public void setLongObjectFieldWithMaxValueOnly(
			Long longObjectFieldWithMaxValueOnly) {
		this.longObjectFieldWithMaxValueOnly = longObjectFieldWithMaxValueOnly;
	}

	/**
	 * @return the longObjectFieldWithMinAndMaxValue
	 */
	public Long getLongObjectFieldWithMinAndMaxValue() {
		return longObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @param longObjectFieldWithMinAndMaxValue
	 *            the longObjectFieldWithMinAndMaxValue to set
	 */
	public void setLongObjectFieldWithMinAndMaxValue(
			Long longObjectFieldWithMinAndMaxValue) {
		this.longObjectFieldWithMinAndMaxValue = longObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @return the longFieldWithPreciseValue
	 */
	public long getLongFieldWithPreciseValue() {
		return longFieldWithPreciseValue;
	}

	/**
	 * @param longFieldWithPreciseValue
	 *            the longFieldWithPreciseValue to set
	 */
	public void setLongFieldWithPreciseValue(long longFieldWithPreciseValue) {
		this.longFieldWithPreciseValue = longFieldWithPreciseValue;
	}

	/**
	 * @return the longObjectFieldWithPreciseValue
	 */
	public Long getLongObjectFieldWithPreciseValue() {
		return longObjectFieldWithPreciseValue;
	}

	/**
	 * @param longObjectFieldWithPreciseValue
	 *            the longObjectFieldWithPreciseValue to set
	 */
	public void setLongObjectFieldWithPreciseValue(
			Long longObjectFieldWithPreciseValue) {
		this.longObjectFieldWithPreciseValue = longObjectFieldWithPreciseValue;
	}
}
