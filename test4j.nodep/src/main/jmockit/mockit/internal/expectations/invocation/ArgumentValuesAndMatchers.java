/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import java.util.*;

import mockit.internal.expectations.argumentMatching.*;
import mockit.internal.util.*;

abstract class ArgumentValuesAndMatchers
{
   static final Object[] NULL_VARARGS = new Object[0];

   final InvocationArguments signature;
   Object[] values;
   List<ArgumentMatcher> matchers;

   ArgumentValuesAndMatchers(InvocationArguments signature, Object[] values)
   {
      this.signature = signature;
      this.values = values;
   }

   final void setValuesWithNoMatchers(Object[] argsToVerify)
   {
      values = argsToVerify;
      matchers = null;
   }

   final Object[] prepareForVerification(Object[] argsToVerify, List<ArgumentMatcher> matchers)
   {
      Object[] replayArgs = values;
      values = argsToVerify;
      this.matchers = matchers;
      return replayArgs;
   }

   final ArgumentMatcher getArgumentMatcher(int parameterIndex)
   {
      if (matchers == null) {
         return null;
      }

      ArgumentMatcher matcher = parameterIndex < matchers.size() ? matchers.get(parameterIndex) : null;

      if (matcher == null && parameterIndex < values.length && values[parameterIndex] == null) {
         matcher = AlwaysTrueMatcher.INSTANCE;
      }

      return matcher;
   }

   abstract boolean isMatch(Object[] replayArgs, Map<Object, Object> instanceMap);

   final boolean areEqual(Object[] expectedValues, Object[] actualValues, int count, Map<Object, Object> instanceMap)
   {
      for (int i = 0; i < count; i++) {
         if (isNotEqual(expectedValues[i], actualValues[i], instanceMap)) {
            return false;
         }
      }

      return true;
   }

   private boolean isNotEqual(Object expected, Object actual, Map<Object, Object> instanceMap)
   {
      return
         actual == null && expected != null ||
         actual != null && expected == null ||
         actual != null && actual != expected && actual != instanceMap.get(expected) &&
         !EqualityMatcher.areEqualWhenNonNull(actual, expected);
   }

   abstract Error assertMatch(Object[] replayArgs, Map<Object, Object> instanceMap);

   final Error assertEquals(Object[] expectedValues, Object[] actualValues, int count, Map<Object, Object> instanceMap)
   {
      for (int i = 0; i < count; i++) {
         Object expected = expectedValues[i];
         Object actual = actualValues[i];

         if (isNotEqual(expected, actual, instanceMap)) {
            return signature.argumentMismatchMessage(i, expected, actual);
         }
      }

      return null;
   }

   abstract boolean hasEquivalentMatchers(ArgumentValuesAndMatchers other);

   final boolean equivalentMatches(ArgumentMatcher matcher1, Object arg1, ArgumentMatcher matcher2, Object arg2)
   {
      boolean matcher1MatchesArg2 = matcher1.matches(arg2);
      boolean matcher2MatchesArg1 = matcher2.matches(arg1);

      if (arg1 != null && arg2 != null && matcher1MatchesArg2 && matcher2MatchesArg1) {
         return true;
      }

      if (arg1 == arg2 && matcher1MatchesArg2 == matcher2MatchesArg1) { // both matchers fail
         ArgumentMismatch desc1 = new ArgumentMismatch();
         matcher1.writeMismatchPhrase(desc1);
         ArgumentMismatch desc2 = new ArgumentMismatch();
         matcher2.writeMismatchPhrase(desc2);
         return desc1.toString().equals(desc2.toString());
      }

      return false;
   }

   final int indexOfFirstValueAfterEquivalentMatchers(ArgumentValuesAndMatchers other)
   {
      List<ArgumentMatcher> otherMatchers = other.matchers;

      if (otherMatchers == null || otherMatchers.size() != matchers.size()) {
         return -1;
      }

      int i = 0;
      int m = matchers.size();

      while (i < m) {
         ArgumentMatcher matcher1 = matchers.get(i);
         ArgumentMatcher matcher2 = otherMatchers.get(i);

         if (matcher1 == null || matcher2 == null) {
            if (!EqualityMatcher.areEqual(values[i], other.values[i])) {
               return -1;
            }
         }
         else if (matcher1 != matcher2) {
            if (matcher1.getClass() != matcher2.getClass() || matcher1.getClass() == HamcrestAdapter.class) {
               return -1;
            }

            if (!equivalentMatches(matcher1, values[i], matcher2, other.values[i])) {
               return -1;
            }
         }

         i++;
      }

      return i;
   }

   final String toString(MethodFormatter methodFormatter)
   {
      ArgumentMismatch desc = new ArgumentMismatch();
      desc.append(":\n").append(methodFormatter.toString());

      int parameterCount = values.length;

      if (parameterCount > 0) {
         desc.append("\n   with arguments: ");

         if (matchers == null) {
            desc.appendFormatted(values);
         }
         else {
            List<String> parameterTypes = methodFormatter.getParameterTypes();
            String sep = "";

            for (int i = 0; i < parameterCount; i++) {
               ArgumentMatcher matcher = getArgumentMatcher(i);
               String parameterType = parameterTypes.get(i);
               desc.append(sep).appendFormatted(parameterType, values[i], matcher);
               sep = ", ";
            }
         }
      }

      return desc.toString();
   }
}
