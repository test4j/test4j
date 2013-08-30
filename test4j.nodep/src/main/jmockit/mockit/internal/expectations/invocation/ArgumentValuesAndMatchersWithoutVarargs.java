/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import java.util.*;

import mockit.internal.expectations.argumentMatching.*;

final class ArgumentValuesAndMatchersWithoutVarargs extends ArgumentValuesAndMatchers
{
   ArgumentValuesAndMatchersWithoutVarargs(InvocationArguments signature, Object[] values) { super(signature, values); }

   @Override
   boolean isMatch(Object[] replayArgs, Map<Object, Object> instanceMap)
   {
      if (matchers == null) {
         return areEqual(values, replayArgs, replayArgs.length, instanceMap);
      }

      for (int i = 0; i < replayArgs.length; i++) {
         Object actual = replayArgs[i];
         ArgumentMatcher expected = getArgumentMatcher(i);

         if (expected == null) {
            Object arg = values[i];
            if (arg == null) continue;
            expected = new EqualityMatcher(arg);
         }

         if (!expected.matches(actual)) {
            return false;
         }
      }

      return true;
   }

   @Override
   Error assertMatch(Object[] replayArgs, Map<Object, Object> instanceMap)
   {
      if (matchers == null) {
         return assertEquals(values, replayArgs, replayArgs.length, instanceMap);
      }

      for (int i = 0; i < replayArgs.length; i++) {
         Object actual = replayArgs[i];
         ArgumentMatcher expected = getArgumentMatcher(i);

         if (expected == null) {
            Object arg = values[i];
            if (arg == null) continue;
            expected = new EqualityMatcher(arg);
         }

         if (!expected.matches(actual)) {
            return signature.argumentMismatchMessage(i, expected, actual);
         }
      }

      return null;
   }

   @Override
   boolean hasEquivalentMatchers(ArgumentValuesAndMatchers other)
   {
      int i = indexOfFirstValueAfterEquivalentMatchers(other);

      if (i < 0) {
         return false;
      }

      while (i < values.length) {
         if (!EqualityMatcher.areEqual(values[i], other.values[i])) {
            return false;
         }

         i++;
      }

      return true;
   }
}
