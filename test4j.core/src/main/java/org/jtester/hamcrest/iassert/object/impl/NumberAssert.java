package org.jtester.hamcrest.iassert.object.impl;

import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.core.IsEqual;
import org.jtester.hamcrest.iassert.common.impl.ComparableAssert;
import org.jtester.hamcrest.iassert.common.intf.IAssert;
import org.jtester.hamcrest.iassert.object.intf.INumberAssert;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class NumberAssert<T extends Number & Comparable<T>, E extends INumberAssert<T, ?>> extends
		ComparableAssert<T, E> implements INumberAssert<T, E> {

	public NumberAssert(Class<? extends IAssert<?, ?>> clazE) {
		super(clazE);
	}

	public NumberAssert(T value, Class<? extends IAssert<?, ?>> clazE) {
		super(value, clazE);
	}

	public NumberAssert(T value, Class<? extends IAssert<?, ?>> clazE, Class valueClazz) {
		super(value, clazE);
		this.valueClaz = valueClazz;
	}

	/**
	 * 因为IsEqual是基于equal比较值相等的，int和long即使值一样，equal也是不等的<br>
	 * 这里把它们强制转换为同一种类型比较
	 */
	public E isEqualTo(Number expected) {
		Object _expected = expected;
		if (expected == null) {
			return super.isEqualTo((T) expected);
		}
		if (this.valueClaz == int.class || this.valueClaz == Integer.class) {
			_expected = Integer.parseInt(expected.toString());
		}
		if (this.valueClaz == short.class || this.valueClaz == Short.class) {
			_expected = Short.parseShort(expected.toString());
		}
		if (this.valueClaz == long.class || this.valueClaz == Long.class) {
			_expected = Long.parseLong(expected.toString());
		}
		if (this.valueClaz == float.class || this.valueClaz == Float.class) {
			_expected = Float.parseFloat(expected.toString());
		}
		if (this.valueClaz == double.class || this.valueClaz == Double.class) {
			_expected = Double.parseDouble(expected.toString());
		}

		Matcher<? super T> matcher = IsEqual.equalTo(_expected);
		return this.assertThat(matcher);
	}
}
