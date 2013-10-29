/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.instrument.*;
import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.*;

import static java.lang.reflect.Modifier.*;

import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.expectations.mocking.InstanceFactory.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

abstract class BaseTypeRedefinition
{
   private static final class MockedClass
   {
      final InstanceFactory instanceFactory;
      final ClassDefinition[] mockedClassDefinitions;

      MockedClass(InstanceFactory instanceFactory, ClassDefinition[] classDefinitions)
      {
         this.instanceFactory = instanceFactory;
         mockedClassDefinitions = classDefinitions;
      }

      void redefineClasses()
      {
         RedefinitionEngine.redefineClasses(mockedClassDefinitions);
      }
   }

   private static final Map<Integer, MockedClass> mockedClasses = new HashMap<Integer, MockedClass>();
   private static final Map<Type, Class<?>> mockInterfaces = new HashMap<Type, Class<?>>();

   Class<?> targetClass;
   MockedType typeMetadata;
   InstanceFactory instanceFactory;
   private List<ClassDefinition> mockedClassDefinitions;

   BaseTypeRedefinition(Class<?> mockedType) { targetClass = mockedType; }

   final InstanceFactory redefineType(Type typeToMock)
   {
      if (targetClass == null || targetClass.isInterface()) {
         createMockedInterfaceImplementationAndInstanceFactory(typeToMock);
      }
      else {
         redefineTargetClassAndCreateInstanceFactory(typeToMock);
      }

      TestRun.mockFixture().registerInstanceFactoryForMockedType(targetClass, instanceFactory);
      return instanceFactory;
   }

   private void createMockedInterfaceImplementationAndInstanceFactory(Type interfaceToMock)
   {
      Class<?> mockedInterface = interfaceToMock(interfaceToMock);

      if (mockedInterface == null) {
         createMockInterfaceImplementationUsingStandardProxy(interfaceToMock);
         return;
      }

      Class<?> mockClass = mockInterfaces.get(interfaceToMock);

      if (mockClass != null) {
         targetClass = mockClass;
         createNewMockInstanceFactoryForInterface();
         return;
      }

      generateNewMockImplementationClassForInterface(interfaceToMock);
      createNewMockInstanceFactoryForInterface();

      mockInterfaces.put(interfaceToMock, targetClass);
   }

   private Class<?> interfaceToMock(Type typeToMock)
   {
      if (typeToMock instanceof Class<?>) {
         Class<?> theInterface = (Class<?>) typeToMock;

         if (isPublic(theInterface.getModifiers()) && !theInterface.isAnnotation()) {
            return theInterface;
         }
      }
      else if (typeToMock instanceof ParameterizedType) {
         return interfaceToMock(((ParameterizedType) typeToMock).getRawType());
      }

      return null;
   }

   private void createMockInterfaceImplementationUsingStandardProxy(Type typeToMock)
   {
      Object mock = EmptyProxy.Impl.newEmptyProxy(getClass().getClassLoader(), typeToMock);
      targetClass = mock.getClass();

      redefineMethodsAndConstructorsInTargetType();

      instanceFactory = new InterfaceInstanceFactory(mock);
   }

   private void createNewMockInstanceFactoryForInterface()
   {
      Object mock = ConstructorReflection.newInstanceUsingDefaultConstructor(targetClass);
      instanceFactory = new InterfaceInstanceFactory(mock);
   }

   private void generateNewMockImplementationClassForInterface(final Type interfaceToMock)
   {
      targetClass = new ImplementationClass(interfaceToMock) {
         @Override
         protected ClassVisitor createMethodBodyGenerator(ClassReader typeReader, String className)
         {
            return new InterfaceImplementationGenerator(typeReader, interfaceToMock, className);
         }
      }.generateNewMockImplementationClassForInterface();
   }

   final void redefineMethodsAndConstructorsInTargetType()
   {
      redefineClassAndItsSuperClasses(targetClass, false);
   }

   private void redefineClassAndItsSuperClasses(Class<?> realClass, boolean isSuperClass)
   {
      ClassReader classReader = createClassReader(realClass);
      ExpectationsModifier modifier = createModifier(realClass, classReader);

      if (isSuperClass) {
         modifier.useDynamicMockingForSuperClass();
      }

      try {
         redefineClass(realClass, classReader, modifier);
      }
      catch (VisitInterruptedException ignore) {
         // As defined in ExpectationsModifier, some critical JRE classes have all methods excluded from mocking by
         // default. This exception occurs when they are visited.
         // In this case, we simply stop class redefinition for the rest of the class hierarchy.
         return;
      }

      Class<?> superClass = realClass.getSuperclass();

      if (superClass != null && superClass != Object.class && superClass != Proxy.class && superClass != Enum.class) {
         redefineClassAndItsSuperClasses(superClass, true);
      }
   }

   abstract ExpectationsModifier createModifier(Class<?> realClass, ClassReader classReader);

   private void redefineClass(Class<?> realClass, ClassReader classReader, ClassVisitor modifier)
   {
      classReader.accept(modifier, 0);
      byte[] modifiedClass = modifier.toByteArray();

      ClassDefinition classDefinition = new ClassDefinition(realClass, modifiedClass);
      RedefinitionEngine.redefineClasses(classDefinition);

      if (mockedClassDefinitions != null) {
         mockedClassDefinitions.add(classDefinition);
      }
   }

   private ClassReader createClassReader(Class<?> realClass)
   {
      return ClassFile.createReaderOrGetFromCache(realClass);
   }

   private void redefineTargetClassAndCreateInstanceFactory(Type typeToMock)
   {
      Integer mockedClassId = redefineClassesFromCache();

      if (mockedClassId == null) {
         return;
      }

      if (targetClass.isEnum()) {
         instanceFactory = new EnumInstanceFactory(targetClass);
         redefineMethodsAndConstructorsInTargetType();
      }
      else if (isAbstract(targetClass.getModifiers())) {
         redefineMethodsAndConstructorsInTargetType();
         Class<?> subclass = generateConcreteSubclassForAbstractType(typeToMock);
         instanceFactory = new ClassInstanceFactory(subclass);
      }
      else {
         redefineMethodsAndConstructorsInTargetType();
         instanceFactory = new ClassInstanceFactory(targetClass);
      }

      storeRedefinedClassesInCache(mockedClassId);
   }

   final Integer redefineClassesFromCache()
   {
      Integer mockedClassId = typeMetadata != null ? typeMetadata.hashCode() : targetClass.hashCode();
      MockedClass mockedClass = mockedClasses.get(mockedClassId);

      if (mockedClass != null) {
         mockedClass.redefineClasses();
         instanceFactory = mockedClass.instanceFactory;
         return null;
      }

      mockedClassDefinitions = new ArrayList<ClassDefinition>();
      return mockedClassId;
   }

   final void storeRedefinedClassesInCache(Integer mockedClassId)
   {
      ClassDefinition[] classDefs = mockedClassDefinitions.toArray(new ClassDefinition[mockedClassDefinitions.size()]);
      MockedClass mockedClass = new MockedClass(instanceFactory, classDefs);

      mockedClasses.put(mockedClassId, mockedClass);
   }

   private Class<?> generateConcreteSubclassForAbstractType(Type typeToMock)
   {
      String subclassName = getNameForConcreteSubclassToCreate();

      ClassReader classReader = createClassReader(targetClass);
      SubclassGenerationModifier modifier =
         new SubclassGenerationModifier(typeMetadata.mockingCfg, typeToMock, classReader, subclassName);
      classReader.accept(modifier, 0);

      return new ImplementationClass().defineNewClass(targetClass.getClassLoader(), modifier, subclassName);
   }

   abstract String getNameForConcreteSubclassToCreate();
}