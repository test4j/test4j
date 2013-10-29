/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4.internal;

import java.util.*;

import org.junit.runners.model.*;

import mockit.*;

/**
 * Startup mock that modifies the JUnit 4.5+ test runner so that it calls back to JMockit immediately after every test
 * executes.
 * When that happens, JMockit will assert any expectations set during the test, including expectations specified through
 * {@link mockit.Mock} as well as in {@link mockit.Expectations} subclasses.
 * <p/>
 * This class is not supposed to be accessed from user code. JMockit will automatically load it at startup.
 */
public final class MockFrameworkMethod extends MockUp<FrameworkMethod>
{
   private final JUnit4TestRunnerDecorator decorator = new JUnit4TestRunnerDecorator();

   @Mock
   public Object invokeExplosively(Invocation invocation, Object target, Object... params) throws Throwable
   {
      FrameworkMethod it = invocation.getInvokedInstance();
      return decorator.invokeExplosively(it, target, params);
   }

   @Mock
   public static void validatePublicVoidNoArg(Invocation invocation, boolean isStatic, List<Throwable> errors)
   {
      FrameworkMethod it = invocation.getInvokedInstance();

      if (!isStatic && it.getMethod().getParameterTypes().length > 0) {
         it.validatePublicVoid(false, errors);
         return;
      }

      invocation.proceed();
   }
}
