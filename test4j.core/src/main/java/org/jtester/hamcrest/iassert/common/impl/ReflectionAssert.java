package org.jtester.hamcrest.iassert.common.impl;

import org.jtester.hamcrest.iassert.common.intf.IAssert;
import org.jtester.hamcrest.iassert.common.intf.IReflectionAssert;
import org.jtester.hamcrest.matcher.property.MapPropertyEqaulMatcher;
import org.jtester.hamcrest.matcher.property.PropertiesEqualMatcher;
import org.jtester.hamcrest.matcher.property.PropertyEqualMatcher;
import org.jtester.hamcrest.matcher.property.PropertyEqualStringMatcher;
import org.jtester.hamcrest.matcher.property.PropertyItemMatcher;
import org.jtester.hamcrest.matcher.property.ReflectionEqualMatcher;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.module.ICore.DataMap;

import ext.jtester.hamcrest.Matcher;

@SuppressWarnings("rawtypes")
public class ReflectionAssert<T, E extends IAssert> extends ListHasItemsAssert<T, E> implements IReflectionAssert<E> {

	public ReflectionAssert(Class<? extends IAssert> clazE) {
		super(clazE);
	}

	public ReflectionAssert(T value, Class<? extends IAssert> clazE) {
		super(value, clazE);
	}

	public E propertyEq(String[] properties, Object expected, EqMode... modes) {
		if (expected instanceof Matcher) {
			throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
		}
		PropertiesEqualMatcher matcher = new PropertiesEqualMatcher(expected, properties, modes);
		return this.assertThat(matcher);
	}

	public E reflectionEq(Object expected, EqMode... modes) {
		return this.eqByReflect(expected, modes);
	}

	public E eqByReflect(Object expected, EqMode... modes) {
		if (expected instanceof Matcher) {
			throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
		} else {
			ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected, modes);
			return this.assertThat(matcher);
		}
	}

	public E propertyMatch(String property, Matcher matcher) {
		PropertyItemMatcher _matcher = new PropertyItemMatcher(property, matcher);
		return this.assertThat(_matcher);
	}

	public E propertyEq(String property, Object expected, EqMode... modes) {
		if (expected instanceof Matcher) {
			throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
		} else {
			PropertyEqualMatcher matcher = new PropertyEqualMatcher(expected, property, modes);
			return this.assertThat(matcher);
		}
	}

	public E propertyEq(String property, String expected, StringMode mode, StringMode... more) {
		StringMode[] modes = new StringMode[more.length + 1];
		int index = 0;
		modes[index++] = mode;
		for (StringMode item : more) {
			modes[index++] = item;
		}
		PropertyEqualStringMatcher matcher = new PropertyEqualStringMatcher(expected, property, modes);
		return this.assertThat(matcher);
	}

	public E eqIgnoreDefault(Object expected) {
		if (expected instanceof Matcher) {
			throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
		}
		ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected, new EqMode[] { EqMode.IGNORE_DEFAULTS });
		return this.assertThat(matcher);
	}

	public E eqIgnoreOrder(Object expected) {
		if (expected instanceof Matcher) {
			throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
		}
		ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected, new EqMode[] { EqMode.IGNORE_ORDER });
		return this.assertThat(matcher);
	}

	public E eqIgnoreAll(Object expected) {
		if (expected instanceof Matcher) {
			throw new AssertionError("please use method[propertyMatch(String, Matcher)]");
		}
		ReflectionEqualMatcher matcher = new ReflectionEqualMatcher(expected, new EqMode[] { EqMode.IGNORE_ORDER,
				EqMode.IGNORE_DEFAULTS, EqMode.IGNORE_DATES });
		return this.assertThat(matcher);
	}

	public E reflectionEqMap(DataMap expected, EqMode... modes) {
		return this.propertyEqMap(expected, modes);
	}

	public E propertyEqMap(DataMap expected, EqMode... modes) {
		MapPropertyEqaulMatcher matcher = new MapPropertyEqaulMatcher(expected, modes);
		return this.assertThat(matcher);
	}
}
