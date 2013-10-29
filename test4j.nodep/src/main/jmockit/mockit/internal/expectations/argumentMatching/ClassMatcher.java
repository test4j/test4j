/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

import mockit.internal.util.*;

public final class ClassMatcher implements ArgumentMatcher
{
   private final String nameOfExpectedClass;
   private final Class<?> matchableClass;

   public ClassMatcher(Class<?> expectedClass)
   {
      nameOfExpectedClass = expectedClass.getName();

      Class<?> wrapperTypeIfPrimitive = AutoBoxing.getWrapperType(expectedClass);
      matchableClass = wrapperTypeIfPrimitive == null ? expectedClass : wrapperTypeIfPrimitive;
   }

   public boolean matches(Object argValue)
   {
      return matchableClass.isInstance(argValue);
   }

   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch)
   {
      argumentMismatch.append("an instance of ").append(nameOfExpectedClass);
   }
}
