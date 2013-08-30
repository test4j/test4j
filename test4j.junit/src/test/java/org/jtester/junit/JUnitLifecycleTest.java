package org.jtester.junit;

import org.jtester.module.core.utility.MessageHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JUnitLifecycleTest {

	@BeforeClass
	public static void beforeClass() {
		MessageHelper.info("Before Class方法必须是静态的！");
	}

	@Before
	public void beforeMethod() {
		MessageHelper.info("Before Method！");
	}

	@Test
	public void test1() {
		MessageHelper.info("Test Method！");
	}

	@After
	public void afterMethod() {
		MessageHelper.info("After Method！");
	}

	@AfterClass
	public static void afterClass() {
		MessageHelper.info("After Class方法必须是静态的！");
	}
}
