package org.jtester.datafilling.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@SuppressWarnings("serial")
public class OneDimensionalTestPojo implements Serializable {

	private boolean booleanField;

	private Boolean booleanObjectField;

	private byte byteField;

	private Byte byteObjectField;

	private short shortField;

	private Short shortObjectField;

	private char charField;

	private Character charObjectField;

	private int intField;

	private Integer intObjectField;

	private long longField;

	private Long longObjectField;

	private float floatField;

	private Float floatObjectField;

	private double doubleField;

	private Double doubleObjectField;

	private String stringField;

	private Object objectField;

	private Calendar calendarField;

	private Date dateField;

	private Random[] randomArray;

	private int[] intArray;

	private boolean[] booleanArray;

	private BigDecimal bigDecimalField;

	public OneDimensionalTestPojo() {

	}

	public int getIntField() {
		return intField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}

	public long getLongField() {
		return longField;
	}

	public void setLongField(long longField) {
		this.longField = longField;
	}

	public float getFloatField() {
		return floatField;
	}

	public void setFloatField(float floatField) {
		this.floatField = floatField;
	}

	public double getDoubleField() {
		return doubleField;
	}

	public void setDoubleField(double doubleField) {
		this.doubleField = doubleField;
	}

	public String getStringField() {
		return stringField;
	}

	public void setStringField(String stringField) {
		this.stringField = stringField;
	}

	public Object getObjectField() {
		return objectField;
	}

	public void setObjectField(Object objectField) {
		this.objectField = objectField;
	}

	public Calendar getCalendarField() {
		return calendarField;
	}

	public void setCalendarField(Calendar calendarField) {
		this.calendarField = calendarField;
	}

	public Date getDateField() {
		return dateField;
	}

	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}

	public boolean isBooleanField() {
		return booleanField;
	}

	public void setBooleanField(boolean booleanField) {
		this.booleanField = booleanField;
	}

	public Random[] getRandomArray() {
		return randomArray;
	}

	public void setRandomArray(Random[] randomArray) {
		this.randomArray = randomArray;
	}

	public Boolean getBooleanObjectField() {
		return booleanObjectField;
	}

	public void setBooleanObjectField(Boolean booleanObjectField) {
		this.booleanObjectField = booleanObjectField;
	}

	public byte getByteField() {
		return byteField;
	}

	public void setByteField(byte byteField) {
		this.byteField = byteField;
	}

	public Byte getByteObjectField() {
		return byteObjectField;
	}

	public void setByteObjectField(Byte byteObjectField) {
		this.byteObjectField = byteObjectField;
	}

	public short getShortField() {
		return shortField;
	}

	public void setShortField(short shortField) {
		this.shortField = shortField;
	}

	public Short getShortObjectField() {
		return shortObjectField;
	}

	public void setShortObjectField(Short shortObjectField) {
		this.shortObjectField = shortObjectField;
	}

	public char getCharField() {
		return charField;
	}

	public void setCharField(char charField) {
		this.charField = charField;
	}

	public Character getCharObjectField() {
		return charObjectField;
	}

	public void setCharObjectField(Character charObjectField) {
		this.charObjectField = charObjectField;
	}

	public Integer getIntObjectField() {
		return intObjectField;
	}

	public void setIntObjectField(Integer intObjectField) {
		this.intObjectField = intObjectField;
	}

	public Long getLongObjectField() {
		return longObjectField;
	}

	public void setLongObjectField(Long longObjectField) {
		this.longObjectField = longObjectField;
	}

	public Float getFloatObjectField() {
		return floatObjectField;
	}

	public void setFloatObjectField(Float floatObjectField) {
		this.floatObjectField = floatObjectField;
	}

	public Double getDoubleObjectField() {
		return doubleObjectField;
	}

	public void setDoubleObjectField(Double doubleObjectField) {
		this.doubleObjectField = doubleObjectField;
	}

	public int[] getIntArray() {
		return intArray;
	}

	public void setIntArray(int[] intArray) {
		this.intArray = intArray;
	}

	public boolean[] getBooleanArray() {
		return booleanArray;
	}

	public void setBooleanArray(boolean[] booleanArray) {
		this.booleanArray = booleanArray;
	}

	public BigDecimal getBigDecimalField() {
		return bigDecimalField;
	}

	public void setBigDecimalField(BigDecimal bigDecimalField) {
		this.bigDecimalField = bigDecimalField;
	}

}
