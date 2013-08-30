/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal;

import mockit.external.asm4.*;

import static mockit.internal.util.Utilities.*;

/**
 * Allows the creation of new implementation classes for interfaces and abstract classes.
 */
public class ImplementationClass<T>
{
   private final Class<T> mockedType;
   private byte[] generatedBytecode;

   public ImplementationClass() { mockedType = null; }
   protected ImplementationClass(Class<T> mockedType) { this.mockedType = mockedType; }

   public final Class<T> generateNewMockImplementationClassForInterface()
   {
      ClassReader interfaceReader = ClassFile.createClassFileReader(mockedType);
      String mockClassName = getNameForGeneratedClass(mockedType, mockedType.getSimpleName());
      ClassVisitor modifier = createMethodBodyGenerator(interfaceReader, mockClassName);
      interfaceReader.accept(modifier, ClassReader.SKIP_DEBUG);

      return defineNewClass(mockedType.getClassLoader(), modifier, mockClassName);
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
