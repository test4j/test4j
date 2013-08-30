package org.jtester.datafilling.annotations;

import java.io.Serializable;

import org.jtester.datafilling.annotations.FillChar;
import org.jtester.datafilling.utils.FillDataTestConstants;

@SuppressWarnings("serial")
public class CharValuePojo implements Serializable {
	@FillChar(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE)
	private char charFieldWithMinValueOnly;

	@FillChar(max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private char charFieldWithMaxValueOnly;

	@FillChar(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE, max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private char charFieldWithMinAndMaxValue;

	@FillChar(value = ' ')
	private char charFieldWithBlankInPreciseValue;

	@FillChar(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE)
	private Character charObjectFieldWithMinValueOnly;

	@FillChar(max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private Character charObjectFieldWithMaxValueOnly;

	@FillChar(min = FillDataTestConstants.NUMBER_INT_MIN_VALUE, max = FillDataTestConstants.NUMBER_INT_ONE_HUNDRED)
	private Character charObjectFieldWithMinAndMaxValue;

	@FillChar(value = FillDataTestConstants.CHAR_PRECISE_VALUE)
	private char charFieldWithPreciseValue;

	/**
	 * @return the charFieldWithMinValueOnly
	 */
	public char getCharFieldWithMinValueOnly() {
		return charFieldWithMinValueOnly;
	}

	/**
	 * @param charFieldWithMinValueOnly
	 *            the charFieldWithMinValueOnly to set
	 */
	public void setCharFieldWithMinValueOnly(char charFieldWithMinValueOnly) {
		this.charFieldWithMinValueOnly = charFieldWithMinValueOnly;
	}

	/**
	 * @return the charFieldWithMaxValueOnly
	 */
	public char getCharFieldWithMaxValueOnly() {
		return charFieldWithMaxValueOnly;
	}

	/**
	 * @param charFieldWithMaxValueOnly
	 *            the charFieldWithMaxValueOnly to set
	 */
	public void setCharFieldWithMaxValueOnly(char charFieldWithMaxValueOnly) {
		this.charFieldWithMaxValueOnly = charFieldWithMaxValueOnly;
	}

	/**
	 * @return the charFieldWithMinAndMaxValue
	 */
	public char getCharFieldWithMinAndMaxValue() {
		return charFieldWithMinAndMaxValue;
	}

	/**
	 * @param charFieldWithMinAndMaxValue
	 *            the charFieldWithMinAndMaxValue to set
	 */
	public void setCharFieldWithMinAndMaxValue(char charFieldWithMinAndMaxValue) {
		this.charFieldWithMinAndMaxValue = charFieldWithMinAndMaxValue;
	}

	/**
	 * @return the charObjectFieldWithMinValueOnly
	 */
	public Character getCharObjectFieldWithMinValueOnly() {
		return charObjectFieldWithMinValueOnly;
	}

	/**
	 * @param charObjectFieldWithMinValueOnly
	 *            the charObjectFieldWithMinValueOnly to set
	 */
	public void setCharObjectFieldWithMinValueOnly(Character charObjectFieldWithMinValueOnly) {
		this.charObjectFieldWithMinValueOnly = charObjectFieldWithMinValueOnly;
	}

	/**
	 * @return the charObjectFieldWithMaxValueOnly
	 */
	public Character getCharObjectFieldWithMaxValueOnly() {
		return charObjectFieldWithMaxValueOnly;
	}

	/**
	 * @param charObjectFieldWithMaxValueOnly
	 *            the charObjectFieldWithMaxValueOnly to set
	 */
	public void setCharObjectFieldWithMaxValueOnly(Character charObjectFieldWithMaxValueOnly) {
		this.charObjectFieldWithMaxValueOnly = charObjectFieldWithMaxValueOnly;
	}

	/**
	 * @return the charObjectFieldWithMinAndMaxValue
	 */
	public Character getCharObjectFieldWithMinAndMaxValue() {
		return charObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @param charObjectFieldWithMinAndMaxValue
	 *            the charObjectFieldWithMinAndMaxValue to set
	 */
	public void setCharObjectFieldWithMinAndMaxValue(Character charObjectFieldWithMinAndMaxValue) {
		this.charObjectFieldWithMinAndMaxValue = charObjectFieldWithMinAndMaxValue;
	}

	/**
	 * @return the charFieldWithPreciseValue
	 */
	public char getCharFieldWithPreciseValue() {
		return charFieldWithPreciseValue;
	}

	/**
	 * @param charFieldWithPreciseValue
	 *            the charFieldWithPreciseValue to set
	 */
	public void setCharFieldWithPreciseValue(char charFieldWithPreciseValue) {
		this.charFieldWithPreciseValue = charFieldWithPreciseValue;
	}

	/**
	 * @return the charFieldWithBlankInPreciseValue
	 */
	public char getCharFieldWithBlankInPreciseValue() {
		return charFieldWithBlankInPreciseValue;
	}

	/**
	 * @param charFieldWithBlankInPreciseValue
	 *            the charFieldWithBlankInPreciseValue to set
	 */
	public void setCharFieldWithBlankInPreciseValue(char charFieldWithBlankInPreciseValue) {
		this.charFieldWithBlankInPreciseValue = charFieldWithBlankInPreciseValue;
	}
}
