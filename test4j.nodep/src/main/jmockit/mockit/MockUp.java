/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.reflect.*;
import java.util.*;

import mockit.internal.mockups.*;
import mockit.internal.startup.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

/**
 * A base class used in the creation of a <em>mock-up</em> for a class or interface.
 * Such mock-ups can be used in <em>state-based</em> unit tests or as <em>fake</em> implementations for use in
 * integration tests.
 * <pre>
 *
 * // Define and apply a mock-up before exercising the code under test:
 * new MockUp&lt;SomeClass>() {
 *    &#64;Mock int someMethod(int i) { assertTrue(i > 0); return 123; }
 *    &#64;Mock(maxInvocations = 2) void anotherMethod(int i, String s) { &#47;* validate arguments *&#47; }
 * };
 * </pre>
 * One or more <em>mock methods</em> annotated {@linkplain Mock as such} must be defined in the concrete subclass.
 * Each {@code @Mock} method should have a matching method or constructor in the mocked class/interface.
 * At runtime, the execution of a mocked method/constructor will get redirected to the corresponding mock method.
 * <p/>
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html">In the Tutorial</a>
 *
 * @param <T> specifies the type (class, interface, etc.) to be mocked; multiple interfaces can be mocked by defining
 * a <em>type variable</em> in the test class or test method, and using it as the type argument;
 * if the type argument itself is a parameterized type, then only its raw type is considered for mocking
 *
 * @see #MockUp()
 * @see #MockUp(Class)
 * @see #getMockInstance()
 * @see #tearDown()
 */
public abstract class MockUp<T>
{
   static { Startup.verifyInitialization(); }

   private Set<Class<?>> classesToRestore;
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
      validateMockingAllowed();
      tearDownPreviousMockUpIfSameMockClassAlreadyApplied();

      Type typeToMock = MockClassSetup.getTypeToMock(getClass());

      if (typeToMock instanceof Class<?>) {
         //noinspection unchecked
         mockInstance = redefineClassOrImplementInterface((Class<T>) typeToMock, null);
      }
      else if (typeToMock instanceof ParameterizedType){
         ParameterizedType parameterizedType = (ParameterizedType) typeToMock;
         //noinspection unchecked
         Class<T> realClass = (Class<T>) parameterizedType.getRawType();
         mockInstance = redefineClassOrImplementInterface(realClass, parameterizedType);
      }
      else { // a TypeVariable
         mockInstance = createMockInstanceForMultipleInterfaces(typeToMock);
      }
   }

   private void validateMockingAllowed()
   {
      if (TestRun.isInsideNoMockingZone()) {
         throw new IllegalStateException("Invalid place to apply a mock-up");
      }
   }

   private void tearDownPreviousMockUpIfSameMockClassAlreadyApplied()
   {
      Class<?> mockClass = getClass();
      MockUp<?> previousMock = TestRun.getMockClasses().findMock(mockClass);

      if (previousMock != null) {
         previousMock.tearDown();
      }
   }

   private T redefineClassOrImplementInterface(Class<T> classToMock, ParameterizedType typeToMock)
   {
      if (classToMock.isInterface()) {
         return new MockedImplementationClass<T>(this).generate(classToMock, typeToMock);
      }

      classesToRestore = redefineMethods(classToMock, typeToMock);
      return null;
   }

   private Set<Class<?>> redefineMethods(Class<T> realClass, ParameterizedType mockedType)
   {
      return Startup.initializing ? null : new MockClassSetup(realClass, mockedType, this, null).redefineMethods();
   }

   private T createMockInstanceForMultipleInterfaces(Type typeToMock)
   {
      T proxy = EmptyProxy.Impl.newEmptyProxy(null, typeToMock);
      //noinspection unchecked
      redefineMethods((Class<T>) proxy.getClass(), null);
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
   @SuppressWarnings("unchecked")
   protected MockUp(Class<?> classToMock)
   {
      validateMockingAllowed();
      tearDownPreviousMockUpIfSameMockClassAlreadyApplied();

      if (classToMock.isInterface()) {
         mockInstance = new MockedImplementationClass<T>(this).generate((Class<T>) classToMock, null);
      }
      else {
         classesToRestore = redefineMethods((Class<T>) classToMock, null);
         mockInstance = null;
      }
   }

   /**
    * Returns the mock instance created for the mocked interface(s), or {@literal null} if a class was specified to be
    * mocked instead.
    * This mock instance belongs to a dynamically generated class which implements the mocked interface(s).
    * <p/>
    * For a given mock-up instance, this method always returns the same mock instance.
    * <p/>
    * All methods in the generated implementation class are empty, with non-void methods returning a default value
    * according to the return type: {@literal 0} for {@code int}, {@literal null} for a reference type, and so on.
    * <p/>
    * The {@code equals}, {@code hashCode}, and {@code toString} methods inherited from {@code java.lang.Object} are
    * overridden with an appropriate implementation in each case:
    * {@code equals} is implemented by comparing the two object references (the mock instance and the method argument)
    * for equality; {@code hashCode} is implemented to return the identity hash code for the mock instance; and
    * {@code toString} returns the standard string representation that {@code Object#toString} would have returned.
    */
   public final T getMockInstance() { return mockInstance; }

   /**
    * Discards the mock methods originally set up by instantiating this mock-up object, restoring mocked methods to
    * their original behaviors.
    * <p/>
    * JMockit will automatically restore classes mocked by a test at the end of its execution, as well as classes
    * mocked for the whole test class before the first test in the next test class is executed.
    * Therefore, this method should rarely be used, if ever.
    */
   public final void tearDown()
   {
      if (classesToRestore != null) {
         TestRun.mockFixture().restoreAndRemoveRedefinedClasses(classesToRestore);
         TestRun.getMockClasses().removeMock(this);
         classesToRestore = null;
      }
   }
}
