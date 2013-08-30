package org.jtester.hamcrest.iassert.object.impl;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
public class ArrayAssertTest extends JTester {
	public void hasItems() {
		want.array(new String[] { "first item", "second item", "third item" }).hasAllItems("first item", "second item");
	}
}
