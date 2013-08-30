package org.jtester.hamcrest;

import org.jtester.fortest.beans.Address;
import org.jtester.fortest.beans.User;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
public class TestPropertyAssert extends JTester {
	public void assertObject() {
		User user = new User(1, "wu", "darui");
		user.setAddress(new Address("网商路699号", "310012", "alibaba滨江大楼"));

		User actualUser = yourApi();
		want.object(actualUser).reflectionEq(user);
		want.object(actualUser).propertyEq("first", "wu").propertyEq("address.postcode", "310012")
				.propertyMatch("last", the.string().isEqualTo("darui"));

		want.object(actualUser).propertyEq(new String[] { "first", "last", "address.postcode" },
				new String[] { "wu", "darui", "310012" });
	}

	public static User yourApi() {
		User user = new User(1, "wu", "darui");
		user.setAddress(new Address("网商路699号", "310012", "alibaba滨江大楼"));
		return user;
	}
}
