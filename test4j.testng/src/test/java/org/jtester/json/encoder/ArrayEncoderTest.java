package org.jtester.json.encoder;

import org.jtester.testng.JTester;
import org.test4j.json.JSON;
import org.test4j.json.encoder.beans.test.User;
import org.test4j.json.helper.JSONFeature;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class ArrayEncoderTest extends JTester {

	@Test
	public void testEncode() {
		User[] users = new User[2];
		users[0] = User.newInstance(12, "darui.wu");
		users[1] = users[0];

		String json = JSON.toJSON(users, JSONFeature.UseSingleQuote);
		want.string(json).contains("#class:'org.jtester.json.encoder.beans.test.User@").contains("#refer:@");
	}
}
