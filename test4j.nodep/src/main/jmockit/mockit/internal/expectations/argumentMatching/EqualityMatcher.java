/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

import java.lang.reflect.Array;

public class EqualityMatcher implements ArgumentMatcher
{
   final Object object;

   public EqualityMatcher(Object equalArg) { object = equalArg; }
   public boolean matches(Object argValue) { return areEqual(argValue, object); }
   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch) { argumentMismatch.appendFormatted(object); }

   public static boolean areEqual(Object o1, Object o2)
   {
      if (o1 == null) {
         return o2 == null;
      }
      else if (o2 == null) {
         return false;
      }
      else if (o1 == o2) {
         return true;
      }

      return areEqualWhenNonNull(o1, o2);
   }

   public static boolean areEqualWhenNonNull(Object o1, Object o2)
   {
      if (isArray(o1)) {
         return isArray(o2) && areArraysEqual(o1, o2);
      }

      return o1.equals(o2);
   }

   private static boolean isArray(Object o) { return o.getClass().isArray(); }

   private static boolean areArraysEqual(Object array1, Object array2)
   {
      int length1 = Array.getLength(array1);

      if (length1 != Array.getLength(array2)) {
         return false;
      }

      for (int i = 0; i < length1; i++) {
         Object value1 = Array.get(array1, i);
         Object value2 = Array.get(array2, i);

         if (!areEqual(value1, value2)) {
            return false;
         }
      }

      return true;
   }
}
