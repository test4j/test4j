package org.jtester.hamcrest.matcher.property.comparator;

import java.util.HashMap;

import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.json.encoder.beans.test.User;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
@Test
public class MapComparatorTest extends JTester {

	public void testMap() {
		want.object(new HashMap() {
			{
				this.put("id", 123);
				this.put("name", "darui.wu");
			}
		}).reflectionEq(new HashMap() {
			{
				this.put("id", 123);
				this.put("name", null);
			}
		}, EqMode.IGNORE_DEFAULTS);
	}

	public void testMap2() {
		want.object(User.newInstance(123, "darui.wu")).reflectionEqMap(new DataMap() {
			{
				this.put("id", 123);
				this.put("name", null);
			}
		}, EqMode.IGNORE_DEFAULTS);
	}

	@Test(expectedExceptions = AssertionError.class)
	public void testMap3() {
		want.object(new HashMap() {
			{
				this.put("id", 123);
				this.put("name", "darui.wu");
			}
		}).reflectionEq(new HashMap() {
			{
				this.put("id", 123);
			}
		}, EqMode.IGNORE_DEFAULTS);
	}

}
