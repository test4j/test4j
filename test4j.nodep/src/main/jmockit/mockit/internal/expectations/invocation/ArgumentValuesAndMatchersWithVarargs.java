/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import java.lang.reflect.*;
import java.util.*;

import mockit.internal.*;
import mockit.internal.expectations.argumentMatching.*;

final class ArgumentValuesAndMatchersWithVarargs extends ArgumentValuesAndMatchers
{
   ArgumentValuesAndMatchersWithVarargs(InvocationArguments signature, Object[] values) { super(signature, values); }

   @Override
   boolean isMatch(Object[] replayArgs, Map<Object, Object> instanceMap)
   {
      if (matchers == null) {
         return areEqual(replayArgs, instanceMap);
      }

      VarargsComparison varargsComparison = new VarargsComparison(replayArgs);
      int n = varargsComparison.getTotalArgumentCountWhenDifferent();

      if (n < 0) {
         return false;
      }

      for (int i = 0; i < n; i++) {
         Object actual = varargsComparison.getOtherArgument(i);
         ArgumentMatcher expected = getArgumentMatcher(i);

         if (expected == null) {
            Object arg = varargsComparison.getThisArgument(i);
            if (arg == null) continue;
            expected = new EqualityMatcher(arg);
         }

         if (!expected.matches(actual)) {
            return false;
         }
      }

      return true;
   }

   private boolean areEqual(Object[] replayArgs, Map<Object, Object> instanceMap)
   {
      int argCount = replayArgs.length;

      if (!areEqual(values, replayArgs, argCount - 1, instanceMap)) {
         return false;
      }

      VarargsComparison varargsComparison = new VarargsComparison(replayArgs);
      Object[] expectedValues = varargsComparison.thisVarArgs;
      Object[] actualValues = varargsComparison.otherVarArgs;

      return
         varargsComparison.sameVarargArrayLength() &&
         areEqual(expectedValues, actualValues, expectedValues.length, instanceMap);
   }

   @Override
   Error assertMatch(Object[] replayArgs, Map<Object, Object> instanceMap)
   {
      if (matchers == null) {
         return assertEquality(replayArgs, instanceMap);
      }

      VarargsComparison varargsComparison = new VarargsComparison(replayArgs);
      int n = varargsComparison.getTotalArgumentCountWhenDifferent();

      if (n < 0) {
         return varargsComparison.errorForVarargsArraysOfDifferentLengths();
      }

      for (int i = 0; i < n; i++) {
         Object actual = varargsComparison.getOtherArgument(i);
         ArgumentMatcher expected = getArgumentMatcher(i);

         if (expected == null) {
            Object arg = varargsComparison.getThisArgument(i);
            if (arg == null) continue;
            expected = new EqualityMatcher(arg);
         }

         if (!expected.matches(actual)) {
            return signature.argumentMismatchMessage(i, expected, actual);
         }
      }

      return null;
   }

   private Error assertEquality(Object[] replayArgs, Map<Object, Object> instanceMap)
   {
      int argCount = replayArgs.length;
      Error nonVarargsError = assertEquals(values, replayArgs, argCount - 1, instanceMap);

      if (nonVarargsError != null) {
         return nonVarargsError;
      }

      VarargsComparison varargsComparison = new VarargsComparison(replayArgs);
      Object[] expectedValues = varargsComparison.thisVarArgs;
      Object[] actualValues = varargsComparison.otherVarArgs;

      if (!varargsComparison.sameVarargArrayLength()) {
         return varargsComparison.errorForVarargsArraysOfDifferentLengths();
      }

      Error varargsError = assertEquals(expectedValues, actualValues, expectedValues.length, instanceMap);

      if (varargsError != null) {
         return new UnexpectedInvocation("Varargs " + varargsError);
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

      VarargsComparison varargsComparison = new VarargsComparison(other.values);
      int n = varargsComparison.getTotalArgumentCountWhenDifferent();

      if (n < 0) {
         return false;
      }

      while (i < n) {
         Object thisArg = varargsComparison.getThisArgument(i);
         Object otherArg = varargsComparison.getOtherArgument(i);

         if (!EqualityMatcher.areEqual(thisArg, otherArg)) {
            return false;
         }

         i++;
      }

      return true;
   }
   
   private final class VarargsComparison
   {
      private final Object[] otherValues;
      final Object[] thisVarArgs;
      final Object[] otherVarArgs;
      private final int regularArgCount;

      VarargsComparison(Object[] otherValues)
      {
         this.otherValues = otherValues;
         thisVarArgs = getVarArgs(values);
         otherVarArgs = getVarArgs(otherValues);
         regularArgCount = values.length - 1;
      }

      private Object[] getVarArgs(Object[] args)
      {
         Object lastArg = args[args.length - 1];

         if (lastArg == null) {
            return NULL_VARARGS;
         }
         else if (lastArg instanceof Object[]) {
            return (Object[]) lastArg;
         }

         int varArgsLength = Array.getLength(lastArg);
         Object[] results = new Object[varArgsLength];

         for (int i = 0; i < varArgsLength; i++) {
            results[i] = Array.get(lastArg, i);
         }

         return results;
      }

      int getTotalArgumentCountWhenDifferent()
      {
         if (thisVarArgs == NULL_VARARGS) {
            return regularArgCount;
         }

         if (!sameVarargArrayLength()) {
            return -1;
         }

         return regularArgCount + thisVarArgs.length;
      }

      boolean sameVarargArrayLength() { return thisVarArgs.length == otherVarArgs.length; }

      Object getThisArgument(int parameter)
      {
         return parameter < regularArgCount ? values[parameter] : thisVarArgs[parameter - regularArgCount];
      }

      Object getOtherArgument(int parameter)
      {
         return parameter < regularArgCount ? otherValues[parameter] : otherVarArgs[parameter - regularArgCount];
      }

      Error errorForVarargsArraysOfDifferentLengths()
      {
         return new UnexpectedInvocation(
            "Expected " + thisVarArgs.length + " values for varargs parameter, got " + otherVarArgs.length);
      }
   }
}
