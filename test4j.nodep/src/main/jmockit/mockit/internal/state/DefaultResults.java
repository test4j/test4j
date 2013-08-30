/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

import mockit.*;
import mockit.external.asm4.Type;
import mockit.internal.util.*;

public final class DefaultResults
{
   private Map<String, ResultExtractor> defaultResults;
   private final Map<String, GenericReturnType> genericReturnTypes = new ConcurrentHashMap<String, GenericReturnType>();

   private static final class GenericReturnType
   {
      private final Map<String, Class<?>> typeVariables;
      private final List<String> components;

      GenericReturnType(String signature)
      {
         typeVariables = new HashMap<String, Class<?>>(3);

         if (signature.charAt(0) == '<') {
            extractTypeVariables(signature);
         }

         components = new ArrayList<String>(5);
         extractComponents(signature);
      }

      private void extractTypeVariables(String signature)
      {
         int q = signature.indexOf('>');
         String[] typeMappings = signature.substring(1, q).split(";");

         for (String typeMapping : typeMappings) {
            int p = typeMapping.indexOf(':');
            String typeVariable = typeMapping.substring(0, p);
            if (typeMapping.charAt(p + 1) == ':') p++;
            String typeName = typeMapping.substring(p + 2);
            typeVariables.put(typeVariable, Utilities.loadClassByInternalName(typeName));
         }
      }

      private void extractComponents(String signature)
      {
         int p = signature.indexOf(')') + 1;

         while (p < signature.length()) {
            char typeCode = signature.charAt(p);

            if (typeCode == 'L' || typeCode == 'T') {
               int q1 = signature.indexOf('<', p);
               int q2 = signature.indexOf(';', p);
               int q = q1 > 0 && q1 < q2 ? q1 : q2;
               components.add(signature.substring(p, q));
               p = q;
            }

            p++;
         }
      }

      boolean acceptsValueOfType(GenericReturnType resultType)
      {
         int n = components.size();

         if (n != resultType.components.size()) {
            return false;
         }

         for (int i = 0; i < n; i++) {
            String c1 = components.get(i);
            String c2 = resultType.components.get(i);
            boolean genericComponent = c1.charAt(0) == 'T';

            if (!genericComponent && !c1.equals(c2)) {
               return false;
            }
            else if (genericComponent && c2.charAt(0) == 'T') {
               if (!c1.equals(c2)) return false;
            }
            else if (genericComponent) {
               Class<?> type1 = typeVariables.get(c1.substring(1));

               if (type1 != null) {
                  Class<?> type2 = Utilities.loadClassByInternalName(c2.substring(1));

                  if (!type1.isAssignableFrom(type2)) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private static final class ResultExtractor
   {
      final Field inputField;
      final Object fieldOwner;
      ResultExtractor next;
      volatile int invocationsRemaining;
      volatile Object valueCache;

      ResultExtractor(Field inputField, Object fieldOwner)
      {
         this.inputField = inputField;
         this.fieldOwner = fieldOwner;
         invocationsRemaining = inputField.getAnnotation(Input.class).invocations();
      }

      void chainNextOne(ResultExtractor next)
      {
         this.next = next;

         if (invocationsRemaining == Integer.MAX_VALUE) {
            invocationsRemaining = 1;
         }
      }

      void extractException()
      {
         Object exception = getInputFieldValue();
         Utilities.throwCheckedException((Exception) exception);
      }

      Object getInputFieldValue()
      {
         if (invocationsRemaining <= 0) {
            return next == null ? null : next.getInputFieldValue();
         }

         invocationsRemaining--;
         Object valueFromCache = valueCache;

         if (valueFromCache != null) {
            return valueFromCache;
         }

         Object fieldValue = Utilities.getFieldValue(inputField, fieldOwner);

         if (fieldValue == null) {
            fieldValue = Utilities.newInstanceUsingDefaultConstructor(inputField.getType());
         }

         valueCache = fieldValue;
         return fieldValue;
      }
   }

   public void add(Field inputField, Object fieldOwner)
   {
      Class<?> fieldType = inputField.getType();
      String resultTypeDesc;

      if (Exception.class.isAssignableFrom(fieldType)) {
         resultTypeDesc = Type.getInternalName(fieldType);
      }
      else {
         resultTypeDesc = getReturnTypeDescriptor(inputField, fieldType);
      }

      addExtractor(resultTypeDesc, new ResultExtractor(inputField, fieldOwner));
   }

   private String getReturnTypeDescriptor(Field inputField, Class<?> fieldType)
   {
      String returnTypeDesc = Utilities.invoke(Field.class, inputField, "getGenericSignature");

      if (returnTypeDesc == null) {
         returnTypeDesc = Type.getDescriptor(fieldType);
      }

      return returnTypeDesc;
   }

   private void addExtractor(String resultTypeDesc, ResultExtractor resultExtractor)
   {
      if (defaultResults == null) {
         defaultResults = new LinkedHashMap<String, ResultExtractor>();
      }

      ResultExtractor previousExtractor = defaultResults.get(resultTypeDesc);

      if (previousExtractor == null) {
         defaultResults.put(resultTypeDesc, resultExtractor);
      }
      else {
         previousExtractor.chainNextOne(resultExtractor);
      }
   }

   public Object get(String signature, String[] exceptions)
   {
      if (defaultResults == null) {
         return null;
      }

      extractAndThrowExceptionIfSpecified(exceptions);

      String returnTypeDesc = DefaultValues.getReturnTypeDesc(signature);
      ResultExtractor extractor;

      if (signature.charAt(0) == '<' && !signature.startsWith("<init>") || returnTypeDesc.charAt(0) == 'T') {
         extractor = findResultForGenericType(signature);
      }
      else {
         extractor = defaultResults.get(returnTypeDesc);
      }

      return extractor == null ? null : extractor.getInputFieldValue();
   }

   private void extractAndThrowExceptionIfSpecified(String[] exceptions)
   {
      if (exceptions != null) {
         for (String exception : exceptions) {
            ResultExtractor extractor = defaultResults.get(exception);

            if (extractor != null) {
               extractor.extractException();
            }
         }
      }
   }

   private ResultExtractor findResultForGenericType(String signatureOfInvokedMethod)
   {
      GenericReturnType returnTypeOfInvokedMethod = findReturnType(signatureOfInvokedMethod);

      for (Entry<String, ResultExtractor> keyAndValue : defaultResults.entrySet()) {
         String resultTypeDesc = keyAndValue.getKey();

         if (resultTypeDesc.length() > 1) {
            GenericReturnType resultType = findReturnType(resultTypeDesc);

            if (returnTypeOfInvokedMethod.acceptsValueOfType(resultType)) {
               return keyAndValue.getValue();
            }
         }
      }

      return null;
   }

   private GenericReturnType findReturnType(String genericSignature)
   {
      GenericReturnType genericType = genericReturnTypes.get(genericSignature);

      if (genericType == null) {
         genericType = new GenericReturnType(genericSignature);
         genericReturnTypes.put(genericSignature, genericType);
      }

      return genericType;
   }

   void clear()
   {
      defaultResults = null;
   }
}