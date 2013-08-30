package org.jtester.junit.demo.features;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;

public class RuleAnnotationDemo {
	@Rule
	public IgnoreLeadingFailure ilf = new IgnoreLeadingFailure();

	@Test
	public void testTest1() {
		assertTrue(false);
	}

	@Test
	public void testTest2() {
		assertTrue(true);
	}
}
