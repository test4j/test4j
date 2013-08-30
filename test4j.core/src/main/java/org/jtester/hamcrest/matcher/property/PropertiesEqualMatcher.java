package org.jtester.hamcrest.matcher.property;

import static org.jtester.hamcrest.matcher.property.reflection.ReflectionComparatorFactory.createRefectionComparator;

import java.util.List;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.jtester.hamcrest.matcher.property.report.DefaultDifferenceReport;
import org.jtester.hamcrest.matcher.property.report.DifferenceReport;
import org.jtester.tools.commons.ArrayHelper;
import org.jtester.tools.commons.ListHelper;
import org.jtester.tools.reflector.PropertyAccessor;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;

@SuppressWarnings("rawtypes")
public class PropertiesEqualMatcher extends BaseMatcher {
	private final Object expected;

	private final String[] properties;

	private final EqMode[] modes;

	public PropertiesEqualMatcher(Object expected, String[] properties, EqMode[] modes) {
		this.expected = expected;
		this.properties = properties;
		if (this.properties == null || this.properties.length == 0) {
			throw new RuntimeException("the properties can't be empty.");
		}
		this.modes = modes;
	}

	public PropertiesEqualMatcher(Object expected, String[] properties) {
		this(expected, properties, null);
	}

	public boolean matches(Object actual) {
		if (actual == null) {
			buff.append("properties equals matcher, the actual value can't be null.");
			return false;
		}
		if (ArrayHelper.isCollOrArray(actual)) {
			List list = ListHelper.toList(actual);
			return this.matchList(list);
		} else {
			return this.matchPoJo(actual);
		}
	}

	private boolean matchList(List array) {
		if (ArrayHelper.isCollOrArray(this.expected) == false) {
			buff.append("property of List/Array equals matcher, the expected value should be an Array or Collection, but is a type[");
			buff.append(this.expected == null ? "null" : this.expected.getClass().getName());
			buff.append("] object.\n");
			return false;
		}
		List expected = ListHelper.toList(this.expected);
		if (expected.size() > array.size()) {
			buff.append("the size of expected array is greater then the size of actual array.");
		}
		List<List> actuals = PropertyAccessor.getPropertiesOfList(array, properties, true);
		List<List> expects = PropertyAccessor.getPropertiesOfList(expected, properties, false);

		ReflectionComparator reflectionComparator = createRefectionComparator(modes);
		this.difference = reflectionComparator.getDifference(expects, actuals);

		return difference == null;
	}

	private boolean matchPoJo(Object pojo) {
		List actuals = PropertyAccessor.getPropertiesOfPoJo(pojo, properties, true);
		List expects = null;
		if (ArrayHelper.isCollOrArray(this.expected)) {
			expects = ListHelper.toList(this.expected);
		} else {
			expects = PropertyAccessor.getPropertiesOfPoJo(this.expected, properties, false);
		}
		ReflectionComparator reflectionComparator = createRefectionComparator(modes);
		this.difference = reflectionComparator.getDifference(expects, actuals);

		return difference == null;
	}

	private final StringBuilder buff = new StringBuilder();

	private Difference difference;

	public void describeTo(Description description) {
		description.appendText(buff.toString());
		if (difference != null) {
			String message = "Incorrect value for properties: " + ArrayHelper.toString(this.properties);
			description.appendText(message);
			DifferenceReport differenceReport = new DefaultDifferenceReport();
			description.appendText(differenceReport.createReport(difference));
		}
	}
}
