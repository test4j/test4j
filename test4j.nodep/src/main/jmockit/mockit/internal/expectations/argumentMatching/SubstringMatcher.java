/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

abstract class SubstringMatcher implements ArgumentMatcher
{
   final String substring;

   SubstringMatcher(CharSequence substring) { this.substring = substring.toString(); }
}