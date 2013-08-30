/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.annotations;

import java.lang.reflect.*;
import static java.lang.reflect.Modifier.*;

import mockit.external.asm4.*;
import mockit.internal.*;

import static mockit.internal.util.Utilities.*;

public final class MockedImplementationClass<T>
{
   private final Object mockInstance;

   public MockedImplementationClass(Object mockInstance) { this.mockInstance = mockInstance; }

   public MockedImplementationClass(Class<?> mockClass, Object mockInstance)
   {
      this(mockInstance == null ? newInstance(mockClass) : mockInstance);
   }

   public T generate(Class<T> interfaceToBeMocked, ParameterizedType typeToMock)
   {
      if (!isPublic(interfaceToBeMocked.getModifiers())) {
         T proxy = newEmptyProxy(interfaceToBeMocked.getClassLoader(), interfaceToBeMocked);
         new MockClassSetup(proxy.getClass(), null, mockInstance, null).redefineMethods();
         return proxy;
      }

      ImplementationClass<T> implementationClass = new ImplementationClass<T>(interfaceToBeMocked) {
         @Override
         protected ClassVisitor createMethodBodyGenerator(ClassReader typeReader, String className)
         {
            return new InterfaceImplementationGenerator(typeReader, className);
         }
      };

      Class<T> generatedClass = implementationClass.generateNewMockImplementationClassForInterface();
      byte[] generatedBytecode = implementationClass.getGeneratedBytecode();

      T proxy = newInstanceUsingDefaultConstructor(generatedClass);

      MockClassSetup setup = new MockClassSetup(generatedClass, typeToMock, mockInstance, generatedBytecode);
      setup.setBaseType(interfaceToBeMocked);
      setup.redefineMethods();

      return proxy;
   }
}
