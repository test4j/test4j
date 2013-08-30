package org.jtester.module.core;

import java.lang.reflect.Method;

/**
 * Listener for test events. The events must follow following ordering:
 * <ul>
 * <li>[jTester] setupClass - TestClass1</li>
 * <li>[Test] testBeforeClass - TestClass1 (not for JUnit3)</li>
 * <li>[jTester] setupMethod - TestClass1</li>
 * <li>[Test] testSetUp - TestClass1</li>
 * <li>[jTester] beforeMethodRunning - TestClass1 - test1</li>
 * <li>[Test] testMethod - TestClass1 - test1</li>
 * <li>[jTester] afterMethodRunned - TestClass1 - test1</li>
 * <li>[Test] testTearDown - TestClass1</li>
 * <li>[jTester] teardownMethod - TestClass1</li>
 * <li>[jTester] setupMethod - TestClass1</li>
 * <li>[Test] testSetUp - TestClass1</li>
 * <li>[jTester] beforeMethodRunning - TestClass1 - test2</li>
 * <li>[Test] testMethod - TestClass1 - test2</li>
 * <li>[jTester] afterMethodRunned - TestClass1 - test2</li>
 * <li>[Test] testTearDown - TestClass1</li>
 * <li>[jTester] teardownMethod - TestClass1</li>
 * <li>[Test] testAfterClass - TestClass1 (not for JUnit3)</li>
 * </ul>
 * <p/>
 * The after methods will always when the before counterpart has run (or begun).
 * For example if an exception occurs during the setupMethod method, the
 * teardownMethod method will still be called.
 * <p/>
 * Is implemented as an abstract class with empty methods instead of an
 * interface, since most implementations only need to implement a small subset
 * of the provided callback methods.
 */
@SuppressWarnings("rawtypes")
public abstract class TestListener {

	/**
	 * Invoked before any of the test in a test class are run. This can be
	 * overridden to for example add test-class initialization.<br>
	 * Phase<br>
	 * o <b>setupClass</b>&nbsp;(Object testObject) <br>
	 * o beforeMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o beforeMethodRunning&nbsp;(Object testObject, Method testMethod)<br>
	 * o afterMethodRunned&nbsp;(Object testObject, Method testMethod, Throwable
	 * testThrowable)<br>
	 * o afterMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o teardownClass&nbsp;(Object testObject)<br>
	 * <br>
	 * 
	 * @param testObject
	 *            The test class, not null
	 */
	public void beforeClass(Class testClazz) {
		// empty
	}

	/**
	 * Invoked before the test setup (eg @Before) is run. This can be overridden
	 * to for example initialize the test-fixture.<br>
	 * Phase<br>
	 * o setupClass&nbsp;(Object testObject) <br>
	 * o <b>beforeMethod</b>&nbsp;(Object testObject, Method testMethod)<br>
	 * o beforeMethodRunning&nbsp;(Object testObject, Method testMethod)<br>
	 * o afterMethodRunned&nbsp;(Object testObject, Method testMethod, Throwable
	 * testThrowable)<br>
	 * o afterMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o teardownClass&nbsp;(Object testObject)<br>
	 * <br>
	 * 
	 * @param testObject
	 *            The test instance, not null
	 * @param testMethod
	 *            The test method, not null
	 */
	public void beforeMethod(Object testObject, Method testMethod) {
		// empty
	}

	/**
	 * Invoked before the test but after the test setup (eg @Before) is run.
	 * This can be overridden to for example further initialize the test-fixture
	 * using values that were set during the test setup.<br>
	 * Phase<br>
	 * o setupClass&nbsp;(Object testObject) <br>
	 * o beforeMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o <b>beforeMethodRunning</b>&nbsp;(Object testObject, Method testMethod)<br>
	 * o afterMethodRunned&nbsp;(Object testObject, Method testMethod, Throwable
	 * testThrowable)<br>
	 * o afterMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o teardownClass&nbsp;(Object testObject)<br>
	 * <br>
	 * 
	 * @param testObject
	 *            The test instance, not null
	 * @param testMethod
	 *            The test method, not null
	 */
	public void beforeRunning(Object testObject, Method testMethod) {
		// empty
	}

	/**
	 * Invoked after the test run but before the test tear down (e.g. @After).
	 * This can be overridden to for example add assertions for testing the
	 * result of the test. It the before method or the test raised an exception,
	 * this exception will be passed to the method.<br>
	 * Phase<br>
	 * o setupClass&nbsp;(Object testObject) <br>
	 * o beforeMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o beforeMethodRunning&nbsp;(Object testObject, Method testMethod)<br>
	 * o <b>afterMethodRunned</b>&nbsp;(Object testObject, Method testMethod,
	 * Throwable testThrowable)<br>
	 * o afterMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o teardownClass&nbsp;(Object testObject)<br>
	 * <br>
	 * 
	 * @param testObject
	 *            The test instance, not null
	 * @param testMethod
	 *            The test method, not null
	 * @param testThrowable
	 *            The throwable thrown during the test or beforeMethodRunning,
	 *            null if none was thrown
	 */
	public void afterRunned(Object testObject, Method testMethod, Throwable testThrowable) {
		// empty
	}

	/**
	 * Invoked after the test tear down (eg @After). This can be overridden to
	 * for example perform extra cleanup after the test.<br>
	 * Phase<br>
	 * o setupClass&nbsp;(Object testObject) <br>
	 * o beforeMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o beforeMethodRunning&nbsp;(Object testObject, Method testMethod)<br>
	 * o afterMethodRunned&nbsp;(Object testObject, Method testMethod, Throwable
	 * testThrowable)<br>
	 * o <b>afterMethod</b>&nbsp;(Object testObject, Method testMethod)<br>
	 * o teardownClass&nbsp;(Object testObject)<br>
	 * <br>
	 * 
	 * @param testObject
	 *            The test instance, not null
	 * @param testMethod
	 *            The test method, not null
	 */
	public void afterMethod(Object testObject, Method testMethod) {
		// empty
	}

	/**
	 * Invoked after the test class tear down (eg @AfterClass).<br>
	 * Phase<br>
	 * o setupClass&nbsp;(Object testObject) <br>
	 * o beforeMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o beforeMethodRunning&nbsp;(Object testObject, Method testMethod)<br>
	 * o afterMethodRunned&nbsp;(Object testObject, Method testMethod, Throwable
	 * testThrowable)<br>
	 * o afterMethod&nbsp;(Object testObject, Method testMethod)<br>
	 * o <b>teardownClass</b>&nbsp;(Object testObject)<br>
	 * <br>
	 * 
	 * @param testObject
	 * @throws Exception
	 */
	public void afterClass(Object testObject) {
		// empty
	}

	public String toString() {
		return getName();
	}

	/**
	 * 监听器名称
	 * 
	 * @return
	 */
	protected abstract String getName();
}
