/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

public final class StringContainmentMatcher extends SubstringMatcher
{
   public StringContainmentMatcher(CharSequence substring) { super(substring); }

   public boolean matches(Object string)
   {
      return string instanceof CharSequence && string.toString().contains(substring);
   }

   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch)
   {
      argumentMismatch.append("a string containing ").appendFormatted(substring);
   }
}