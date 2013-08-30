/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.filtering;

import java.util.*;
import java.util.regex.*;

import mockit.external.asm4.*;

public final class MockingConfiguration
{
   private final List<MockFilter> filtersToApply;
   private final boolean desiredFilterResultWhenMatching;

   public MockingConfiguration(String[] filters, boolean desiredFilterResultWhenMatching)
   {
      filtersToApply = parseMockFilters(filters);
      this.desiredFilterResultWhenMatching = desiredFilterResultWhenMatching;
   }

   private List<MockFilter> parseMockFilters(String[] mockFilters)
   {
      List<MockFilter> filters = new ArrayList<MockFilter>(mockFilters.length);

      for (String mockFilter : mockFilters) {
         filters.add(new RegexMockFilter(mockFilter));
      }

      return filters;
   }

   public boolean matchesFilters(String name, String desc)
   {
      for (MockFilter filter : filtersToApply) {
         if (filter.matches(name, desc)) {
            return desiredFilterResultWhenMatching;
         }
      }

      return !desiredFilterResultWhenMatching;
   }

   private static final class RegexMockFilter implements MockFilter
   {
      private static final Pattern CONSTRUCTOR_NAME_REGEX = Pattern.compile("<init>");
      private static final String[] ANY_PARAMS = {};

      private final Pattern nameRegex;
      private final String[] paramTypeNames;

      private RegexMockFilter(String filter)
      {
         int lp = filter.indexOf('(');
         int rp = filter.indexOf(')');

         if (lp < 0 && rp >= 0 || lp >= 0 && lp >= rp) {
            throw new IllegalArgumentException("Invalid filter: " + filter);
         }

         if (lp == 0) {
            nameRegex = CONSTRUCTOR_NAME_REGEX;
         }
         else {
            nameRegex = Pattern.compile(lp < 0 ? filter : filter.substring(0, lp));
         }

         paramTypeNames = parseParameterTypes(filter, lp, rp);
      }

      private String[] parseParameterTypes(String filter, int lp, int rp)
      {
         if (lp < 0) {
            return ANY_PARAMS;
         }
         else if (lp == rp - 1) {
            return null;
         }
         else {
            String[] typeNames = filter.substring(lp + 1, rp).split(",");

            for (int i = 0; i < typeNames.length; i++) {
               typeNames[i] = typeNames[i].trim();
            }

            return typeNames;
         }
      }

      public boolean matches(String name, String desc)
      {
         if (!nameRegex.matcher(name).matches()) {
            return false;
         }

         if (paramTypeNames == ANY_PARAMS) {
            return true;
         }
         else if (paramTypeNames == null) {
            return desc.charAt(1) == ')';
         }

         Type[] argTypes = Type.getArgumentTypes(desc);

         if (argTypes.length != paramTypeNames.length) {
            return false;
         }

         for (int i = 0; i < paramTypeNames.length; i++) {
            Type argType = argTypes[i];
            String paramTypeName = argType.getClassName();

            if (!paramTypeName.endsWith(paramTypeNames[i])) {
               return false;
            }
         }

         return true;
      }
   }
}
