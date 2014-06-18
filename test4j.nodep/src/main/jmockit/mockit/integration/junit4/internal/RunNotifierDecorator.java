/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4.internal;

import org.junit.runner.notification.*;
import org.junit.runner.*;

import mockit.*;
import mockit.integration.internal.*;
import mockit.internal.startup.*;
import mockit.internal.state.*;

/**
 * Startup mock which works in conjunction with {@link JUnit4TestRunnerDecorator} to provide JUnit 4.5+ integration.
 * <p/>
 * This class is not supposed to be accessed from user code. JMockit will automatically load it at startup.
 */
public final class RunNotifierDecorator extends MockUp<RunNotifier>
{
   @Mock
   public static void fireTestRunStarted(Invocation invocation, Description description)
   {
      if (description != null) {
         UsingMocksAndStubs mocksAndStubs = description.getAnnotation(UsingMocksAndStubs.class);

         if (mocksAndStubs != null) {
            Class<?>[] mockAndRealClasses = mocksAndStubs.value();
            Startup.initializing = true;

            try {
               TestRunnerDecorator.setUpMocksAndStubs(mockAndRealClasses);
            }
            finally {
               Startup.initializing = false;
            }
         }
      }

      invocation.proceed();
   }

   @Mock
   public static void fireTestRunFinished(Invocation invocation, Result result)
   {
      TestRun.enterNoMockingZone();

      try {
         TestRunnerDecorator.cleanUpMocksFromPreviousTestClass();
         invocation.proceed();
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }
}
