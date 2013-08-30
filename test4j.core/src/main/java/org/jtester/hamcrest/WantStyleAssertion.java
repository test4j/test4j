package org.jtester.hamcrest;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.jtester.hamcrest.iassert.object.impl.ArrayAssert;
import org.jtester.hamcrest.iassert.object.impl.BooleanAssert;
import org.jtester.hamcrest.iassert.object.impl.ByteAssert;
import org.jtester.hamcrest.iassert.object.impl.CharacterAssert;
import org.jtester.hamcrest.iassert.object.impl.CollectionAssert;
import org.jtester.hamcrest.iassert.object.impl.DateAssert;
import org.jtester.hamcrest.iassert.object.impl.DoubleAssert;
import org.jtester.hamcrest.iassert.object.impl.FileAssert;
import org.jtester.hamcrest.iassert.object.impl.FloatAssert;
import org.jtester.hamcrest.iassert.object.impl.IntegerAssert;
import org.jtester.hamcrest.iassert.object.impl.JSONAssert;
import org.jtester.hamcrest.iassert.object.impl.LongAssert;
import org.jtester.hamcrest.iassert.object.impl.MapAssert;
import org.jtester.hamcrest.iassert.object.impl.NumberAssert;
import org.jtester.hamcrest.iassert.object.impl.ObjectAssert;
import org.jtester.hamcrest.iassert.object.impl.ShortAssert;
import org.jtester.hamcrest.iassert.object.impl.StringAssert;
import org.jtester.hamcrest.iassert.object.intf.IArrayAssert;
import org.jtester.hamcrest.iassert.object.intf.IBooleanAssert;
import org.jtester.hamcrest.iassert.object.intf.IByteAssert;
import org.jtester.hamcrest.iassert.object.intf.ICharacterAssert;
import org.jtester.hamcrest.iassert.object.intf.ICollectionAssert;
import org.jtester.hamcrest.iassert.object.intf.IDateAssert;
import org.jtester.hamcrest.iassert.object.intf.IDoubleAssert;
import org.jtester.hamcrest.iassert.object.intf.IFileAssert;
import org.jtester.hamcrest.iassert.object.intf.IFloatAssert;
import org.jtester.hamcrest.iassert.object.intf.IIntegerAssert;
import org.jtester.hamcrest.iassert.object.intf.IJSONAssert;
import org.jtester.hamcrest.iassert.object.intf.ILongAssert;
import org.jtester.hamcrest.iassert.object.intf.IMapAssert;
import org.jtester.hamcrest.iassert.object.intf.INumberAssert;
import org.jtester.hamcrest.iassert.object.intf.IObjectAssert;
import org.jtester.hamcrest.iassert.object.intf.IShortAssert;
import org.jtester.hamcrest.iassert.object.intf.IStringAssert;
import org.jtester.tools.commons.ArrayHelper;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class WantStyleAssertion {
	/**
	 * 字符串断言
	 * 
	 * @param value
	 *            字符串变量
	 * @return
	 */
	public IStringAssert string(String value) {
		return new StringAssert(value);
	}

	/**
	 * 布尔变量断言
	 * 
	 * @param value
	 *            a boolean variable
	 * @return
	 */
	public IBooleanAssert bool(Boolean value) {
		return new BooleanAssert(value);
	}

	/**
	 * integer变量断言
	 * 
	 * @param value
	 *            a integer variable
	 * @return
	 */
	public IIntegerAssert number(Integer value) {
		return new IntegerAssert(value);
	}

	/**
	 * short变量断言
	 * 
	 * @param value
	 *            a short variable
	 * @return
	 */
	public IShortAssert number(Short value) {
		return new ShortAssert(value);
	}

	/**
	 * long变量断言
	 * 
	 * @param value
	 *            a long variable
	 * @return
	 */
	public ILongAssert number(Long value) {
		return new LongAssert(value);
	}

	/**
	 * double变量断言
	 * 
	 * @param value
	 *            a double variable
	 * @return
	 */
	public IDoubleAssert number(Double value) {
		return new DoubleAssert(value);
	}

	/**
	 * float变量断言
	 * 
	 * @param value
	 *            a float variable
	 * @return
	 */
	public IFloatAssert number(Float value) {
		return new FloatAssert(value);
	}

	/**
	 * bigdecimal断言
	 * 
	 * @param decimal
	 * @return
	 */
	public INumberAssert<BigDecimal, ?> number(BigDecimal decimal) {
		return new NumberAssert(decimal, NumberAssert.class, BigDecimal.class);
	}

	/**
	 * bigInteger断言
	 * 
	 * @param decimal
	 * @return
	 */
	public INumberAssert<BigInteger, ?> number(BigInteger bigint) {
		return new NumberAssert(bigint, NumberAssert.class, BigInteger.class);
	}

	/**
	 * Byte断言
	 * 
	 * @param decimal
	 * @return
	 */
	public INumberAssert<Byte, ?> number(Byte byt) {
		return new NumberAssert(byt, NumberAssert.class, Byte.class);
	}

	/**
	 * char变量断言
	 * 
	 * @param value
	 *            a character variable
	 * @return
	 */
	public ICharacterAssert character(Character value) {
		return new CharacterAssert(value);
	}

	/**
	 * byte变量断言
	 * 
	 * @param value
	 *            a byte variable
	 * @return
	 */
	public IByteAssert bite(Byte value) {
		return new ByteAssert(value);
	}

	/**
	 * 数组变量断言
	 * 
	 * @param value
	 *            a array variable
	 * @return
	 */
	public <T extends Object> IArrayAssert array(T value[]) {
		return new ArrayAssert(value);
	}

	/**
	 * collection变量断言
	 * 
	 * @param value
	 *            a collection argument
	 * @return
	 */
	public ICollectionAssert list(Collection collection) {
		return new CollectionAssert(collection);
	}

	/**
	 * 数组变量断言
	 * 
	 * @param value
	 *            a array variable
	 * @return
	 */
	public <T extends Object> IArrayAssert list(T value[]) {
		return new ArrayAssert(value);
	}

	/**
	 * 布尔值数组变量断言
	 * 
	 * @param value
	 *            a boolean array
	 * @return
	 */
	public IArrayAssert list(boolean value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * byte数组变量断言
	 * 
	 * @param value
	 *            a byte array
	 * @return
	 */
	public IArrayAssert list(byte value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * character数组变量断言
	 * 
	 * @param value
	 *            a character array
	 * @return
	 */
	public IArrayAssert list(char value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * short数值类型数组变量断言
	 * 
	 * @param value
	 *            a short array
	 * @return
	 */
	public IArrayAssert list(short value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * integer数值类型数组变量断言
	 * 
	 * @param value
	 *            a integer array
	 * @return
	 */
	public IArrayAssert list(int value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * long数值类型数组变量断言
	 * 
	 * @param value
	 *            a long array
	 * @return
	 */
	public IArrayAssert list(long value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * float数值类型数组变量断言
	 * 
	 * @param value
	 *            a float array
	 * @return
	 */
	public IArrayAssert list(float value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * double数值类型数组变量断言
	 * 
	 * @param value
	 *            a double array
	 * @return
	 */
	public IArrayAssert list(double value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * 布尔值数组变量断言
	 * 
	 * @param value
	 *            a boolean array
	 * @return
	 */
	public IArrayAssert array(boolean value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * byte数组变量断言
	 * 
	 * @param value
	 *            a byte array
	 * @return
	 */
	public IArrayAssert array(byte value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * character数组变量断言
	 * 
	 * @param value
	 *            a character array
	 * @return
	 */
	public IArrayAssert array(char value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * short数值类型数组变量断言
	 * 
	 * @param value
	 *            a short array
	 * @return
	 */
	public IArrayAssert array(short value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * integer数值类型数组变量断言
	 * 
	 * @param value
	 *            a integer array
	 * @return
	 */
	public IArrayAssert array(int value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * long数值类型数组变量断言
	 * 
	 * @param value
	 *            a long array
	 * @return
	 */
	public IArrayAssert array(long value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * float数值类型数组变量断言
	 * 
	 * @param value
	 *            a float array
	 * @return
	 */
	public IArrayAssert array(float value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * double数值类型数组变量断言
	 * 
	 * @param value
	 *            a double array
	 * @return
	 */
	public IArrayAssert array(double value[]) {
		return new ArrayAssert(ArrayHelper.toArray(value));
	}

	/**
	 * map变量断言
	 * 
	 * @param value
	 *            a map argument
	 * @return
	 */
	public IMapAssert map(Map map) {
		return new MapAssert(map);
	}

	/**
	 * collection变量断言
	 * 
	 * @param value
	 *            a collection argument
	 * @return
	 */
	public ICollectionAssert collection(Collection collection) {
		return new CollectionAssert(collection);
	}

	/**
	 * 通用object对象断言
	 * 
	 * @param value
	 *            a object argument
	 * @return
	 */
	public IObjectAssert object(Object bean) {
		return new ObjectAssert(bean);
	}

	/**
	 * 一个永远失败的断言
	 */
	public void fail() {
		// assert true == false;
		this.bool(true).isEqualTo(false);
	}

	/**
	 * 一个永远失败的断言
	 * 
	 * @param message
	 *            失败后的提示信息
	 */
	public void fail(String message) {
		// assert true == false : message;
		this.bool(true).isEqualTo(message, false);
	}

	/**
	 * a file argument asserter
	 * 
	 * @param filename
	 * @return
	 */
	public IFileAssert file(String filename) {
		File file = new File(filename.replace("file:", ""));
		return new FileAssert(file);
	}

	/**
	 * 文件变量断言
	 * 
	 * @param file
	 * @return
	 */
	public IFileAssert file(File file) {
		return new FileAssert(file);
	}

	/**
	 * calendar变量断言
	 * 
	 * @param cal
	 * @return
	 */
	public IDateAssert<Calendar> calendar(Calendar cal) {
		return new DateAssert<Calendar>(cal, Calendar.class);
	}

	/**
	 * 日期变量断言
	 * 
	 * @param date
	 * @return
	 */
	public IDateAssert<Date> date(Date date) {
		return new DateAssert<Date>(date, Date.class);
	}

	public IJSONAssert json(String string) {
		return new JSONAssert(string, IJSONAssert.class);
	}
}
