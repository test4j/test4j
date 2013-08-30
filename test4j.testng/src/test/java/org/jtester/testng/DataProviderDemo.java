package org.jtester.testng;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "testng-demo")
public class DataProviderDemo extends JTester {
	@Test(dataProvider = "testDemo_dataprovider")
	public void testDemo(int count, String name) {
		want.number(count).isEqualTo(2);
		want.string(name).start("my");
	}

	@DataProvider
	public Object[][] testDemo_dataprovider() {
		return new Object[][] { { 2, "mytest" }, { 1, "mytest" }, { 2, "mytest" } };
	}
}
