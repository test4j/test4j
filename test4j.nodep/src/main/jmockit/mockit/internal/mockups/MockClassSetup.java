/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.mockups;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.*;

import mockit.*;
import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.startup.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

public final class MockClassSetup
{
   private Class<?> realClass;
   private ClassReader rcReader;
   private final MockMethods mockMethods;
   private final MockUp<?> mock;
   private final boolean forStartupMock;

   public MockClassSetup(Class<? extends MockUp<?>> mockClass)
   {
      mock = ConstructorReflection.newInstance(mockClass);

      Type typeToMock = getTypeToMock(mockClass);
      ParameterizedType mockedType;

      if (typeToMock instanceof Class<?>) {
         mockedType = null;
         realClass = (Class<?>) typeToMock;
      }
      else if (typeToMock instanceof ParameterizedType){
         mockedType = (ParameterizedType) typeToMock;
         realClass = (Class<?>) mockedType.getRawType();
      }
      else {
         throw new IllegalArgumentException(
            "Invalid target type specified in mock-up " + mockClass + ": " + typeToMock);
      }

      mockMethods = new MockMethods(realClass, mockedType);
      forStartupMock = true;

      new MockMethodCollector(mockMethods).collectMockMethods(mockClass);
   }

   public static Type getTypeToMock(Class<?> mockUpClass)
   {
      Class<?> currentClass = mockUpClass;

      do {
         Type superclass = currentClass.getGenericSuperclass();

         if (superclass instanceof ParameterizedType) {
            return ((ParameterizedType) superclass).getActualTypeArguments()[0];
         }
         else if (superclass == MockUp.class) {
            throw new IllegalArgumentException("No type to be mocked");
         }

         currentClass = (Class<?>) superclass;
      }
      while (true);
   }

   public MockClassSetup(Class<?> realClass, ParameterizedType mockedType, MockUp<?> mockUp, byte[] realClassCode)
   {
      this.realClass = realClass;
      mockMethods = new MockMethods(realClass, mockedType);
      mock = mockUp;
      forStartupMock = false;
      rcReader = realClassCode == null ? null : new ClassReader(realClassCode);

      new MockMethodCollector(mockMethods).collectMockMethods(mockUp.getClass());
   }

   public Set<Class<?>> redefineMethods()
   {
      Set<Class<?>> redefinedClasses = redefineMethodsInClassHierarchy();
      validateThatAllMockMethodsWereApplied();
      return redefinedClasses;
   }

   private Set<Class<?>> redefineMethodsInClassHierarchy()
   {
      Set<Class<?>> redefinedClasses = new HashSet<Class<?>>();

      while (realClass != null && mockMethods.hasUnusedMocks()) {
         byte[] modifiedClassFile = modifyRealClass();

         if (modifiedClassFile != null) {
            applyClassModifications(modifiedClassFile);
            redefinedClasses.add(realClass);
         }

         Class<?> superClass = realClass.getSuperclass();
         realClass = superClass == Object.class || superClass == Proxy.class ? null : superClass;
         rcReader = null;
      }

      return redefinedClasses;
   }

   private byte[] modifyRealClass()
   {
      if (rcReader == null) {
         rcReader = createClassReaderForRealClass();
      }

      MockupsModifier modifier = new MockupsModifier(rcReader, realClass, mock, mockMethods, forStartupMock);
      rcReader.accept(modifier, 0);

      return modifier.wasModified() ? modifier.toByteArray() : null;
   }

   private ClassReader createClassReaderForRealClass()
   {
      if (realClass.isInterface() || realClass.isArray()) {
         throw new IllegalArgumentException("Not a modifiable class: " + realClass.getName());
      }

      return ClassFile.createReaderFromLastRedefinitionIfAny(realClass);
   }

   private void applyClassModifications(byte[] modifiedClassFile)
   {
      Startup.redefineMethods(realClass, modifiedClassFile);
      mockMethods.registerMockStates(forStartupMock);

      if (forStartupMock) {
         CachedClassfiles.addClassfile(realClass, modifiedClassFile);
      }
      else {
         TestRun.mockFixture().addRedefinedClass(mockMethods.getMockClassInternalName(), realClass, modifiedClassFile);
      }
   }

   private void validateThatAllMockMethodsWereApplied()
   {
      List<String> remainingMocks = mockMethods.getUnusedMockSignatures();

      if (!remainingMocks.isEmpty()) {
         String classDesc = mockMethods.getMockClassInternalName();
         String mockSignatures = new MethodFormatter(classDesc).friendlyMethodSignatures(remainingMocks);

         throw new IllegalArgumentException(
            "Matching real methods not found for the following mocks:\n" + mockSignatures);
      }
   }
}
