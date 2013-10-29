/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.io.*;
import java.lang.reflect.*;
import java.nio.*;
import java.util.*;

import mockit.internal.expectations.invocation.*;
import mockit.internal.util.*;

final class ReturnTypeConversion
{
   private final Expectation expectation;
   private final Class<?> returnType;
   private final Object value;

   ReturnTypeConversion(Expectation expectation, Class<?> returnType, Object value)
   {
      this.expectation = expectation;
      this.returnType = returnType;
      this.value = value;
   }

   void addConvertedValueOrValues()
   {
      boolean valueIsArray = value != null && value.getClass().isArray();
      boolean valueIsIterable = value instanceof Iterable<?>;

      if (valueIsArray || valueIsIterable || value instanceof Iterator<?>) {
         if (returnType == void.class || hasReturnOfDifferentType()) {
            if (valueIsArray) {
               expectation.getResults().addReturnValues(value);
            }
            else if (valueIsIterable) {
               expectation.getResults().addReturnValues((Iterable<?>) value);
            }
            else {
               expectation.getResults().addDeferredReturnValues((Iterator<?>) value);
            }

            return;
         }
      }

      expectation.substituteCascadedMockToBeReturnedIfNeeded(value);
      expectation.getResults().addReturnValue(value);
   }

   private boolean hasReturnOfDifferentType()
   {
      return
         !returnType.isArray() &&
         !Iterable.class.isAssignableFrom(returnType) && !Iterator.class.isAssignableFrom(returnType) &&
         !returnType.isAssignableFrom(value.getClass());
   }

   void addConvertedValue()
   {
      Class<?> wrapperType =
         AutoBoxing.isWrapperOfPrimitiveType(returnType) ? returnType : AutoBoxing.getWrapperType(returnType);
      Class<?> valueType = value.getClass();

      if (valueType == wrapperType) {
         expectation.getResults().addReturnValueResult(value);
      }
      else if (wrapperType != null && AutoBoxing.isWrapperOfPrimitiveType(valueType)) {
         addPrimitiveValueConvertingAsNeeded(wrapperType);
      }
      else {
         boolean valueIsArray = valueType.isArray();

         if (valueIsArray || value instanceof Iterable<?> || value instanceof Iterator<?>) {
            addMultiValuedResultBasedOnTheReturnType(valueIsArray);
         }
         else if (wrapperType != null) {
            throw newIncompatibleTypesException();
         }
         else {
            addResultFromSingleValue();
         }
      }
   }

   private void addMultiValuedResultBasedOnTheReturnType(boolean valueIsArray)
   {
      if (returnType == void.class) {
         addMultiValuedResult(valueIsArray);
      }
      else if (returnType == Object.class) {
         expectation.getResults().addReturnValueResult(value);
      }
      else if (valueIsArray && addCollectionOrMapWithElementsFromArray()) {
         return;
      }
      else if (hasReturnOfDifferentType()) {
         addMultiValuedResult(valueIsArray);
      }
      else {
         expectation.getResults().addReturnValueResult(value);
      }
   }

   private void addMultiValuedResult(boolean valueIsArray)
   {
      if (valueIsArray) {
         expectation.getResults().addResults(value);
      }
      else if (value instanceof Iterable<?>) {
         expectation.getResults().addResults((Iterable<?>) value);
      }
      else {
         expectation.getResults().addDeferredResults((Iterator<?>) value);
      }
   }

   private boolean addCollectionOrMapWithElementsFromArray()
   {
      int n = Array.getLength(value);
      Object values = null;

      if (returnType.isAssignableFrom(ListIterator.class)) {
         List<Object> list = new ArrayList<Object>(n);
         addArrayElements(list, n);
         values = list.listIterator();
      }
      else if (returnType.isAssignableFrom(List.class)) {
         values = addArrayElements(new ArrayList<Object>(n), n);
      }
      else if (returnType.isAssignableFrom(Set.class)) {
         values = addArrayElements(new LinkedHashSet<Object>(n), n);
      }
      else if (returnType.isAssignableFrom(SortedSet.class)) {
         values = addArrayElements(new TreeSet<Object>(), n);
      }
      else if (returnType.isAssignableFrom(Map.class)) {
         values = addArrayElements(new LinkedHashMap<Object, Object>(n), n);
      }
      else if (returnType.isAssignableFrom(SortedMap.class)) {
         values = addArrayElements(new TreeMap<Object, Object>(), n);
      }

      if (values != null) {
         expectation.getResults().addReturnValue(values);
         return true;
      }

      return false;
   }

   private Object addArrayElements(Collection<Object> values, int elementCount)
   {
      for (int i = 0; i < elementCount; i++) {
         Object element = Array.get(value, i);
         values.add(element);
      }

      return values;
   }

   private Object addArrayElements(Map<Object, Object> values, int elementPairCount)
   {
      for (int i = 0; i < elementPairCount; i++) {
         Object keyAndValue = Array.get(value, i);

         if (keyAndValue == null || !keyAndValue.getClass().isArray()) {
            return null;
         }

         Object key = Array.get(keyAndValue, 0);
         Object element = Array.getLength(keyAndValue) > 1 ? Array.get(keyAndValue, 1) : null;
         values.put(key, element);
      }

      return values;
   }

   private void addResultFromSingleValue()
   {
      if (returnType == Object.class) {
         expectation.getResults().addReturnValueResult(value);
      }
      else if (returnType == void.class) {
         throw newIncompatibleTypesException();
      }
      else if (returnType.isArray()) {
         Object array = Array.newInstance(returnType.getComponentType(), 1);
         Array.set(array, 0, value);
         expectation.getResults().addReturnValueResult(array);
      }
      else if (returnType.isAssignableFrom(ArrayList.class)) {
         addCollectionWithSingleElement(new ArrayList<Object>(1));
      }
      else if (returnType.isAssignableFrom(LinkedList.class)) {
         addCollectionWithSingleElement(new LinkedList<Object>());
      }
      else if (returnType.isAssignableFrom(HashSet.class)) {
         addCollectionWithSingleElement(new HashSet<Object>(1));
      }
      else if (returnType.isAssignableFrom(TreeSet.class)) {
         addCollectionWithSingleElement(new TreeSet<Object>());
      }
      else if (returnType.isAssignableFrom(ListIterator.class)) {
         List<Object> l = new ArrayList<Object>(1);
         l.add(value);
         expectation.getResults().addReturnValueResult(l.listIterator());
      }
      else if (value instanceof CharSequence) {
         addCharSequence((CharSequence) value);
      }
      else {
         throw newIncompatibleTypesException();
      }
   }

   private void addCollectionWithSingleElement(Collection<Object> container)
   {
      container.add(value);
      expectation.getResults().addReturnValueResult(container);
   }

   private void addCharSequence(CharSequence value)
   {
      Object convertedValue = value;

      if (returnType.isAssignableFrom(ByteArrayInputStream.class)) {
         convertedValue = new ByteArrayInputStream(value.toString().getBytes());
      }
      else if (returnType.isAssignableFrom(StringReader.class)) {
         convertedValue = new StringReader(value.toString());
      }
      else if (!(value instanceof StringBuilder) && returnType.isAssignableFrom(StringBuilder.class)) {
         convertedValue = new StringBuilder(value);
      }
      else if (!(value instanceof CharBuffer) && returnType.isAssignableFrom(CharBuffer.class)) {
         convertedValue = CharBuffer.wrap(value);
      }

      expectation.getResults().addReturnValueResult(convertedValue);
   }

   private IllegalArgumentException newIncompatibleTypesException()
   {
      ExpectedInvocation invocation = expectation.invocation;
      String valueTypeName = value.getClass().getName().replace("java.lang.", "");
      String returnTypeName = returnType.getName().replace("java.lang.", "");

      StringBuilder msg = new StringBuilder(200);
      msg.append("Value of type ").append(valueTypeName);
      msg.append(" incompatible with return type ").append(returnTypeName).append(" of ");
      msg.append(new MethodFormatter(invocation.getClassDesc(), invocation.getMethodNameAndDescription()));

      return new IllegalArgumentException(msg.toString());
   }

   private void addPrimitiveValueConvertingAsNeeded(Class<?> targetType)
   {
      Object convertedValue = null;

      if (value instanceof Number) {
         convertedValue = convertFromNumber(targetType, (Number) value);
      }
      else if (value instanceof Character) {
         convertedValue = convertFromChar(targetType, (Character) value);
      }

      if (convertedValue == null) {
         throw newIncompatibleTypesException();
      }

      expectation.getResults().addReturnValueResult(convertedValue);
   }

   private Object convertFromNumber(Class<?> targetType, Number number)
   {
      if (targetType == Integer.class) {
         return number.intValue();
      }
      else if (targetType == Short.class) {
         return number.shortValue();
      }
      else if (targetType == Long.class) {
         return number.longValue();
      }
      else if (targetType == Byte.class) {
         return number.byteValue();
      }
      else if (targetType == Double.class) {
         return number.doubleValue();
      }
      else if (targetType == Float.class) {
         return number.floatValue();
      }
      else if (targetType == Character.class) {
         return (char) number.intValue();
      }

      return null;
   }

   private Object convertFromChar(Class<?> targetType, char c)
   {
      if (targetType == Integer.class) {
         return (int) c;
      }
      else if (targetType == Short.class) {
         return (short) c;
      }
      else if (targetType == Long.class) {
         return (long) c;
      }
      else if (targetType == Byte.class) {
         return (byte) c;
      }
      else if (targetType == Double.class) {
         return (double) c;
      }
      else if (targetType == Float.class) {
         return (float) c;
      }

      return null;
   }
}
