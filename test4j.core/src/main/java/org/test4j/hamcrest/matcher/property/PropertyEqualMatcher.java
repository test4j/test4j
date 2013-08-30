package org.test4j.hamcrest.matcher.property;

import static org.test4j.hamcrest.matcher.property.reflection.ReflectionComparatorFactory.createRefectionComparator;

import java.util.List;

import org.test4j.hamcrest.matcher.property.difference.Difference;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.test4j.hamcrest.matcher.property.report.DefaultDifferenceReport;
import org.test4j.hamcrest.matcher.property.report.DifferenceReport;
import org.test4j.tools.commons.ArrayHelper;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.reflector.PropertyAccessor;

import ext.test4j.hamcrest.BaseMatcher;
import ext.test4j.hamcrest.Description;

@SuppressWarnings("rawtypes")
public class PropertyEqualMatcher extends BaseMatcher {
	private final Object expected;

	private final String property;

	private final EqMode[] modes;

	public PropertyEqualMatcher(Object expected, String properties, EqMode[] modes) {
		this.expected = expected;
		this.property = properties;
		if (this.property == null) {
			throw new RuntimeException("the properties can't be empty.");
		}
		this.modes = modes;
	}

	public PropertyEqualMatcher(Object expected, String property) {
		this(expected, property, null);
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
		List actuals = PropertyAccessor.getPropertyOfList(array, property, true);
		List expected = ListHelper.toList(this.expected);
		if (ArrayHelper.isCollOrArray(this.expected)) {
			expected = PropertyAccessor.getPropertyOfList(expected, property, false);
		}

		ReflectionComparator reflectionComparator = createRefectionComparator(modes);
		this.difference = reflectionComparator.getDifference(expected, actuals);

		return difference == null;
	}

	private boolean matchPoJo(Object pojo) {
		Object actuals = PropertyAccessor.getPropertyByOgnl(pojo, property, true);
		Object expects = this.expected;
		if (ArrayHelper.isCollOrArray(this.expected) == false) {
			expects = PropertyAccessor.getPropertyByOgnl(this.expected, property, false);
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
			String message = "Incorrect value for properties: " + ArrayHelper.toString(this.property);
			description.appendText(message);
			DifferenceReport differenceReport = new DefaultDifferenceReport();
			description.appendText(differenceReport.createReport(difference));
		}
	}
}
