package org.jtester.hamcrest.iassert.common.impl;

import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.core.AllOf;
import org.jtester.hamcrest.iassert.common.intf.IAssert;
import org.jtester.hamcrest.iassert.common.intf.IComparableAssert;
import org.jtester.hamcrest.matcher.mockito.GreaterOrEqual;
import org.jtester.hamcrest.matcher.mockito.GreaterThan;
import org.jtester.hamcrest.matcher.mockito.LessOrEqual;
import org.jtester.hamcrest.matcher.mockito.LessThan;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ComparableAssert<T, E extends IAssert> extends AllAssert<T, E> implements IComparableAssert<T, E> {

	public ComparableAssert(Class<? extends IAssert> clazE) {
		super(clazE);
	}

	public ComparableAssert(T value, Class<? extends IAssert> clazE) {
		super(value, clazE);
	}

	public E isGe(T min) {
		assetCanComparable(min);
		GreaterOrEqual matcher = new GreaterOrEqual((Comparable) min);
		return this.assertThat(matcher);
	}

	public E isGt(T min) {
		assetCanComparable(min);
		GreaterThan matcher = new GreaterThan((Comparable) min);
		return this.assertThat(matcher);
	}

	public E isLe(T max) {
		assetCanComparable(max);
		LessOrEqual matcher = new LessOrEqual((Comparable) max);
		return this.assertThat(matcher);
	}

	public E isLt(T max) {
		assetCanComparable(max);
		LessThan matcher = new LessThan((Comparable) max);
		return this.assertThat(matcher);
	}

	public E isBetween(T min, T max) {
		assetCanComparable(min);
		assetCanComparable(max);
		if (((Comparable) min).compareTo((Comparable) max) > 0) {
			throw new AssertionError(String.format("arg1[%s] must less than arg2[%s]", min, max));
		}
		GreaterOrEqual geq = new GreaterOrEqual((Comparable) min);
		LessOrEqual leq = new LessOrEqual((Comparable) max);
		Matcher<?> matcher = AllOf.allOf(geq, leq);
		return this.assertThat(matcher);
	}

	private void assetCanComparable(T o) {
		if (o != null && !(o instanceof Comparable)) {
			throw new AssertionError("the object[" + o + "] isn't a comparable object.");
		}
	}

	public E isLessThan(T max) {
		return this.isLt(max);
	}

	public E isLessEqual(T max) {
		return this.isLe(max);
	}

	public E isGreaterThan(T min) {
		return this.isGt(min);
	}

	public E isGreaterEqual(T min) {
		return this.isGe(min);
	}
}
