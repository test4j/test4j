package org.jtester.junit.extend;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JUnitDemo {

	@BeforeClass
	public static void beforeClass() {
		System.out.println("beforeClass");
	}

	@Before
	public void initMethod() {
		System.out.println("init setup");
	}

	@Test
	public void test1() {
		System.out.println("test1");
	}

	@Test
	public void test2() {
		System.out.println("test2");
	}
}
