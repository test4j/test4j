package org.jtester.junit.filter.acceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jtester.junit.JTesterRunner;
import org.jtester.junit.filter.SuiteType;
import org.jtester.junit.filter.finder.FilterCondiction;
import org.jtester.junit.filter.finder.FilterCondictionTest_Suite;
import org.jtester.module.ICore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JTesterRunner.class)
public class TestInClasspathAcceptorTest implements ICore {

	public static class Anything {
	}

	private TestInClasspathAcceptor tester;
	private SuiteType[] suiteTypes;
	private Class<?>[] baseTypes;
	private Class<?>[] excludedBaseTypes;

	@Before
	public void initTester() {
		suiteTypes = new SuiteType[] { SuiteType.JUNT4_TEST_CLASSES };
		baseTypes = new Class<?>[] { Object.class };
		excludedBaseTypes = new Class<?>[] {};
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
	}

	@Test
	public void acceptInnerClasses() {
		assertTrue(tester.acceptInnerClass());
	}

	@Test
	public void dontAcceptNonTestClass() {
		assertFalse(tester.isCorrectTestType(forfilter.tests.p1.P1NoTest.class));
		assertFalse(tester.isCorrectTestType(forfilter.suitetest.TestSuite.class));
	}

	@Test
	public void acceptTestClass() {
		assertTrue(tester.isCorrectTestType(forfilter.tests.p1.P1Test.class));
	}

	@Test
	public void dontAcceptNonRunWithClassWhenSuiteTypeIsRunWith() {
		suiteTypes = new SuiteType[] { SuiteType.SUITE_TEST_CLASSES };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertFalse(tester.isCorrectTestType(forfilter.tests.p1.P1Test.class));
		assertFalse(tester.isCorrectTestType(forfilter.tests.p1.P1NoTest.class));
	}

	@Test
	public void acceptRunWithClassesWhenSuiteTypeIsRunWith() {
		suiteTypes = new SuiteType[] { SuiteType.SUITE_TEST_CLASSES };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.suitetest.TestSuite.class));
	}

	/**
	 * This is now (from JUnit45 on) possible since recursiion in suites will be
	 * detected
	 */
	@Test
	public void alsoAcceptRunWithClassesThatUseClasspathSuiteThemselves() {
		suiteTypes = new SuiteType[] { SuiteType.SUITE_TEST_CLASSES };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.suitetest.ACPTestSuite.class));
	}

	@Test
	public void acceptRunWithAndTestClasses() {
		suiteTypes = new SuiteType[] { SuiteType.SUITE_TEST_CLASSES, SuiteType.JUNT4_TEST_CLASSES };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.suitetest.TestSuite.class));
		assertTrue(tester.isCorrectTestType(forfilter.tests.p1.P1Test.class));
		assertFalse(tester.isCorrectTestType(forfilter.tests.p1.P1NoTest.class));
	}

	@Test
	public void abstractTestClasses() {
		assertFalse(tester.isCorrectClazType(forfilter.tests.p2.AbstractP2Test.class));
		assertTrue(tester.isCorrectClazType(forfilter.tests.p2.ConcreteP2Test.class));
	}

	@Test
	public void dontAcceptJUnit38TestClassByDefault() {
		assertFalse(tester.isCorrectTestType(forfilter.tests.ju38.JU38ConcreteTest.class));
	}

	@Test
	public void acceptJUnit38TestClassIfConfigured() {
		suiteTypes = new SuiteType[] { SuiteType.JUNIT38_TEST_CLASSES };
		FilterCondiction testFilter = new FilterCondiction(false, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.tests.ju38.JU38ConcreteTest.class));
		assertFalse(tester.isCorrectClazType(forfilter.tests.ju38.JU38AbstractTest.class));
	}

	@Test
	public void filterPatternsNull() {
		assertTrue(tester.isAcceptedByPatterns("oops.MyClass"));
		assertTrue(tester.isAcceptedByPatterns("TopLevel"));
	}

	@Test
	public void oneFilterPattern() {
		String[] patterns = new String[] { "oops.*" };
		FilterCondiction testFilter = new FilterCondiction(true, patterns, suiteTypes, baseTypes, excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isAcceptedByPatterns("oops.MyClass"));
		assertFalse(tester.isAcceptedByPatterns("TopLevel"));
	}

	@Test
	public void twoFilterPatterns() {
		FilterCondiction testFilter = new FilterCondiction();
		reflector.invoke(testFilter, "setFilterPatterns", new Object[] { new String[] { "oops.*", ".*Test" } });
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isAcceptedByPatterns("oops.MyClass"));
		assertFalse(tester.isAcceptedByPatterns("TopLevel"));
		want.bool(tester.isAcceptedByPatterns("ppp.MyTest")).is(false);
	}

	@Test
	public void negationFilter() {
		// Accept all tests except those matching "oops.*"
		String[] patterns = new String[] { "!oops.*" };
		FilterCondiction testFilter = new FilterCondiction(true, patterns, suiteTypes, baseTypes, excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isAcceptedByPatterns("TopLevel"));
		assertFalse(tester.isAcceptedByPatterns("oops.MyTest"));
	}

	@Test
	public void filtersPlusNegationFilters() {
		// Accept all tests that match any positive filter AND do not match any
		// negation filter
		String[] patterns = new String[] { "oops*", "!*Test", ".allo*", "!h*" };
		FilterCondiction testFilter = new FilterCondiction(true, patterns, suiteTypes, baseTypes, excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertFalse(tester.isAcceptedByPatterns("TopLevel"));
		assertFalse(tester.isAcceptedByPatterns("oops.MyTest"));
		assertTrue(tester.isAcceptedByPatterns("oops.MyOops"));
		assertFalse(tester.isAcceptedByPatterns("hallo.AnOops"));
		assertTrue(tester.isAcceptedByPatterns(".allo.AnOops"));
	}

	@Test
	public void baseTypeFilterIsAppliedOnTestClasses() {
		suiteTypes = new SuiteType[] { SuiteType.JUNT4_TEST_CLASSES };
		baseTypes = new Class<?>[] { Anything.class, forfilter.tests.p2.AbstractP2Test.class };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.tests.p2.ConcreteP2Test.class));
		assertFalse(tester.isCorrectClazType(forfilter.tests.p1.P1Test.class));
	}

	@Test
	public void baseTypeFilterIsAppliedOnJUnit38TestClasses() {
		suiteTypes = new SuiteType[] { SuiteType.JUNIT38_TEST_CLASSES };
		baseTypes = new Class<?>[] { Anything.class, forfilter.tests.ju38.JU38AbstractTest.class };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.tests.ju38.JU38ConcreteTest.class));
		assertFalse(tester.isCorrectClazType(forfilter.tests.ju38.JU38TestWithoutBase.class));
	}

	@Test
	public void baseTypeFilterIsNotAppliedOnRunWithClasses() {
		suiteTypes = new SuiteType[] { SuiteType.SUITE_TEST_CLASSES };
		baseTypes = new Class<?>[] { Anything.class };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.suitetest.TestSuite.class));
	}

	@Test
	public void excludedBaseTypeFilterIsAppliedOnTestClasses() {
		suiteTypes = new SuiteType[] { SuiteType.JUNT4_TEST_CLASSES };
		excludedBaseTypes = new Class<?>[] { Anything.class, forfilter.tests.p2.AbstractP2Test.class };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.tests.p1.P1Test.class));
		assertFalse(tester.isCorrectClazType(forfilter.tests.p2.ConcreteP2Test.class));
	}

	@Test
	public void excludedBaseTypeFilterIsAppliedOnJUnit38TestClasses() {
		suiteTypes = new SuiteType[] { SuiteType.JUNIT38_TEST_CLASSES };
		excludedBaseTypes = new Class<?>[] { Anything.class, forfilter.tests.ju38.JU38AbstractTest.class };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.tests.ju38.JU38TestWithoutBase.class));
		assertFalse(tester.isCorrectClazType(forfilter.tests.ju38.JU38ConcreteTest.class));
	}

	@Test
	public void excludedBaseTypeFilterIsNotAppliedOnRunWithClasses() {
		suiteTypes = new SuiteType[] { SuiteType.SUITE_TEST_CLASSES };
		excludedBaseTypes = new Class<?>[] { Object.class };
		FilterCondiction testFilter = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		tester = new TestInClasspathAcceptor(testFilter);
		assertTrue(tester.isCorrectTestType(forfilter.suitetest.TestSuite.class));
	}

	@Test
	public void searchInJars() {
		FilterCondiction testFilter1 = new FilterCondiction(true, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		assertTrue(new TestInClasspathAcceptor(testFilter1).searchInJars());
		FilterCondiction testFilter2 = new FilterCondiction(false, new String[0], suiteTypes, baseTypes,
				excludedBaseTypes);
		assertFalse(new TestInClasspathAcceptor(testFilter2).searchInJars());
	}

	@Test
	public void testIsJunit4SuiteClaz() throws Exception {
		TestInClasspathAcceptor acceptor = reflector.newInstance(TestInClasspathAcceptor.class);
		Boolean isSuite = reflector.invoke(acceptor, "isJunit4SuiteClaz",
				new Object[] { FilterCondictionTest_Suite.class });
		want.bool(isSuite).is(true);
	}

	@Test
	public void testIsJunit4SuiteClaz_RunwithRunner() throws Exception {
		TestInClasspathAcceptor acceptor = reflector.newInstance(TestInClasspathAcceptor.class);
		Boolean isSuite = reflector.invoke(acceptor, "isJunit4SuiteClaz",
				new Object[] { TestInClasspathAcceptorTest.class });
		want.bool(isSuite).is(false);
	}
}
