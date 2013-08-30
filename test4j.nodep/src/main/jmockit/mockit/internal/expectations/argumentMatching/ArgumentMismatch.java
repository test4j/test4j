/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.argumentMatching;

import java.lang.reflect.*;

public final class ArgumentMismatch
{
   private final StringBuilder out = new StringBuilder(50);
   private String parameterType;
   private boolean finished;

   public String getParameterType() { return parameterType; }

   public boolean isFinished() { return finished; }
   void markAsFinished() { finished = true; }

   @Override
   public String toString() { return out.toString(); }

   public ArgumentMismatch append(char c) { out.append(c); return this; }
   public ArgumentMismatch append(int i) { out.append(i); return this; }
   public ArgumentMismatch append(double d) { out.append(d); return this; }
   public ArgumentMismatch append(CharSequence str) { out.append(str); return this; }

   public void appendFormatted(String parameterType, Object argumentValue, ArgumentMatcher matcher)
   {
      if (matcher == null) {
         appendFormatted(argumentValue);
      }
      else {
         this.parameterType = parameterType;
         matcher.writeMismatchPhrase(this);
      }
   }

   public void appendFormatted(Object value)
   {
      if (value == null) {
         out.append("null");
      }
      else if (value instanceof CharSequence) {
         appendCharacters((CharSequence) value);
      }
      else if (value instanceof Character) {
         out.append('"');
         appendEscapedOrPlainCharacter((Character) value);
         out.append('"');
      }
      else if (value instanceof Byte) {
         out.append(value).append('b');
      }
      else if (value instanceof Short) {
         out.append(value).append('s');
      }
      else if (value instanceof Long) {
         out.append(value).append('L');
      }
      else if (value instanceof Float) {
         out.append(value).append('F');
      }
      else if (value.getClass().isArray()) {
         appendArray(value);
      }
      else if (value instanceof ArgumentMatcher) {
         ((ArgumentMatcher) value).writeMismatchPhrase(this);
      }
      else {
         out.append(value);
      }
   }

   private void appendArray(Object array)
   {
      out.append('[');
      String separator = "";

      for (int i = 0, n = Array.getLength(array); i < n; i++) {
         Object nextValue = Array.get(array, i);
         out.append(separator);
         appendFormatted(nextValue);
         separator = ", ";
      }

      out.append(']');
   }

   private void appendCharacters(CharSequence characters)
   {
      out.append('"');

      for (int i = 0, n = characters.length(); i < n; i++) {
         char c = characters.charAt(i);
         appendEscapedOrPlainCharacter(c);
      }

      out.append('"');
   }

   private void appendEscapedOrPlainCharacter(char c)
   {
      switch (c) {
         case '"':
            out.append("\\\"");
            break;
         case '\t':
            out.append("\\t");
            break;
         case '\n':
            out.append("\\n");
            break;
         case '\r':
            out.append("\\r");
            break;
         default:
            out.append(c);
      }
   }

   public void appendFormatted(Object[] values)
   {
      String separator = "";

      for (Object value : values) {
         append(separator).appendFormatted(value);
         separator = ", ";
      }
   }
}
