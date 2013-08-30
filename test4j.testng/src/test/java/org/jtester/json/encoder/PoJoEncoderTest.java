package org.jtester.json.encoder;

import org.jtester.fortest.beans.Manager;
import org.jtester.testng.JTester;
import org.test4j.json.JSON;
import org.test4j.json.helper.JSONFeature;
import org.testng.annotations.Test;

@Test
public class PoJoEncoderTest extends JTester {
	String json = "";

	public void testPoJoEncoder() {
		Manager manager = Manager.mock();
		this.json = JSON.toJSON(manager, JSONFeature.UseSingleQuote);
		want.string(json).contains("Tony Tester");
	}

	@Test(description = "对象有多重继承的情况", dependsOnMethods = "testPoJoEncoder")
	public void testPoJoDecoder() {
		Manager manager = JSON.toObject(json, Manager.class);
		want.object(manager).propertyEq("name", "Tony Tester");
	}
}
