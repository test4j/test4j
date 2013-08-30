package org.jtester.hamcrest.matcher.property;

import static org.jtester.hamcrest.matcher.property.reflection.ReflectionComparatorFactory.createRefectionComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jtester.hamcrest.matcher.property.difference.Difference;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.hamcrest.matcher.property.reflection.ReflectionComparator;
import org.jtester.hamcrest.matcher.property.report.DefaultDifferenceReport;
import org.jtester.hamcrest.matcher.property.report.DifferenceReport;
import org.jtester.module.ICore.DataMap;
import org.jtester.tools.commons.ArrayHelper;
import org.jtester.tools.commons.ListHelper;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.jtester.tools.reflector.PropertyAccessor;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.StringDescription;

/**
 * 把实际对象按照Map中的key值取出来，进行反射比较
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapListPropertyEqaulMatcher extends BaseMatcher {

	private final List<DataMap> expected;

	private EqMode[] modes;

	public MapListPropertyEqaulMatcher(List<DataMap> expected, EqMode[] modes) {
		this.expected = expected;
		this.modes = modes;
		if (expected == null) {
			throw new AssertionError("MapPropertyEqaulMatcher, the expected map can't be null.");
		}
	}

	public boolean matches(Object actual) {
		if (actual == null) {
			this.self.appendText("MapPropertyEqaulMatcher, the actual object can't be null or list/array.");
			return false;
		}
		if (ArrayHelper.isCollOrArray(actual) == false) {
			this.self.appendText("MapPropertyEqaulMatcher, the actual object must be an array or a list.");
			return false;
		}
		List list = ListHelper.toList(actual);

		if (this.expected.size() != list.size()) {
			this.self.appendText("MapPropertyEqaulMatcher, the size ofexpeced object is " + this.expected.size()
					+ ", but the size of actual list is " + list.size() + ".");
			return false;
		}
		Set<String> keys = this.getAllKeys();

		List<Map<String, ?>> actuals = getObjectArrayFromList(list, keys, false);
		List<Map<String, ?>> expecteds = getObjectArrayFromList(this.expected, keys, true);

		ReflectionComparator reflectionComparator = createRefectionComparator(modes);
		this.difference = reflectionComparator.getDifference(expecteds, actuals);
		return difference == null;
	}

	private List<Map<String, ?>> getObjectArrayFromList(List list, Set<String> keys, boolean isExpected) {
		List<Map<String, ?>> result = new ArrayList<Map<String, ?>>();
		for (Object o : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (String key : keys) {
				try {
					Object value = PropertyAccessor.getPropertyByOgnl(o, key, true);
					map.put(key, value);
				} catch (NoSuchFieldRuntimeException e) {
					if (isExpected) {
						map.put(key, null);
					} else {
						throw e;
					}
				}
			}
			result.add(map);
		}
		return result;
	}

	private Set<String> getAllKeys() {
		Set<String> keys = new HashSet<String>();
		for (Map map : this.expected) {
			Set set = map.keySet();
			keys.addAll(set);
		}
		return keys;
	}

	private StringDescription self = new StringDescription();

	private Difference difference;

	public void describeTo(Description description) {
		description.appendText(self.toString());
		if (difference != null) {
			DifferenceReport differenceReport = new DefaultDifferenceReport();
			description.appendText(differenceReport.createReport(difference));
		}
	}
}
