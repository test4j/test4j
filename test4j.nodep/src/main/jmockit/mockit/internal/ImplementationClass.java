/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal;

import java.lang.reflect.Type;

import mockit.external.asm4.*;
import mockit.internal.util.*;

/**
 * Allows the creation of new implementation classes for interfaces and abstract classes.
 */
public class ImplementationClass<T>
{
   private final Type mockedType;
   private byte[] generatedBytecode;

   public ImplementationClass() { mockedType = null; }
   protected ImplementationClass(Type mockedType) { this.mockedType = mockedType; }

   public final Class<T> generateNewMockImplementationClassForInterface()
   {
      Class<?> mockedClass = Utilities.getClassType(mockedType);
      ClassReader interfaceReader = ClassFile.createClassFileReader(mockedClass);
      String mockClassName = GeneratedClasses.getNameForGeneratedClass(mockedClass);
      ClassVisitor modifier = createMethodBodyGenerator(interfaceReader, mockClassName);
      interfaceReader.accept(modifier, ClassReader.SKIP_DEBUG);

      return defineNewClass(mockedClass.getClassLoader(), modifier, mockClassName);
   }

   protected ClassVisitor createMethodBodyGenerator(ClassReader typeReader, String className) { return null; }

   public final Class<T> defineNewClass(ClassLoader parentLoader, ClassVisitor generator, String className)
   {
      generatedBytecode = generator.toByteArray();

      if (parentLoader == null) {
         //noinspection AssignmentToMethodParameter
         parentLoader = ImplementationClass.class.getClassLoader();
      }

      return new ClassLoader(parentLoader) {
         @Override
         protected Class<T> findClass(String name)
         {
            //noinspection unchecked
            return (Class<T>) defineClass(name, generatedBytecode, 0, generatedBytecode.length);
         }
      }.findClass(className);
   }

   public final byte[] getGeneratedBytecode() { return generatedBytecode; }
}
