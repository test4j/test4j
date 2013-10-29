/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

/**
 * Matches a decimal argument against another within a given margin of error.
 */
public final class NumericEqualityMatcher implements ArgumentMatcher
{
   private final double value;
   private final double delta;

   public NumericEqualityMatcher(double value, double delta)
   {
      this.value = value;
      this.delta = delta;
   }

   public boolean matches(Object decimalValue)
   {
      return decimalValue instanceof Number && actualDelta((Number) decimalValue) <= delta;
   }

   private double actualDelta(Number decimalValue)
   {
      return Math.abs(decimalValue.doubleValue() - value);
   }

   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch)
   {
      argumentMismatch.append("a numeric value within ").append(delta).append(" of ").append(value);
   }
}
