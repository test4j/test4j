package org.jtester.testng.database.annotations;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "for-test")
public class TestNgUtilTestHelper {
	@Test
	public void testHasTest() {

	}

	public void testNotTest() {
		privateMethod();
	}

	public void testNotTest_ButHasOtherAnnotation() {
		privateMethod();
	}

	@BeforeTest
	public void beforeTest() {

	}

	public static void staticMethod() {

	}

	private void privateMethod() {

	}

	@DataProvider
	public Object[][] dataProvier() {
		return new Object[0][0];
	}
}
