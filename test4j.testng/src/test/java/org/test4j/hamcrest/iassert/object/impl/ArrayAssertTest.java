package org.test4j.hamcrest.iassert.object.impl;

import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "assertion" })
public class ArrayAssertTest extends JTester {
	public void hasItems() {
		want.array(new String[] { "first item", "second item", "third item" }).hasAllItems("first item", "second item");
	}
}
