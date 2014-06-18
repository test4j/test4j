/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit3.internal;

import junit.framework.*;

import mockit.*;

/**
 * Provides an startup mock that modifies the JUnit 3.8 test runner so that it calls back to JMockit for each test
 * execution.
 * When that happens, JMockit will assert any expectations set during the test, including expectations specified through
 * {@link Mock} as well as in {@link Expectations} subclasses.
 * <p/>
 * This class is not supposed to be accessed from user code. JMockit will automatically load it at startup.
 */
public final class MockTestCase extends MockUp<TestCase>
{
   private final JUnitTestCaseDecorator decorator = new JUnitTestCaseDecorator();

   @Mock
   public void runBare(Invocation invocation) throws Throwable
   {
      TestCase testCase = invocation.getInvokedInstance();
      decorator.runBare(testCase);
   }
}
