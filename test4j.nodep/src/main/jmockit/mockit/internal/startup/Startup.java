/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.startup;

import java.io.*;
import java.lang.instrument.*;

import mockit.internal.expectations.transformation.*;

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
    * instrumentation service. Such a class must be public and have a public constructor accepting two parameters: the
    * first of type {@code Map&lt;String, byte[]>}, which will receive a map for storing the transformed classes; and
    * the second of type {@code String}, which will receive any tool arguments.</li>
    * <li>An <em>external mock</em>, which can be any class with a public no-args constructor.
    * Such a class will be used to redefine one or more real classes.
    * The real classes can be specified in one of two ways: by providing a regular expression matching class names as
    * the tool arguments, or by annotating the external mock class with {@link mockit.MockClass}.</li>
    * </ol>
    *
    * @param agentArgs not used
    * @param inst      the instrumentation service provided by the JVM
    */
   public static void premain(String agentArgs, Instrumentation inst) throws Exception
   {
      initialize(true, inst);
   }

   @SuppressWarnings("UnusedDeclaration")
   public static void agentmain(String agentArgs, Instrumentation inst) throws Exception
   {
      initialize(false, inst);
   }

   private static void initialize(boolean initializeTestNG, Instrumentation inst) throws IOException
   {
      instrumentation = inst;
      new JMockitInitialization().initialize(initializeTestNG);
      inst.addTransformer(new ProxyClassfileSavingTransformer());
      inst.addTransformer(new ExpectationsTransformer(inst));
   }

   public static Instrumentation instrumentation()
   {
      verifyInitialization();
      return instrumentation;
   }

   public static boolean wasInitializedOnDemand() { return initializedOnDemand; }

   public static void verifyInitialization()
   {
      if (instrumentation == null) {
         initializedOnDemand = new AgentInitialization().initializeAccordingToJDKVersion();

         if (initializedOnDemand) {
            System.out.println(
               "WARNING: JMockit was initialized on demand, which may cause certain tests to fail;\n" +
               "please check the documentation for better ways to get it initialized.");
         }
      }
   }

   public static boolean initializeIfNeeded()
   {
      if (instrumentation == null) {
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

   public static void initializeIfPossible()
   {
      if (jdk6OrLater) {
         initializeIfNeeded();
      }
   }

   public static void redefineMethods(Class<?> classToRedefine, byte[] modifiedClassfile)
   {
      redefineMethods(new ClassDefinition(classToRedefine, modifiedClassfile));
   }

   public static void redefineMethods(ClassDefinition... classDefs)
   {
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
   }
}
