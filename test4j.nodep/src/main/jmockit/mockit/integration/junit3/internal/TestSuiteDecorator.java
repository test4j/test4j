/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit3.internal;

import java.lang.reflect.*;
import static java.lang.reflect.Modifier.*;

import junit.framework.*;

import mockit.*;

/**
 * Startup mock which works in conjunction with {@linkplain JUnitTestCaseDecorator} to provide JUnit 3.8 integration.
 * <p/>
 * This class is not supposed to be accessed from user code. JMockit will automatically load it at startup.
 */
public final class TestSuiteDecorator extends MockUp<TestSuite>
{
   @Mock
   public boolean isTestMethod(Method m)
   {
      int modifiers = m.getModifiers();

      return
         isPublic(modifiers) && !isStatic(modifiers) &&
         m.getReturnType() == Void.TYPE &&
         m.getName().startsWith("test");
   }
}