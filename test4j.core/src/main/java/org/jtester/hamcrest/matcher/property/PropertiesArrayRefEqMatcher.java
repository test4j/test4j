package org.jtester.hamcrest.matcher.property;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;

import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.tools.commons.StringHelper;
import org.jtester.tools.reflector.PropertyAccessor;

/**
 * 集合（数组）中对象的多个属性值集合(二维数组)，反射值与期望值相等
 * 
 * @author darui.wudr
 * 
 */
public class PropertiesArrayRefEqMatcher extends BaseMatcher<Object> {
	private String[] properties;
	private Object[][] expected;

	ReflectionEqualMatcher matcher;

	public PropertiesArrayRefEqMatcher(String[] properties, Object[][] expected, EqMode... modes) {
		if (properties == null || properties.length == 0) {
			throw new RuntimeException("properties list can't be null!");
		}
		this.properties = properties;
		this.expected = expected;
		this.matcher = new ReflectionEqualMatcher(expected, modes);
	}

	public boolean matches(Object actual) {
		this.propertyValues = PropertyAccessor.getArrayItemProperties(actual, this.properties);

		return matcher.matches(propertyValues);
	}

	private Object[][] propertyValues = null;

	public void describeTo(Description description) {
		description.appendText("the propery[" + StringHelper.toString(this.properties) + "] of object must match");

		description.appendText(String.format(",but actual value is:%s, not matched value[%s]", StringHelper
				.toString(this.propertyValues), StringHelper.toString(this.expected)));
	}
}
