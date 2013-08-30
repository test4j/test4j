/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.logging;

import java.util.logging.*;

import mockit.*;

/**
 * A mock class containing mocks and stubs for the standard logging API in the JDK
 * (java.util.logging).
 * <p/>
 * When a test class is annotated as {@code @UsingMocksAndStubs(JDKLoggingMocks.class)}, all
 * production code touched by the tests in that class will receive mock {@code Logger} instances
 * instead of real ones, when one of the factory methods in class {@code java.util.logging.Logger}
 * is called.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/UsingMocksAndStubs.html">Tutorial</a>
 */
@SuppressWarnings({"UnusedDeclaration"})
@MockClass(realClass = Logger.class)
public final class JDKLoggingMocks
{
   private static final Logger MOCK_LOG = new Logger(null, null) {};

   private JDKLoggingMocks() {}

   /**
    * Returns a singleton mock {@code Logger} instance, whose methods do nothing.
    */
   @Mock public static Logger getAnonymousLogger() { return MOCK_LOG; }

   /**
    * Returns a singleton mock {@code Logger} instance, whose methods do nothing.
    */
   @Mock public static Logger getAnonymousLogger(String resourceBundleName) { return MOCK_LOG; }

   /**
    * Returns a singleton mock {@code Logger} instance, whose methods do nothing.
    */
   @Mock public static Logger getLogger(String name) { return MOCK_LOG; }

   /**
    * Returns a singleton mock {@code Logger} instance, whose methods do nothing.
    */
   @Mock public static Logger getLogger(String name, String resourceBundleName) { return MOCK_LOG; }
}
