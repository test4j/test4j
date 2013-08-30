package org.jtester.hamcrest.iassert.common.impl;

import java.io.File;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.jtester.hamcrest.iassert.common.intf.IAssert;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
@SuppressWarnings({ "rawtypes" })
public class AssertTest extends JTester {
	@Test(dataProvider = "assertClass")
	public void wanted(IAssert<?, ?> as, Class claz) {
		want.object(as).propertyEq("valueClaz", claz);
	}

	@DataProvider
	public Object[][] assertClass() {
		return new Object[][] { { the.bool(), Boolean.class }, /** <br> */
		{ the.array(), Object[].class }, /** <br> */
		{ the.bite(), Byte.class }, /** <br> */
		{ the.calendar(), Calendar.class }, /** <br> */
		{ the.character(), Character.class }, /** <br> */
		{ the.collection(), Collection.class }, /** <br> */
		{ the.date(), Date.class }, /** <br> */
		{ the.doublenum(), Double.class }, /** <br> */
		{ the.file(), File.class }, /** <br> */
		{ the.floatnum(), Float.class }, /** <br> */
		{ the.integer(), Integer.class }, /** <br> */
		{ the.longnum(), Long.class }, /** <br> */
		{ the.map(), Map.class }, /** <br> */
		{ the.object(), Object.class }, /** <br> */
		{ the.shortnum(), Short.class }, /** <br> */
		{ the.string(), String.class } };
	}

	@Test
	public void wantedMap() {
		want.object(the.map()).propertyEq("valueClaz", Map.class);
	}
}
