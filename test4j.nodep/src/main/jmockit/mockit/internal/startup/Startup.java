/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.startup;

import java.io.*;
import java.lang.instrument.*;
import java.util.*;
import java.util.logging.*;

import mockit.*;
import mockit.internal.mockups.*;
import mockit.internal.expectations.transformation.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

/**
 * This is the "agent class" that initializes the JMockit "Java agent". It is not intended for use in client code.
 * It must be public, however, so the JVM can call the {@code premain} method, which as the name implies is called
 * <em>before</em> the {@code main} method.
 *
 * @see #premain(String, Instrumentation)
 */
public final class Startup
{
   static final String javaSpecVersion = System.getProperty("java.specification.version");
   static final boolean jdk6OrLater =
      "1.6".equals(javaSpecVersion) || "1.7".equals(javaSpecVersion) || "1.8".equals(javaSpecVersion);

   public static boolean initializing;
   private static Instrumentation instrumentation;
   private static boolean initializedOnDemand;

   private Startup() {}

   public static boolean isJava6OrLater() { return jdk6OrLater; }

   /**
    * This method must only be called by the JVM, to provide the instrumentation object.
    * In order for this to occur, the JVM must be started with "-javaagent:jmockit.jar" as a command line parameter
    * (assuming the jar file is in the current directory).
    * <p/>
    * It is also possible to load other <em>instrumentation tools</em> at this time, by having set the "jmockit-tools"
    * and/or "jmockit-mocks" system properties in the JVM command line.
    * There are two types of instrumentation tools:
    * <ol>
    * <li>A {@link ClassFileTransformer class file transformer}, which will be instantiated and added to the JVM
    * instrumentation service. Such a class must have a no-args constructor.</li>
    * <li>An <em>external mock</em>, which should be a {@code MockUp} subclass with a no-args constructor.
    * </ol>
    *
    * @param agentArgs not used
    * @param inst      the instrumentation service provided by the JVM
    */
   public static void premain(String agentArgs, Instrumentation inst) throws IOException
   {
      initialize(true, inst);
   }

   private static void initialize(boolean initializeTestNG, Instrumentation inst) throws IOException
   {
      if (instrumentation == null) {
         instrumentation = inst;
         applyStartupMocks(initializeTestNG);
         inst.addTransformer(CachedClassfiles.INSTANCE);
         inst.addTransformer(new ExpectationsTransformer(inst));
      }
   }

   private static void applyStartupMocks(boolean initializeTestNG) throws IOException
   {
      initializing = true;

      try {
         new JMockitInitialization().initialize(initializeTestNG);
      }
      finally {
         initializing = false;
      }
   }

   @SuppressWarnings("UnusedDeclaration")
   public static void agentmain(String agentArgs, Instrumentation inst) throws IOException
   {
      initialize(false, inst);

      Properties properties = System.getProperties();
      properties.put("jmockit-instrumentation", inst);
      ClassLoader customCL = (ClassLoader) properties.remove("jmockit-customCL");

      if (customCL != null) {
         reinitializeJMockitUnderCustomClassLoader(customCL);
      }
   }

   private static void reinitializeJMockitUnderCustomClassLoader(ClassLoader customLoader)
   {
      Class<?> startupClass;

      try {
         startupClass = customLoader.loadClass(Startup.class.getName());
      }
      catch (ClassNotFoundException ignore) {
         return;
      }

      replaceCustomLogManagerAsNeeded();

      System.out.println("JMockit: Reinitializing under custom class loader " + customLoader);
      FieldReflection.setField(startupClass, null, "instrumentation", instrumentation);
      MethodReflection.invoke(startupClass, (Object) null, "reapplyStartupMocks");
   }

   private static void replaceCustomLogManagerAsNeeded()
   {
      final LogManager logManager = LogManager.getLogManager();

      if (logManager.getClass() != LogManager.class) {
         LogManager newLogManager = new LogManager() {
            @Override
            public boolean addLogger(Logger logger)
            {
               logManager.addLogger(logger);
               return super.addLogger(logger);
            }

            @Override
            public Logger getLogger(String name)
            {
               Logger logger = super.getLogger(name);
               if (logger == null) logger = logManager.getLogger(name);
               return logger;
            }
         };

         FieldReflection.setField(LogManager.class, null, "manager", newLogManager);
      }
   }

   @SuppressWarnings("unused")
   private static void reapplyStartupMocks()
   {
      try { applyStartupMocks(false); } catch (IOException e) { throw new RuntimeException(e); }
   }

   public static Instrumentation instrumentation()
   {
      verifyInitialization();
      return instrumentation;
   }

   public static void initialize(Instrumentation inst, Class<? extends MockUp<?>>... mockUpClasses) throws IOException
   {
      boolean fullJMockit = false;

      try {
         Class.forName("mockit.internal.expectations.transformation.ExpectationsTransformer");
         fullJMockit = true;
      }
      catch (ClassNotFoundException ignored) {}

      instrumentation = inst;
      initializing = true;

      try {
         if (fullJMockit) new JMockitInitialization().initialize(true);
         applyMockUpClasses(mockUpClasses);
      }
      finally {
         initializing = false;
      }

      if (fullJMockit) {
         inst.addTransformer(CachedClassfiles.INSTANCE);
         inst.addTransformer(new ExpectationsTransformer(inst));
      }
   }

   private static void applyMockUpClasses(Class<? extends MockUp<?>>[] mockUpClasses)
   {
      for (Class<? extends MockUp<?>> mockUpClass : mockUpClasses) {
         try {
            new MockClassSetup(mockUpClass).redefineMethods();
         }
         catch (UnsupportedOperationException ignored) {}
         catch (Throwable unexpectedFailure) {
            StackTrace.filterStackTrace(unexpectedFailure);
            unexpectedFailure.printStackTrace();
         }
      }
   }

   public static boolean wasInitializedOnDemand() { return initializedOnDemand; }

   public static void verifyInitialization()
   {
      if (getInstrumentation() == null) {
         initializedOnDemand = new AgentInitialization().initializeAccordingToJDKVersion();

         if (initializedOnDemand) {
            System.out.println(
               "WARNING: JMockit was initialized on demand, which may cause certain tests to fail;\n" +
               "please check the documentation for better ways to get it initialized.");
         }
      }
   }

   private static Instrumentation getInstrumentation()
   {
      if (instrumentation == null) {
         instrumentation = (Instrumentation) System.getProperties().remove("jmockit-instrumentation");

         if (instrumentation != null) {
            reapplyStartupMocks();
         }
      }

      return instrumentation;
   }

   public static boolean initializeIfPossible()
   {
      if (getInstrumentation() == null) {
         if (jdk6OrLater) {
            ClassLoader currentCL = Startup.class.getClassLoader();

            if (currentCL != ClassLoader.getSystemClassLoader()) { // custom CL detected
               System.getProperties().put("jmockit-customCL", currentCL);
            }
         }

         try {
            return new AgentInitialization().initializeAccordingToJDKVersion();
         }
         catch (RuntimeException e) {
            e.printStackTrace(); // makes sure the exception gets printed at least once
            throw e;
         }
      }

      return false;
   }

   public static void redefineMethods(Class<?> classToRedefine, byte[] modifiedClassfile)
   {
      redefineMethods(new ClassDefinition(classToRedefine, modifiedClassfile));
   }

   public static void redefineMethods(ClassDefinition... classDefs)
   {
      CachedClassfiles.INSTANCE.setClassesBeingMocked(classDefs);

      try {
         instrumentation().redefineClasses(classDefs);
      }
      catch (ClassNotFoundException e) {
         // should never happen
         throw new RuntimeException(e);
      }
      catch (UnmodifiableClassException e) {
         throw new RuntimeException(e);
      }
      catch (InternalError e) {
         // If a class to be redefined hasn't been loaded yet, the JVM may get a NoClassDefFoundError during
         // redefinition. Unfortunately, it then throws a plain InternalError instead.
         for (ClassDefinition classDef : classDefs) {
            detectMissingDependenciesIfAny(classDef.getDefinitionClass());
         }

         // If the above didn't throw upon detecting a NoClassDefFoundError, the original error is re-thrown.
         throw e;
      }
      finally {
         CachedClassfiles.INSTANCE.setClassesBeingMocked(null);
      }
   }

   private static void detectMissingDependenciesIfAny(Class<?> mockedClass)
   {
      try {
         Class.forName(mockedClass.getName(), true, mockedClass.getClassLoader());
      }
      catch (NoClassDefFoundError e) {
         throw new RuntimeException("Unable to mock " + mockedClass + " due to a missing dependency", e);
      }
      catch (ClassNotFoundException ignore) {
         // Shouldn't happen since the mocked class would already have been found in the classpath.
      }
   }
}
