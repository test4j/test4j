package org.test4j.hamcrest;

import org.test4j.hamcrest.iassert.impl.*;
import org.test4j.hamcrest.iassert.intf.*;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TheStyleAssertion {
	/**
	 * a parameter string will be asserted
	 * 
	 * @return
	 */
	public IStringAssert string() {
		return new StringAssert();
	}

	/**
	 * a parameter boolean will be expected
	 * 
	 * @return
	 */
	public IBooleanAssert bool() {
		return new BooleanAssert();
	}

	/**
	 * a parameter number(integer, long, double,short,float) will be expected
	 * 
	 * @return
	 */
	public INumberAssert number() {
		return new NumberAssert(NumberAssert.class);
	}

	/**
	 * a parameter integer number will be asserted
	 * 
	 * @return
	 */
	public IIntegerAssert integer() {
		return new IntegerAssert();
	}

	/**
	 * a parameter long number will be asserted
	 * 
	 * @return
	 */
	public ILongAssert longnum() {
		return new LongAssert();
	}

	/**
	 * a parameter double number will be asserted
	 * 
	 * @return
	 */
	public IDoubleAssert doublenum() {
		return new DoubleAssert();
	}

	/**
	 * a parameter float number will be asserted
	 * 
	 * @return
	 */
	public IFloatAssert floatnum() {
		return new FloatAssert();
	}

	/**
	 * a parameter short number will be asserted
	 * 
	 * @return
	 */
	public IShortAssert shortnum() {
		return new ShortAssert();
	}

	/**
	 * a parameter character will be asserted
	 * 
	 * @return
	 */
	public ICharacterAssert character() {
		return new CharacterAssert();
	}

	/**
	 * a parameter bite will be asserted
	 * 
	 * @return
	 */
	public IByteAssert bite() {
		return new ByteAssert();
	}

	/**
	 * a parameter array will be asserted
	 * 
	 * @return
	 */
	public IArrayAssert array() {
		return new ArrayAssert();
	}

	/**
	 * a parameter map will be asserted
	 * 
	 * @return
	 */
	public IMapAssert map() {
		return new MapAssert();
	}

	/**
	 * a parameter collection will be asserted
	 * 
	 * @return
	 */
	public ICollectionAssert collection() {
		return new CollectionAssert();
	}

	/**
	 * a parameter general object will be asserted
	 * 
	 * @return
	 */
	public IObjectAssert object() {
		return new ObjectAssert();
	}

	/**
	 * a parameter file will be asserted
	 * 
	 * @return
	 */
	public IFileAssert file() {
		return new FileAssert();
	}

	/**
	 * a parameter calendar will be asserted
	 * 
	 * @return
	 */
	public IDateAssert<Calendar> calendar() {
		return new DateAssert<Calendar>(Calendar.class);
	}

	/**
	 * a parameter date will be asserted
	 * 
	 * @return
	 */
	public IDateAssert<Date> date() {
		return new DateAssert<Date>(Date.class);
	}
}
