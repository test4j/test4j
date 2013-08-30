/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.reflect.*;
import java.util.*;

import mockit.internal.annotations.*;
import mockit.internal.startup.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

/**
 * Provides static methods for the mocking and stubbing of arbitrary classes, according to specified <em>mock
 * classes</em> defined in test code.
 * Such methods are intended to be called from test code only.
 * <p/>
 * Once mocked, a "real" method defined in a production class will behave (during test execution) as if its
 * implementation was replaced by a call to the corresponding <em>mock method</em> in the mock class.
 * Whatever value this mock method returns will be the value returned by the call to the mocked method.
 * The mock method can also throw an exception or error, which will then be propagated to the caller of the mocked
 * "real" method.
 * Therefore, while mocked the original code in the real method is never executed (actually, there's still a way to
 * execute the real implementation, although not normally used for testing purposes).
 * The same basic rules apply to constructors, which can be mocked by mock methods with the special name "$init".
 * <p/>
 * The methods in this class can be divided in the following groups:
 * <ul>
 * <li>
 * <strong>Stubbing API</strong>: {@link #stubOut(Class...)}, {@link #stubOutClass(Class, String...)}, and
 * {@link #stubOutClass(Class, boolean, String...)}.
 * </li>
 * <li>
 * <strong>Mockups API</strong> for state-oriented mocking: {@link MockUp}, {@link #setUpMocks(Object...)},
 * {@link #setUpMock(Class, Class)} and its several overloads, {@link #setUpStartupMocks(Object...)},
 * {@link #setUpMocksAndStubs(Class...)}, and {@link #tearDownMocks(Class...)} / {@link #tearDownMocks()}.
 * </li>
 * <li>
 * <strong>Proxy-based utilities</strong>:
 * {@link #newEmptyProxy(ClassLoader, Class)} and its overloads.
 * These are merely convenience methods that create empty implementation classes for one or more interfaces, where all
 * implemented methods do nothing beyond returning a default value according to the return type of each interface
 * method.
 * The created classes can be mocked through the Mockups API, and its instances passed to code under test.
 * </li>
 * </ul>
 * In the Tutorial:
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html">Writing state-based tests</a>,
 * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/ReflectionUtilities.html">Reflection-based utilities</a>
 */
public final class Mockit
{
   static { Startup.verifyInitialization(); }
   private Mockit() {}

   /**
    * Stubs out all methods, constructors, and static initializers in the given classes, so that they do nothing
    * whenever executed.
    * <p/>
    * Note that any stubbed out constructor will still call a constructor in the super-class, which in turn will be
    * executed normally unless also stubbed out.
    * The super-constructor to be called is chosen arbitrarily.
    * The classes are stubbed out in the order they are given, so make sure any super-class comes first.
    * <p/>
    * Methods with non-<code>void</code> return type will return the default value for this type, that is, zero for a
    * number or {@code char}, {@literal false} for a boolean, empty for an array, or {@literal null} for a reference
    * type.
    * <p/>
    * If a different behavior is desired for any method or constructor, then {@link #setUpMocks(Object...)} and the
    * other similar methods can be used right after the call to this method.
    * They will override any stub previously created with the corresponding mock implementation, if any.
    *
    * @param realClasses one or more regular classes to be stubbed out
    *
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/samples/powermock/test/powermock/examples/suppress/constructor/ExampleWithEvilChild_JMockit_Test.java#17">
    * Example</a>
    * <p/>
    * In the Tutorial:
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html#stubbingMethods">Using the
    * stubbing methods</a>
    */
   public static void stubOut(Class<?>... realClasses)
   {
      for (Class<?> realClass : realClasses) {
         new ClassStubbing(realClass).stubOut();
      }
   }

   /**
    * Same as {@link #stubOut(Class...)} for the given class, except that only the specified class members (if any) are
    * stubbed out, leaving the rest unaffected.
    * Such class members include the methods and constructors defined by the class, plus any static or instance
    * initialization blocks it might have.
    * Note that if <em>no</em> filters are specified the whole class will be stubbed out.
    * <p/>
    * For methods, the filters are {@linkplain java.util.regex.Pattern regular expressions} for method names, optionally
    * followed by parameter type names between parentheses. For constructors, only the parameters are specified.
    * For more details about the syntax for mock filters, see the {@link MockClass#stubs} annotation attribute.
    * <p/>
    * The special filter "&lt;clinit>" will match all static initializers in the given class.
    * <p/>
    * To stub out instance field initializers it is necessary to actually specify all constructors in the class, because
    * such initialization assignments are copied to each and every constructor by the Java compiler.
    *
    * @param realClass a regular class to be stubbed out
    * @param filters one or more filters that specify which class members (methods, constructors, and/or static
    * initialization blocks) to be stubbed out
    *
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/samples/powermock/test/powermock/examples/suppress/method/ExampleWithEvilMethod_JMockit_Test.java#20">
    * Example</a>
    * <p/>
    * In the Tutorial:
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html#stubbingMethods">Using the
    * stubbing methods</a>
    */
   public static void stubOutClass(Class<?> realClass, String... filters)
   {
      new ClassStubbing(realClass, true, filters).stubOut();
   }

   /**
    * The same as {@link #stubOutClass(Class, String...)}, but specifying whether filters are to be inverted or not.
    *
    * @param inverse indicates whether the mock filters are to be inverted or not; if inverted, only the methods and
    * constructors matching them are <strong>not</strong> mocked
    */
   public static void stubOutClass(Class<?> realClass, boolean inverse, String... filters)
   {
      new ClassStubbing(realClass, !inverse, filters).stubOut();
   }

   /**
    * Same as {@link #stubOutClass(Class, String...)}, but accepting the (fully qualified) name of the real class.
    * This is useful when said class is not accessible from the test.
    */
   public static void stubOutClass(String realClassName, String... filters)
   {
      Class<?> realClass = Utilities.loadClass(realClassName);
      new ClassStubbing(realClass, true, filters).stubOut();
   }

   /**
    * Same as {@link #stubOutClass(Class, boolean, String...)}, but accepting the (fully qualified) name of the real
    * class. This is useful when said class is not accessible from the test.
    */
   public static void stubOutClass(String realClassName, boolean inverse, String... filters)
   {
      Class<?> realClass = Utilities.loadClass(realClassName);
      new ClassStubbing(realClass, !inverse, filters).stubOut();
   }

   /**
    * Given a mix of {@linkplain MockClass mock} and real classes, {@linkplain #setUpMock(Class, Class) sets up} each
    * mock class for the associated real class, and {@linkplain #stubOut stubs out} each specified regular class.
    *
    * @param mockAndRealClasses one or more mock classes and/or regular classes to be stubbed out
    */
   public static void setUpMocksAndStubs(Class<?>... mockAndRealClasses)
   {
      for (Class<?> mockOrRealClass : mockAndRealClasses) {
         MockClass metadata = mockOrRealClass.getAnnotation(MockClass.class);

         if (metadata != null) {
            new MockClassSetup(mockOrRealClass, metadata).redefineMethods();
         }
         else {
            new ClassStubbing(mockOrRealClass).stubOut();
         }
      }
   }

   /**
    * Sets up the mocks defined in one or more {@linkplain MockClass mock classes}.
    * <p/>
    * After this call, all such mocks are "in effect" until the end of the test method inside which it appears, if this
    * is the case.
    * If the method is a "before"/"setUp" method which executes before all test methods, then the mocks will remain in
    * effect until the end of the test (including any "after"/"tearDown" methods).
    * <p/>
    * Any invocation count constraints specified on mock methods (such as {@code @Mock(invocations = 1)}, for example)
    * will be automatically verified after the code under test is executed.
    * <p/>
    * For each call made during test execution to a <em>mocked</em> method, the corresponding <em>mock</em> method is
    * called instead.
    * A mock method must have the same signature (ie, name and parameters) as the corresponding mocked/real method.
    * The return type of the mock method must be the same exact type <em>or</em> a compatible one.
    * The {@code throws} clause may differ in any way.
    * Note also that the mock method can be static or not, independently of the real method being static or not.
    * <p/>
    * A constructor in the real class can be mocked by a corresponding mock method of name {@code $init}, declared
    * with the same parameters and with {@code void} return type.
    * It will be called for each new instance of the real class that is created through a call to that constructor, with
    * whatever arguments are passed to it.
    * <p/>
    * <strong>Class initializers</strong> of the real class (one or more {@code static} initialization blocks plus all
    * assignments to {@code static} fields) can be mocked by providing a mock method named {@code $clinit} in the mock
    * class. This method should return {@code void} and have no declared parameters.
    * It will be called at most once, at the time the real class is initialized by the JVM (and since all static
    * initializers for that class are mocked, the initialization will have no effect).
    * <p/>
    * Mock methods can gain access to the instance of the real class on which the corresponding real method or
    * constructor was called. This requires the mock class to define an instance field of name <strong>"it"</strong>,
    * the same type as the real class, and accessible from that class (in general, this means the field will have to be
    * {@code public}). Such a field will always be set to the appropriate real class instance, whenever a mock method is
    * called. Note that through this field the mock class will be able to call any accessible instance method on the
    * real class, including the real method corresponding to the current mock method. In this case, however, such calls
    * are not allowed by default because they lead to infinite recursion, with the mock calling itself indirectly
    * through the redefined real method. If the real method needs to be called from the mock method, then the latter
    * must be declared as {@linkplain mockit.Mock#reentrant reentrant}.
    *
    * @param mockClassesOrInstances one or more classes ({@code Class} objects) or instances of classes which define
    * arbitrary methods and/or constructors, where the ones annotated as {@linkplain Mock mocks} will be used to
    * redefine corresponding real methods/constructors in a designated {@linkplain MockClass#realClass() real class}
    * (usually, a class on which the code under test depends on)
    *
    * @throws IllegalArgumentException if a given mock class fails to specify the corresponding real class using the
    * {@code @MockClass(realClass = ...)} annotation; or if a mock class defines a mock method for which no
    * corresponding real method or constructor exists in the real class;
    * or if the real method matching a mock method is {@code abstract}
    *
    * @see #tearDownMocks(Class[])
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/samples/orderMngmntWebapp/test/orderMngr/domain/order/OrderRepository_MockupsAPI_Test.java#111">
    * Example</a>
    * <p/>
    * In the Tutorial:
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html#MockClass">Using the
    * <code>@MockClass</code> annotation</a>
    */
   public static void setUpMocks(Object... mockClassesOrInstances)
   {
      for (Object mockClassOrInstance : mockClassesOrInstances) {
         Class<?> mockClass;
         Object mock;

         if (mockClassOrInstance instanceof Class<?>) {
            mockClass = (Class<?>) mockClassOrInstance;
            mock = null;
         }
         else {
            mockClass = mockClassOrInstance.getClass();
            mock = mockClassOrInstance;
         }

         new MockClassSetup(mock, mockClass).redefineMethods();
      }
   }

   /**
    * Sets up the <em>startup</em> {@linkplain Mock mocks} defined in one or more mock classes, just like
    * {@link #setUpMocks(Object...)} does for regular mock classes.
    * The difference is in the lifetime of the mocks, which will last to the end of the test run.
    * Consequently, this method should only be called once, before the first test begins execution.
    * One way to achieve this is to put the call in the static initializer of a common base class extended by all test
    * classes in the suite. Another way is by configuring what happens at startup through external means.
    * <p/>
    * There are three ways to set up mock classes at startup time:
    * <ol>
    * <li>Define a value for the "<code>jmockit-mocks</code>" system property, as a comma-separated list of fully
    * qualified class names.</li>
    * <li>Add a custom "<code>jmockit.properties</code>" file to the classpath, with an entry for the
    * "<code>jmockit-mocks</code>" (or just "<code>mocks</code>") property.
    * </li>
    * <li>Specify the "<code>-javaagent:jmockit.jar=&lt;agentArgs></code>" JVM argument, with "<code>agentArgs</code>"
    * containing one or more mock class names, separated by semicolons if more than one.
    * </li>
    * </ol>
    * Note that option two above makes it possible to package a whole set of reusable mock classes in a jar file,
    * provided it contains a suitable <code>jmockit.properties</code> file.
    * By simply adding the jar to the classpath <em>before</em> <code>jmockit.jar</code>, the specified mock classes
    * will be loaded and applied automatically on every test run, as soon as JMockit itself gets initialized.
    *
    * @param mockClassesOrInstances one or more mock classes (either <code>Class</code> literals or fully qualified
    * class names) or instances of mock classes
    *
    * @throws IllegalArgumentException if a given mock class fails to specify the corresponding real class using the
    * {@code @MockClass(realClass = ...)} annotation; or if a mock class defines a mock method for which no
    * corresponding real method or constructor exists in the real class;
    * or if the real method matching a mock method is {@code abstract}
    *
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/main/test/mockit/MockAnnotationsTest.java#465">
    * Example</a>
    * <p/>
    * In the Tutorial:
    * <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/UsingMocksAndStubs.html">Using mocks and stubs over
    * entire test classes and suites</a>
    */
   public static void setUpStartupMocks(Object... mockClassesOrInstances)
   {
      for (Object mockClassOrInstance : mockClassesOrInstances) {
         Class<?> mockClass;
         Object mock;

         if (mockClassOrInstance instanceof Class<?>) {
            mockClass = (Class<?>) mockClassOrInstance;
            mock = null;
         }
         else if (mockClassOrInstance instanceof String) {
            String className = ((String) mockClassOrInstance).trim();
            if (className.length() == 0) continue;
            mockClass = Utilities.loadClass(className);
            mock = null;
         }
         else {
            mockClass = mockClassOrInstance.getClass();
            mock = mockClassOrInstance;
         }

         new MockClassSetup(mock, mockClass).setUpStartupMock();
      }
   }

   /**
    * Similar to {@link #setUpMocks(Object...)}, but accepting a single mock and its corresponding real class.
    * <p/>
    * Useful when the real class is not known in advance, such as when it is determined at runtime through configuration
    * of by creating a {@link Proxy} for an interface.
    *
    * @param realClass the class to be mocked that is used by code under test
    * @param mock an instance of the class containing the mock methods for the real class
    *
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/samples/orderMngmntWebapp/test/orderMngr/domain/order/OrderRepository_MockupsAPI_Test.java#113">
    * Example</a>
    */
   public static void setUpMock(Class<?> realClass, Object mock)
   {
      Class<?> mockClass = mock.getClass();
      new MockClassSetup(realClass, mock, mockClass).redefineMethods();
   }

   /**
    * Same as {@link #setUpMock(Class, Object)}, but accepting the (fully qualified) name of the real class.
    * This is useful when said class is not accessible from the test.
    */
   public static void setUpMock(String realClassName, Object mock)
   {
      Class<?> realClass = Utilities.loadClass(realClassName);
      setUpMock(realClass, mock);
   }

   /**
    * Similar to {@link #setUpMocks(Object...)}, but accepting a single mock class and its corresponding real class.
    * <p/>
    * Can also be useful when the real class is not known in advance, such as when it is determined at runtime through
    * configuration or by creating a {@link Proxy} for an interface.
    *
    * @param realClass the class to be mocked that is used by code under test
    * @param mockClass the class containing the mock methods for the real class
    *
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/main/test/mockit/MockAnnotationsTest.java#169">
    * Example</a>
    */
   public static void setUpMock(Class<?> realClass, Class<?> mockClass)
   {
      new MockClassSetup(realClass, null, mockClass).redefineMethods();
   }

   /**
    * Same as {@link #setUpMock(Class, Class)}, but accepting the (fully qualified) name of the real class.
    * This is useful when said class is not accessible from the test.
    *
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/main/test/integrationTests/textFile/TextFileUsingAnnotatedMockClassesTest.java#40">
    * Example</a>
    */
   public static void setUpMock(String realClassName, Class<?> mockClass)
   {
      Class<?> realClass = Utilities.loadClass(realClassName);
      setUpMock(realClass, mockClass);
   }

   /**
    * Sets up the mocks defined in the given mock class.
    * <p/>
    * If the type {@linkplain MockClass#realClass referred to} by the mock class is actually an interface, then a new
    * empty implementation class is created.
    *
    * @param mockClassOrInstance the mock class itself (given by its {@code Class} literal), or an instance of the mock
    * class
    *
    * @return a new instance of the implementation class created for the mocked interface, or {@code null} otherwise
    *
    * @throws IllegalArgumentException if a given mock class fails to specify the corresponding real class using the
    * {@code @MockClass(realClass = ...)} annotation; or if a mock class defines a mock method for which no
    * corresponding real method or constructor exists in the real class;
    * or if the real method matching a mock method is {@code abstract}
    *
    * @see #setUpMock(Class, Object)
    * @see #setUpMocks(Object...)
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/main/test/mockit/MockAnnotationsTest.java#696">
    * Example</a>
    */
   public static <T> T setUpMock(Object mockClassOrInstance)
   {
      Class<?> mockClass;
      Object mock;

      if (mockClassOrInstance instanceof Class<?>) {
         mockClass = (Class<?>) mockClassOrInstance;
         mock = null;
      }
      else {
         mockClass = mockClassOrInstance.getClass();
         mock = mockClassOrInstance;
      }

      Class<T> realClass = MockClassSetup.getRealClass(mockClass);

      if (realClass.isInterface()) {
         return new MockedImplementationClass<T>(mockClass, mock).generate(realClass, null);
      }

      new MockClassSetup(realClass, mock, mockClass).redefineMethods();
      return null;
   }

   /**
    * Discards any mocks currently in effect, for all test scopes: the current test method (if any), the current test
    * (which starts with the first "before" method and continues until the last "after" method), the current test class
    * (which includes all code from the first "before class" method to the last "after class" method), and the current
    * test suite.
    * <p/>
    * Notice that a call to this method will tear down <em>all</em> mock classes that were applied through use of the
    * Mockups API that are still in effect, as well as any mock classes or stubs applied to the current test class
    * through {@code @UsingMocksAndStubs}.
    * In other words, it would effectively prevent mocks to be set up at the test class and test suite levels.
    * So, use it only if necessary and if it won't discard mock classes that should remain in effect.
    * Consider using {@link #tearDownMocks(Class...)} instead, which lets you restrict the set of real classes to be
    * restored.
    * <p/>
    * JMockit will automatically restore classes mocked by a test at the end of its execution, as well as all classes
    * mocked for the test class as a whole (through a "before class" method or an {@code @UsingMocksAndStubs}
    * annotation) before the first test in the next test class is executed.
    *
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/main/test/mockit/MockAnnotationsTest.java#450">
    * Example</a>
    */
   public static void tearDownMocks()
   {
      TestRun.mockFixture().restoreAndRemoveRedefinedClasses(null);
      TestRun.getMockClasses().getRegularMocks().discardInstances();
   }

   /**
    * Discards any mocks set up for the specified classes that are currently in effect, for all test scopes: the current
    * test method (if any), the current test (which starts with the first "before" method and continues until the last
    * "after" method), the current test class (which includes all code from the first "before class" method to the last
    * "after class" method), and the current test suite.
    * <p/>
    * Notice that if one of the given real classes has a mock class applied at the level of the test class, calling this
    * method would negate the application of that mock class.
    * JMockit will automatically restore classes mocked by a test at the end of its execution, as well as all classes
    * mocked for the test class as a whole (through a "before class" method or an {@code @UsingMocksAndStubs}
    * annotation) before the first test in the next test class is executed.
    *
    * @param realClasses one or more real classes from production code, which may have mocked methods
    */
   public static void tearDownMocks(Class<?>... realClasses)
   {
      Set<Class<?>> classesToRestore = new HashSet<Class<?>>();
      Collections.addAll(classesToRestore, realClasses);
      TestRun.mockFixture().restoreAndRemoveRedefinedClasses(classesToRestore);
   }

   /**
    * Same as {@link #newEmptyProxy(ClassLoader, Class)}, but with the class loader obtained from the interface to be
    * proxied. Note that this may lead to a {@code NoClassDefFoundError} if that interface was loaded by the boot class
    * loader (usually, when it's a JRE class).
    * Therefore, you should only use this method for application-defined interfaces.
    * <p/>
    * This method is just a convenience for some uses of the <em>Mockups</em> API.
    * In <em>JMockit Expectations</em> in particular, mocked instances will be automatically created and assigned to any
    * mock fields or parameters.
    *
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/main/test/integrationTests/textFile/TextFileUsingAnnotatedMockClassesTest.java#91">
    * Example</a>
    */
   public static <E> E newEmptyProxy(Class<E> interfaceToBeProxied)
   {
      return newEmptyProxy(interfaceToBeProxied.getClassLoader(), interfaceToBeProxied);
   }

   /**
    * Creates a {@link Proxy} implementation for a given interface, in which all methods are empty.
    * Non-void methods will return a default value according to the return type: {@literal 0} for {@code int},
    * {@literal null} for a reference type, and so on.
    * <p/>
    * The {@code equals}, {@code hashCode}, and {@code toString} methods inherited from {@code java.lang.Object} are
    * overridden with an appropriate implementation in each case:
    * {@code equals} is implemented by comparing the two object references (the proxy instance and the method argument)
    * for equality; {@code hashCode} is implemented to return the identity hash code for the proxy instance; and
    * {@code toString} returns the standard string representation that {@code Object#toString} would have returned.
    * <p/>
    * This method is just a convenience for some uses of the <em>Mockups</em> API.
    * In <em>JMockit Expectations</em> in particular, mocked instances will be automatically created and assigned to any
    * mock fields or parameters.
    *
    * @param loader the class loader under which to define the proxy class; usually this would be the application class
    * loader, which can be obtained from any application class
    * @param interfaceToBeProxied a {@code Class} object for an interface
    *
    * @return the created proxy instance
    *
    * @see #newEmptyProxy(Class)
    * @see #newEmptyProxy(Type...)
    * @see
    * <a href="http://code.google.com/p/jmockit/source/browse/trunk/samples/orderMngmntWebapp/test/orderMngr/domain/order/OrderRepository_MockupsAPI_Test.java#186">
    * Example</a>
    */
   public static <E> E newEmptyProxy(ClassLoader loader, Class<E> interfaceToBeProxied)
   {
      return Utilities.newEmptyProxy(loader, interfaceToBeProxied);
   }

   /**
    * Creates a {@link Proxy} implementation for a given set of interface types.
    * In this created class all methods will be empty, with return values for non-void methods being the appropriate
    * default value ({@literal 0} for {@code int}, {@literal null} for a reference type, and so on).
    * <p/>
    * The {@code equals}, {@code hashCode}, and {@code toString} methods inherited from {@code java.lang.Object} are
    * overridden with an appropriate implementation in each case:
    * {@code equals} is implemented by comparing the two object references (the proxy instance and the method argument)
    * for equality; {@code hashCode} is implemented to return the identity hash code for the proxy instance; and
    * {@code toString} returns the standard string representation that {@code Object#toString} would have returned.
    * <p/>
    * This method is just a convenience for some uses of the <em>Mockups</em> API.
    * In <em>JMockit Expectations</em> in particular, mocked instances will be automatically created and assigned to any
    * mock fields or parameters.
    *
    * @param interfacesToBeProxied one or more {@code Type} objects, each of which can be a {@code Class} object for an
    * interface, a {@link ParameterizedType} whose raw type is an interface, or a {@link TypeVariable} whose bounds are
    * interfaces
    *
    * @return the created proxy instance
    */
   public static <E> E newEmptyProxy(Type... interfacesToBeProxied)
   {
      return Utilities.newEmptyProxy(null, interfacesToBeProxied);
   }
}
