/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.annotations;

import java.lang.reflect.*;
import java.util.*;

import mockit.internal.state.*;
import mockit.internal.util.*;

/**
 * A container for the mock methods "collected" from a mock class, separated in two sets: one with all the mock methods,
 * and another with just the subset of static methods.
 */
final class AnnotatedMockMethods
{
   final Class<?> realClass;
   private final List<MockMethod> methods;
   private final Map<String, String> typeParametersToTypeArguments;
   private String mockClassInternalName;
   private boolean isInnerMockClass;
   private boolean withItField;
   private List<MockState> mockStates;
   Class<?> classWithMethodToSelectSubclasses;

   final class MockMethod
   {
      final String name;
      final String desc;
      final boolean isStatic;
      final boolean hasInvocationParameter;
      String mockedMethodDesc;
      private final String mockDescWithoutInvocationParameter;
      private GenericSignature mockSignature;
      private int indexForMockState;

      private MockMethod(String nameAndDesc, boolean isStatic)
      {
         int p = nameAndDesc.indexOf('(');
         name = nameAndDesc.substring(0, p);
         desc = nameAndDesc.substring(p);
         this.isStatic = isStatic;
         hasInvocationParameter = desc.startsWith("(Lmockit/Invocation;");
         mockDescWithoutInvocationParameter = hasInvocationParameter ? '(' + desc.substring(20) : desc;
         indexForMockState = -1;
      }

      boolean isMatch(String name, String desc, String signature)
      {
         if (this.name.equals(name)) {
            if (hasMatchingParameters(desc, signature)) {
               mockedMethodDesc = desc;
               return true;
            }
         }

         return false;
      }

      private boolean hasMatchingParameters(String desc, String signature)
      {
         boolean sameParametersIgnoringGenerics = mockDescWithoutInvocationParameter.equals(desc);

         if (sameParametersIgnoringGenerics || signature == null) {
            return sameParametersIgnoringGenerics;
         }

         if (mockSignature == null) {
            mockSignature = new GenericSignature(mockDescWithoutInvocationParameter);
         }

         GenericSignature mockedSignature = new GenericSignature(signature);
         return mockSignature.satisfiesGenericSignature(mockedSignature);
      }

      boolean isForGenericMethod() { return mockSignature != null; }

      Class<?> getRealClass() { return realClass; }
      String getMockNameAndDesc() { return name + desc; }
      boolean isForConstructor() { return "$init".equals(name); }
      boolean hasMatchingRealMethod() { return mockedMethodDesc != null; }
      int getIndexForMockState() { return indexForMockState; }

      boolean isReentrant() { return indexForMockState >= 0 && mockStates.get(indexForMockState).isReentrant(); }
      boolean isDynamic() { return isReentrant() || hasInvocationParameter && isForConstructor(); }

      String errorMessage(String quantifier, int numExpectedInvocations, int timesInvoked)
      {
         String nameAndDesc = getMockNameAndDesc();
         return
            "Expected " + quantifier + ' ' + numExpectedInvocations + " invocation(s) of " +
            new MethodFormatter(mockClassInternalName, nameAndDesc) + ", but was invoked " + timesInvoked + " time(s)";
      }

      @Override
      public boolean equals(Object obj)
      {
         MockMethod other = (MockMethod) obj;
         return realClass == other.getRealClass() && name.equals(other.name) && desc.equals(other.desc);
      }

      @Override
      public int hashCode()
      {
         return 31 * (31 * realClass.hashCode() + name.hashCode()) + desc.hashCode();
      }
   }

   private final class GenericSignature
   {
      private final List<String> parameters;

      GenericSignature(String signature)
      {
         String semicolonSeparatedParameters = getSemiColonSeparatedParameters(signature);
         int n = semicolonSeparatedParameters.length();
         parameters = new ArrayList<String>(n);

         for (int i = 0; i < n; i++) {
            i = addNextParameter(semicolonSeparatedParameters, i);
         }
      }

      private String getSemiColonSeparatedParameters(String signature)
      {
         int p = signature.indexOf('(');
         int q = signature.indexOf(')');
         return signature.substring(p + 1, q);
      }

      private int addNextParameter(String semicolonSeparatedParameters, int i)
      {
         char c = semicolonSeparatedParameters.charAt(i);
         int j = i;
         String parameter;

         if (c == 'L' || c == 'T' || c == '[') {
            do {
               j++;
               c = semicolonSeparatedParameters.charAt(j);
            } while (c != ';' && c != '<');

            parameter = semicolonSeparatedParameters.substring(i, j);

            if (c == '<') {
               j = advanceToNextParameter(semicolonSeparatedParameters, j);
            }
         }
         else {
            parameter = String.valueOf(c);
         }

         parameters.add(parameter);
         return j;
      }

      private int advanceToNextParameter(String semicolonSeparatedParameters, int positionOfCurrentParameter)
      {
         int currentPos = positionOfCurrentParameter;
         int angleBracketDepth = 1;

         do {
            currentPos++;
            char c = semicolonSeparatedParameters.charAt(currentPos);
            if (c == '>') angleBracketDepth--; else if (c == '<') angleBracketDepth++;
         } while (angleBracketDepth > 0);

         return currentPos + 1;
      }

      boolean satisfiesGenericSignature(GenericSignature otherSignature)
      {
         int n = parameters.size();

         if (n != otherSignature.parameters.size()) {
            return false;
         }

         for (int i = 0; i < n; i++) {
            String p1 = otherSignature.parameters.get(i);
            String p2 = parameters.get(i);

            if (!areParametersOfSameType(p1, p2)) {
               return false;
            }
         }

         return true;
      }

      private boolean areParametersOfSameType(String param1, String param2)
      {
         if (param1.equals(param2)) return true;
         if (param1.charAt(0) != 'T') return false;
         if (typeParametersToTypeArguments == null) return true;
         String typeArg1 = typeParametersToTypeArguments.get(param1);
         return param2.equals(typeArg1);
      }
   }

   AnnotatedMockMethods(Class<?> realClass, ParameterizedType mockedType)
   {
      this.realClass = realClass;
      methods = new ArrayList<MockMethod>();
      typeParametersToTypeArguments = new HashMap<String, String>();

      if (mockedType != null) {
         addMappingsFromTypeParametersToTypeArguments(realClass, mockedType);
      }

      addGenericTypeMappingsForSupertypes(realClass);
   }

   private void addGenericTypeMappingsForSupertypes(Class<?> realClass)
   {
      Type superType = realClass;

      while (superType instanceof Class<?> && superType != Object.class) {
         Class<?> superClass = (Class<?>) superType;
         superType = superClass.getGenericSuperclass();

         superType = addGenericTypeMappingsIfParameterized(superType);

         for (Type implementedInterface : superClass.getGenericInterfaces()) {
            addGenericTypeMappingsIfParameterized(implementedInterface);
         }
      }
   }

   private Type addGenericTypeMappingsIfParameterized(Type superType)
   {
      if (superType instanceof ParameterizedType) {
         ParameterizedType mockedSuperType = (ParameterizedType) superType;
         Type rawType = mockedSuperType.getRawType();
         addMappingsFromTypeParametersToTypeArguments((Class<?>) rawType, mockedSuperType);
         return rawType;
      }

      return superType;
   }

   private void addMappingsFromTypeParametersToTypeArguments(Class<?> mockedClass, ParameterizedType mockedType)
   {
      TypeVariable<?>[] typeParameters = mockedClass.getTypeParameters();
      Type[] typeArguments = mockedType.getActualTypeArguments();
      int n = typeParameters.length;

      for (int i = 0; i < n; i++) {
         Type typeArg = typeArguments[i];
         String typeArgName = null;
         String typeVarName = typeParameters[i].getName();

         if (typeArg instanceof Class<?>) {
            typeArgName = 'L' + ((Class<?>) typeArg).getName().replace('.', '/');
         }
         else if (typeArg instanceof TypeVariable<?>) {
            String intermediateTypeArg = 'T' + ((TypeVariable<?>) typeArg).getName();
            typeArgName = typeParametersToTypeArguments.get(intermediateTypeArg);
         }

         String mappedTypeArgName = typeArgName == null ? "Ljava/lang/Object" : typeArgName;
         typeParametersToTypeArguments.put('T' + typeVarName, mappedTypeArgName);
      }
   }

   MockMethod addMethod(boolean fromSuperClass, String name, String desc, boolean isStatic)
   {
      if (fromSuperClass && isMethodAlreadyAdded(name, desc)) {
         return null;
      }

      String nameAndDesc = name + desc;
      MockMethod mockMethod = new MockMethod(nameAndDesc, isStatic);
      methods.add(mockMethod);
      return mockMethod;
   }

   private boolean isMethodAlreadyAdded(String name, String desc)
   {
      int p = desc.lastIndexOf(')');
      String params = desc.substring(0, p + 1);

      for (MockMethod mockMethod : methods) {
         if (mockMethod.name.equals(name) && mockMethod.desc.startsWith(params)) {
            return true;
         }
      }

      return false;
   }

   void addMockState(MockState mockState)
   {
      if (mockStates == null) {
         mockStates = new ArrayList<MockState>(4);
      }

      mockState.mockMethod.indexForMockState = mockStates.size();
      mockStates.add(mockState);
   }

   /**
    * Verifies if a mock method with the same signature of a given real method was previously collected from the mock
    * class.
    * This operation can be performed only once for any given mock method in this container, so that after the last real
    * method is processed there should be no mock methods left unused in the container.
    */
   MockMethod containsMethod(String name, String desc, String signature)
   {
      for (MockMethod mockMethod : methods) {
         if (mockMethod.isMatch(name, desc, signature)) {
            return mockMethod;
         }
      }

      return null;
   }

   String getMockClassInternalName() { return mockClassInternalName; }
   void setMockClassInternalName(String mockClassInternalName) { this.mockClassInternalName = mockClassInternalName; }

   boolean isInnerMockClass() { return isInnerMockClass; }
   void setInnerMockClass(boolean innerMockClass) { isInnerMockClass = innerMockClass; }

   boolean supportsItField(Class<?> mockedClass) { return withItField && mockedClass == realClass; }
   void setWithItField(boolean withItField) { this.withItField = withItField; }

   boolean hasUnusedMocks()
   {
      for (MockMethod method : methods) {
         if (!method.hasMatchingRealMethod()) {
            return true;
         }
      }

      return false;
   }

   List<String> getUnusedMockSignatures()
   {
      List<String> signatures = new ArrayList<String>(methods.size());

      for (MockMethod mockMethod : methods) {
         if (!mockMethod.hasMatchingRealMethod()) {
            signatures.add(mockMethod.getMockNameAndDesc());
         }
      }

      return signatures;
   }

   void registerMockStates()
   {
      if (mockStates != null) {
         AnnotatedMockStates annotatedMockStates = TestRun.getMockClasses().getMockStates();
         annotatedMockStates.addMockClassAndStates(mockClassInternalName, mockStates);
      }
   }
}
