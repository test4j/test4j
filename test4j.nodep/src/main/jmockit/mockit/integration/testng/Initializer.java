/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.testng;

import org.testng.*;

import mockit.integration.testng.internal.*;
import mockit.internal.startup.*;

/**
 * A test listener implementation for TestNG that will properly initialize JMockit before any tests are executed.
 * <p/>
 * One way to configure TestNG to use this class as a listener is to pass
 * "-listener mockit.integration.testng.Initializer" as a command line argument.
 * Another way is through {@code testng.xml} configuration.
 * Please check the <a href="http://testng.org/doc/documentation-main.html#running-testng">TestNG documentation</a> for
 * details.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/RunningTests.html">Tutorial</a>
 */
public final class Initializer implements ITestNGListener
{
   public Initializer()
   {
      if (Startup.initializeIfNeeded()) {
         //noinspection deprecation
         TestNGRunnerDecorator.registerWithTestNG(TestNG.getDefault());
      }
   }
}
