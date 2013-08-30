package org.jtester.hamcrest.matcher.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.DateHelper;
import org.testng.annotations.Test;

import ext.jtester.hamcrest.MatcherAssert;

@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
@Test
public class MapListPropertyEqaulMatcherTest extends JTester {

	public void testMapListPropertyEqaulMatcher() {

		List<DataMap> expected = new ArrayList<DataMap>() {
			{
				this.add(new DataMap() {
					{
						this.put("id", 123);
					}
				});
				this.add(new DataMap() {
					{
						this.put("name", "jobs.he");
					}
				});
			}
		};
		MapListPropertyEqaulMatcher matcher = new MapListPropertyEqaulMatcher(expected,
				new EqMode[] { EqMode.IGNORE_DEFAULTS });

		List<Map<String, ?>> actual = new ArrayList<Map<String, ?>>() {
			{
				this.add(new HashMap() {
					{
						this.put("id", 123);
						this.put("name", "darui.wu");
					}
				});
				this.add(new HashMap() {
					{
						this.put("id", 124);
						this.put("name", "jobs.he");
					}
				});
			}
		};
		MatcherAssert.assertThat(actual, matcher);
	}

	@Test
	public void testPropEqString() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(mockMap());
		list.add(mockMap());
		want.list(list).reflectionEqMap(2, new DataMap() {
			{
				this.put("integer", "20");
				this.put("boolean", "true");
				this.put("double", "20.0");
				this.put("date", "2011-11-12");
			}
		}, EqMode.EQ_STRING);
	}

	private Map<String, Object> mockMap() {
		return new HashMap<String, Object>() {
			{
				this.put("integer", 20);
				this.put("boolean", true);
				this.put("double", 20D);
				this.put("date", DateHelper.parse("2011-11-12"));
			}
		};
	}
}
