/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

public final class InvocationConstraints
{
   public int minInvocations;
   int maxInvocations;
   public int invocationCount;

   public InvocationConstraints(boolean nonStrictInvocation)
   {
      setLimits(nonStrictInvocation ? 0 : 1, nonStrictInvocation ? -1 : 1);
   }

   public void setLimits(int minInvocations, int maxInvocations)
   {
      this.minInvocations = minInvocations;
      this.maxInvocations = maxInvocations;
   }

   void adjustMaxInvocations(int expectedInvocationCount)
   {
      if (maxInvocations > 0 && maxInvocations < expectedInvocationCount) {
         maxInvocations = expectedInvocationCount;
      }
   }

   void setUnlimitedMaxInvocations()
   {
      maxInvocations = -1;
   }

   public boolean incrementInvocationCount()
   {
      invocationCount++;
      return invocationCount == maxInvocations;
   }

   public boolean isInvocationCountLessThanMinimumExpected()
   {
      return invocationCount < minInvocations;
   }

   public boolean isInvocationCountMoreThanMaximumExpected()
   {
      return maxInvocations >= 0 && invocationCount > maxInvocations;
   }

   public boolean isInvocationCountInExpectedRange()
   {
      return minInvocations <= invocationCount && (invocationCount <= maxInvocations || maxInvocations < 0);
   }

   public Error verifyLowerLimit(ExpectedInvocation invocation, int minInvocations)
   {
      if (invocationCount < minInvocations) {
         int missingInvocations = minInvocations - invocationCount;
         return invocation.errorForMissingInvocations(missingInvocations);
      }

      return null;
   }

   public Error verifyUpperLimit(ExpectedInvocation invocation, Object[] replayArgs, int maxInvocations)
   {
      if (maxInvocations >= 0) {
         int unexpectedInvocations = invocationCount - maxInvocations;

         if (unexpectedInvocations > 0) {
            return invocation.errorForUnexpectedInvocations(replayArgs, unexpectedInvocations);
         }
      }

      return null;
   }

   public Error errorForMissingExpectations(ExpectedInvocation invocation)
   {
      return invocation.errorForMissingInvocations(minInvocations - invocationCount) ;
   }
}
