/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

import static mockit.internal.util.Utilities.*;

/**
 * Adapts the {@code org.hamcrest.Matcher} interface to {@link ArgumentMatcher}.
 */
@SuppressWarnings({"UnnecessaryFullyQualifiedName"})
public final class HamcrestAdapter implements ArgumentMatcher
{
   private final org.hamcrest.Matcher<?> hamcrestMatcher;

   public static ArgumentMatcher create(Object matcher)
   {
      if (matcher instanceof org.hamcrest.Matcher<?>) {
         return new HamcrestAdapter((org.hamcrest.Matcher<?>) matcher);
      }

      return new ReflectiveMatcher(matcher);
   }

   private HamcrestAdapter(org.hamcrest.Matcher<?> matcher) { hamcrestMatcher = matcher; }

   public boolean matches(Object argValue)
   {
      return hamcrestMatcher.matches(argValue);
   }

   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch)
   {
      org.hamcrest.Description strDescription = new org.hamcrest.StringDescription();
      hamcrestMatcher.describeTo(strDescription);
      argumentMismatch.append(strDescription.toString());
   }

   public Object getInnerValue()
   {
      Object innermostMatcher = getInnermostMatcher();

      return getArgumentValueFromMatcherIfAvailable(innermostMatcher);
   }

   private Object getInnermostMatcher()
   {
      org.hamcrest.Matcher<?> innerMatcher = hamcrestMatcher;

      while (innerMatcher instanceof org.hamcrest.core.Is || innerMatcher instanceof org.hamcrest.core.IsNot) {
         innerMatcher = getField(innerMatcher.getClass(), org.hamcrest.Matcher.class, innerMatcher);
      }

      return innerMatcher;
   }

   private Object getArgumentValueFromMatcherIfAvailable(Object argMatcher)
   {
      if (
         argMatcher instanceof org.hamcrest.core.IsEqual || argMatcher instanceof org.hamcrest.core.IsSame ||
         "org.hamcrest.number.OrderingComparison".equals(argMatcher.getClass().getName())
      ) {
         return getField(argMatcher.getClass(), Object.class, argMatcher);
      }

      return null;
   }
}
