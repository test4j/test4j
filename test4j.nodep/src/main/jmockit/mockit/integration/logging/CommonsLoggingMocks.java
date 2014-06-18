/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.logging;

import org.apache.commons.logging.*;

import mockit.*;

/**
 * A mock class containing mocks and stubs for the Apache Commons Logging API.
 * <p/>
 * When a test class is annotated as {@code @UsingMocksAndStubs(CommonsLoggingMocks.class)},
 * all production code touched by the tests in that class will receive mock {@code Log} instances
 * instead of real ones, when one of the factory methods in class
 * {@code org.apache.commons.logging.LogFactory} is called.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/UsingMocksAndStubs.html">Tutorial</a>
 */
public final class CommonsLoggingMocks extends MockUp<LogFactory>
{
   private static final Log MOCK_LOG = new MockUp<Log>(){}.getMockInstance();

   private CommonsLoggingMocks() {}

   /**
    * Returns a singleton mock {@code Log} instance, whose methods do nothing.
    */
   @Mock public static Log getLog(String name) { return MOCK_LOG; }

   /**
    * Returns a singleton mock {@code Log} instance, whose methods do nothing.
    */
   @Mock public static Log getLog(Class<?> clazz) { return MOCK_LOG; }
}
