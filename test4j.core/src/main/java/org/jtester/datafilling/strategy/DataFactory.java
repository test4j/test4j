package org.jtester.datafilling.strategy;

import org.jtester.datafilling.common.AttributeInfo;

/**
 * This interface defines the contact for data providers.
 */
public interface DataFactory {

	/** It returns a byte/Byte value. */
	public Byte getByte(AttributeInfo attribute);

	/**
	 * It returns a byte/Byte within min and max value (included).
	 * 
	 * @param minValue
	 *            The minimum value for the returned value
	 * @param maxValue
	 *            The maximum value for the returned value
	 * @return A byte/Byte within min and max value (included).
	 */
	public Byte getByteInRange(byte minValue, byte maxValue, AttributeInfo attribute);

	/** It returns a char/Character value. */
	public Character getCharacter(AttributeInfo attribute);

	/**
	 * It returns a char/Character value between min and max value (included).
	 * 
	 * @param minValue
	 *            The minimum value for the returned value
	 * @param maxValue
	 *            The maximum value for the returned value
	 * @return A char/Character value between min and max value (included).
	 */
	public Character getCharacterInRange(char minValue, char maxValue, AttributeInfo attribute);

	/** It returns a double/Double value */
	public Double getDouble(AttributeInfo attribute);

	/**
	 * It returns a double/Double value between min and max value (included).
	 * 
	 * @param minValue
	 *            The minimum value for the returned value
	 * @param maxValue
	 *            The maximum value for the returned value
	 * @return A double/Double value between min and max value (included)
	 */
	public Double getDoubleInRange(double minValue, double maxValue, AttributeInfo attribute);

	/** It returns a float/Float value. */
	public Float getFloat(AttributeInfo attribute);

	/**
	 * It returns a float/Float value between min and max value (included).
	 * 
	 * @param minValue
	 *            The minimum value for the returned value
	 * @param maxValue
	 *            The maximum value for the returned value
	 * @return A float/Float value between min and max value (included).
	 */
	public Float getFloatInRange(float minValue, float maxValue, AttributeInfo attribute);

	/** It returns an int/Integer value. */
	public Integer getInteger(AttributeInfo attribute);

	/**
	 * It returns an int/Integer value between min and max value (included).
	 * 
	 * @param minValue
	 *            The minimum value for the returned value
	 * @param maxValue
	 *            The maximum value for the returned value
	 * @return An int/Integer value between min and max value (included).
	 */
	public int getIntegerInRange(int minValue, int maxValue, AttributeInfo attribute);

	/** It returns a long/Long value. */
	public Long getLong(AttributeInfo attribute);

	/**
	 * It returns a long/Long value between min and max value (included).
	 * 
	 * @param minValue
	 *            The minimum value for the returned value
	 * @param maxValue
	 *            The maximum value for the returned value
	 * @return A long/Long value between min and max value (included).
	 */
	public Long getLongInRange(long minValue, long maxValue, AttributeInfo attribute);

	/** It returns a short/Short value. */
	public Short getShort(AttributeInfo attribute);

	/**
	 * It returns a short/Short value between min and max value (included).
	 * 
	 * @param minValue
	 *            The minimum value for the returned value
	 * @param maxValue
	 *            The maximum value for the returned value
	 * @return A short/Short value between min and max value (included).
	 */
	public Short getShortInRange(short minValue, short maxValue, AttributeInfo attribute);

	/** It returns a string value */
	public String getStringValue(AttributeInfo attribute);

	/**
	 * It returns a String of {@code length} characters.
	 * 
	 * @param length
	 *            The number of characters required in the returned String
	 * @return A String of {@code length} characters
	 */
	public String getStringOfLength(int length, AttributeInfo attribute);
}
