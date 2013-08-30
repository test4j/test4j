package org.jtester.junit.demo.features;

import static org.junit.Assert.assertTrue;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class LoggingRuleTest {
	@ClassRule
	public static LoggingRule classRule = new LoggingRule("classrule");

	@Rule
	public static LoggingRule rule = new LoggingRule("rule");

	@Test
	public void testSomething() {
		System.out.println("In TestSomething");
		assertTrue(true);
	}

	@Test
	public void testSomethingElse() {
		System.out.println("In TestSomethingElse");
		assertTrue(true);
	}
}
