/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

import java.util.regex.*;

public final class PatternMatcher implements ArgumentMatcher
{
   private final Pattern pattern;

   public PatternMatcher(String regex) { pattern = Pattern.compile(regex); }

   public boolean matches(Object argValue)
   {
      return pattern.matcher((CharSequence) argValue).matches();
   }

   public void writeMismatchPhrase(ArgumentMismatch argumentMismatch)
   {
      argumentMismatch.append("a string matching \"").append(pattern.toString()).append('"');
   }
}
