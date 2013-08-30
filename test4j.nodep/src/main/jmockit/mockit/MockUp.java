/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.reflect.*;

import mockit.internal.annotations.*;
import mockit.internal.startup.*;

/**
 * A <em>mock-up</em> for a class or interface, to be used in <em>state-based</em> unit tests or to provide a
 * <em>fake</em> implementation for use in integration tests.
 * <pre>
 *
 * // Define and apply a mock-up before exercising the code under test:
 * new MockUp&lt;SomeClass>() {
 *    &#64;Mock int someMethod(int i) { assertTrue(i > 0); return 123; }
 *    &#64;Mock(maxInvocations = 2) void anotherMethod(int i, String s) { &#47;* validate arguments *&#47; }
 * };
 * </pre>
 * One or more <em>mock methods</em> annotated {@linkplain Mock as such} must be defined in the concrete subclass.
 * Each such method should have a matching "real" method or constructor in the mocked class/interface.
 * At runtime, the execution of a mocked method/constructor will get redirected to the corresponding mock method.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html">In the Tutorial</a>
 *
 * @param <T> specifies the type (class, interface, etc.) to be mocked; multiple interfaces can be mocked by defining
 * a <em>type variable</em> in the test class or test method, and using it as the type argument;
 * if the type argument itself is a parameterized type, then only its raw type is considered for mocking
 */
public abstract class MockUp<T>
{
   static { Startup.verifyInitialization(); }
   private final T mockInstance;

   /**
    * Applies the {@linkplain Mock mock methods} defined in the concrete subclass to the class or interface specified
    * through the type parameter.
    * <p/>
    * When one or more interfaces are specified to be mocked, a mocked proxy class that implements the interfaces is
    * created, with the proxy instance made available through a call to {@link #getMockInstance()}.
    *
    * @throws IllegalArgumentException if no type to be mocked was specified;
    * or if there is a mock method for which no corresponding real method or constructor is found;
    * or if the real method matching a mock method is {@code abstract}
    *
    * @see #MockUp(Class)
    */
   protected MockUp()
   {
      Type typeToMock = getTypeToMock();

      if (typeToMock instanceof Class<?>) {
         //noinspection unchecked
         mockInstance = redefineClass((Class<T>) typeToMock);
      }
      else if (typeToMock instanceof ParameterizedType){
         mockInstance = redefineClass((ParameterizedType) typeToMock);
      }
      else { // a TypeVariable
         mockInstance = createMockInstanceForMultipleInterfaces(typeToMock);
      }
   }

   private Type getTypeToMock()
   {
      Class<?> currentClass = getClass();

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

   private T redefineClass(Class<T> classToMock)
   {
      if (classToMock.isInterface()) {
         return new MockedImplementationClass<T>(this).generate(classToMock, null);
      }

      redefineMethods(classToMock);
      return null;
   }

   private void redefineMethods(Class<T> realClass)
   {
      new MockClassSetup(realClass, null, this, null).redefineMethods();
   }

   private T redefineClass(ParameterizedType typeToMock)
   {
      @SuppressWarnings("unchecked")
      Class<T> realClass = (Class<T>) typeToMock.getRawType();

      if (realClass.isInterface()) {
         return new MockedImplementationClass<T>(this).generate(realClass, typeToMock);
      }

      new MockClassSetup(realClass, typeToMock, this, null).redefineMethods();
      return null;
   }

   @SuppressWarnings("unchecked")
   private T createMockInstanceForMultipleInterfaces(Type typeToMock)
   {
      T proxy = (T) Mockit.newEmptyProxy(typeToMock);
      redefineMethods((Class<T>) proxy.getClass());
      return proxy;
   }

   /**
    * Applies the {@linkplain Mock mock methods} defined in the concrete subclass to the given class/interface.
    * <p/>
    * In most cases, the constructor with no parameters can be used. This variation should be used only when the type
    * to be mocked is not accessible or known to the test.
    *
    * @see #MockUp()
    */
   protected MockUp(Class<?> classToMock)
   {
      //noinspection unchecked
      mockInstance = redefineClass((Class<T>) classToMock);
   }

   /**
    * Returns the mock instance created for the interface(s) to be mocked specified by the type parameter {@code T}, or
    * {@literal null} otherwise (ie, if a class was specified to be mocked).
    */
   public final T getMockInstance() { return mockInstance; }

   /**
    * Indicates whether the given class should also be mocked.
    * <p/>
    * If this method is overridden, all subclasses which extend the real class specified through this class's type
    * parameter {@code &lt;T>} will also be considered for mocking.
    * The same applies to classes implementing an specified real <em>interface</em>, when that's the case.
    * The overriding method will be called for each such subclass, to decide whether it should actually be mocked or
    * not.
    * <p/>
    * If not overridden, only the class/interface {@code &lt;T>} is actually mocked.
    *
    * @param loader the class loader defining the subclass
    * @param subclassName the fully qualified name of a class extending/implementing the class/interface that was
    *                     specified to be mocked through the type parameter {@code &lt;T>}
    *
    * @return {@code true} if the subclass should be mocked as well, {@code false} otherwise
    */
   protected boolean shouldBeMocked(ClassLoader loader, String subclassName) { return false; }
}
