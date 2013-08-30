package org.jtester.datafilling.annotations;

import java.io.Serializable;

import org.jtester.datafilling.annotations.FillByte;
import org.jtester.datafilling.utils.FillDataTestConstants;

public class ByteValuePojo implements Serializable {
	private static final long serialVersionUID = 1L;

	@FillByte(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE)
	private byte byteFieldWithMinValueOnly;

	@FillByte(max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private byte byteFieldWithMaxValueOnly;

	@FillByte(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE, max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private byte byteFieldWithMinAndMaxValue;

	@FillByte(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE)
	private Byte byteObjectFieldWithMinValueOnly;

	@FillByte(max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private Byte byteObjectFieldWithMaxValueOnly;

	@FillByte(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE, max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private Byte byteObjectFieldWithMinAndMaxValue;

	@FillByte(value = FillDataTestConstants.BYTE_PRECISE_VALUE)
	private byte byteFieldWithPreciseValue;

	/**
	 * @return the byteFieldWithMinValueOnly
	 */
	public byte getByteFieldWithMinValueOnly() {
		return byteFieldWithMinValueOnly;
	}

	/**
	 * @param byteFieldWithMinValueOnly
	 *            the byteFieldWithMinValueOnly to set
	 */
	public void setByteFieldWithMinValueOnly(byte byteFieldWithMinValueOnly) {
		this.byteFieldWithMinValueOnly = byteFieldWithMinValueOnly;
	}

	/**
	 * @return the byteFieldWithMaxValueOnly
	 */
	public byte getByteFieldWithMaxValueOnly() {
		return byteFieldWithMaxValueOnly;
	}

	/**
	 * @param byteFieldWithMaxValueOnly
	 *            the byteFieldWithMaxValueOnly to set
	 */
	public void setByteFieldWithMaxValueOnly(byte byteFieldWithMaxValueOnly) {
		this.byteFieldWithMaxValueOnly = byteFieldWithMaxValueOnly;
	}

	/**
	 * @return the byteFieldWithMinAndMaxValue
	 */
	public byte getByteFieldWithMinAndMaxValue() {
		return byteFieldWithMinAndMaxValue;
	}

	/**
	 * @param byteFieldWithMinAndMaxValue
	 *            the byteFieldWithMinAndMaxValue to set
	 */
	public void setByteFieldWithMinAndMaxValue(byte byteFieldWithMinAndMaxValue) {
		this.byteFieldWithMinAndMaxValue = byteFieldWithMinAndMaxValue;
	}

	/**
	 * @return the byteObjectFieldWithMinValueOnly
	 */
	public Byte getByteObjectFieldWithMinValueOnly() {
		return byteObjectFieldWithMinValueOnly;
	}

	/**
	 * @param byteObjectFieldWithMinValueOnly
	 *            the byteObjectFieldWithMinValueOnly to set
	 */
	public void setByteObjectFieldWithMinValueOnly(Byte byteObjectFieldWithMinValueOnly) {
		this.byteObjectFieldWithMinValueOnly = byteObjectFieldWithMinValueOnly;
	}

	/**
	 * @return the byteObjectFieldWithMaxValueOnly
	 */
	public Byte getByteObjectFieldWithMaxValueOnly() {
		return byteObjectFieldWithMaxValueOnly;
	}

	/**
	 * @param byteObjectFieldWithMaxValueOnly
	 *            the byteObjectFieldWithMaxValueOnly to set
	 */
	public void setByteObjectFieldWithMaxValueOnly(Byte byteObjectFieldWithMaxValueOnly) {
		this.byteObjectFieldWithMaxValueOnly = byteObjectFieldWithMaxValueOnly;
	}

	/**
	 * @return the byteObjectFieldWithMinAndMaxValue
	 */
	public Byte getByteObjectFieldWithMinAndMaxValue() {
		return byteObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @param byteObjectFieldWithMinAndMaxValue
	 *            the byteObjectFieldWithMinAndMaxValue to set
	 */
	public void setByteObjectFieldWithMinAndMaxValue(Byte byteObjectFieldWithMinAndMaxValue) {
		this.byteObjectFieldWithMinAndMaxValue = byteObjectFieldWithMinAndMaxValue;
	}

	public byte getByteFieldWithPreciseValue() {
		return byteFieldWithPreciseValue;
	}

	public void setByteFieldWithPreciseValue(byte byteFieldWithPreciseValue) {
		this.byteFieldWithPreciseValue = byteFieldWithPreciseValue;
	}
}
