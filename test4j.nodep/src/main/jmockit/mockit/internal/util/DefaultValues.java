/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

import java.lang.reflect.*;
import java.util.*;

import mockit.external.asm4.Type;

import static java.util.Collections.*;

/**
 * Provides default values for each type, typically used for returning default values according to
 * method return types.
 */
public final class DefaultValues
{
   private DefaultValues() {}

   private static final Map<String, Object> TYPE_DESC_TO_VALUE_MAP = new HashMap<String, Object>()
   {
      {
         put("Z", false);
         put("C", '\0');
         put("B", (byte) 0);
         put("S", (short) 0);
         put("I", 0);
         put("F", 0.0F);
         put("J", 0L);
         put("D", 0.0);
         put("Ljava/util/Collection;", emptyList());
         put("Ljava/util/List;", emptyList());
         put("Ljava/util/Set;", emptySet());
         put("Ljava/util/SortedSet;", unmodifiableSortedSet(new TreeSet<Object>()));
         put("Ljava/util/Map;", emptyMap());
         put("Ljava/util/SortedMap;", unmodifiableSortedMap(new TreeMap<Object, Object>()));
      }
   };

   private static final Map<String, Object> ELEM_TYPE_TO_ONE_D_ARRAY = new HashMap<String, Object>()
   {
      {
         put("[Z", new boolean[0]);
         put("[C", new char[0]);
         put("[B", new byte[0]);
         put("[S", new short[0]);
         put("[I", new int[0]);
         put("[F", new float[0]);
         put("[J", new long[0]);
         put("[D", new double[0]);
         put("[Ljava/lang/Object;", new Object[0]);
         put("[Ljava/lang/String;", new String[0]);
      }
   };

   public static Object computeForReturnType(String methodNameAndDesc)
   {
      String typeDesc = getReturnTypeDesc(methodNameAndDesc);
      return computeForType(typeDesc);
   }

   public static String getReturnTypeDesc(String methodNameAndDesc)
   {
      if (methodNameAndDesc == null) return null;
      int rightParen = methodNameAndDesc.indexOf(')') + 1;
      return methodNameAndDesc.substring(rightParen);
   }

   public static Object computeForType(String typeDesc)
   {
      char typeDescChar = typeDesc.charAt(0);

      if (typeDescChar == 'V') {
         return null;
      }

      Object defaultValue = TYPE_DESC_TO_VALUE_MAP.get(typeDesc);

      if (defaultValue != null) {
         return defaultValue;
      }

      if (typeDescChar == 'L') {
         return null;
      }

      // It's an array.
      Object emptyArray = ELEM_TYPE_TO_ONE_D_ARRAY.get(typeDesc);

      if (emptyArray == null) {
         emptyArray = newEmptyArray(typeDesc);
      }

      return emptyArray;
   }

   private static Object newEmptyArray(String typeDesc)
   {
      Type type = Type.getType(typeDesc);
      Class<?> elementType = Utilities.getClassForType(type.getElementType());

      return Array.newInstance(elementType, new int[type.getDimensions()]);
   }

   public static Object computeForType(Class<?> type)
   {
      if (type.isArray()) {
         return Array.newInstance(type.getComponentType(), 0);
      }
      else if (type != void.class && type.isPrimitive()) {
         return defaultValueForPrimitiveType(type);
      }

      return null;
   }

   public static Object defaultValueForPrimitiveType(Class<?> type)
   {
      if (type == int.class) {
         return 0;
      }
      else if (type == boolean.class) {
         return false;
      }
      else if (type == long.class) {
         return 0L;
      }
      else if (type == double.class) {
         return 0.0;
      }
      else if (type == float.class) {
         return 0.0F;
      }
      else if (type == char.class) {
         return '\0';
      }
      else if (type == byte.class) {
         return (byte) 0;
      }
      else {
         return (short) 0;
      }
   }

   public static Object computeForWrapperType(java.lang.reflect.Type type)
   {
      if (type == Integer.class) {
         return 0;
      }
      else if (type == Boolean.class) {
         return false;
      }
      else if (type == Long.class) {
         return 0L;
      }
      else if (type == Double.class) {
         return 0.0;
      }
      else if (type == Float.class) {
         return 0.0F;
      }
      else if (type == Character.class) {
         return '\0';
      }
      else if (type == Byte.class) {
         return (byte) 0;
      }
      else if (type == Short.class) {
         return (short) 0;
      }

      return null;
   }
}
