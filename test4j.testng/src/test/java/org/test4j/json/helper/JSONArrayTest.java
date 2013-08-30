package org.test4j.json.helper;

import org.jtester.testng.JTester;
import org.test4j.json.helper.JSONArray;
import org.test4j.json.helper.JSONSingle;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class JSONArrayTest extends JTester {

	@Test
	public void testDescription() {
		JSONArray array = new JSONArray();
		array.add(new JSONSingle("value"));

		String result = array.toString();
		want.string(result).isEqualTo("[value]");
	}
}
