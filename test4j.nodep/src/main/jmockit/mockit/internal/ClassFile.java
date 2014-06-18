/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import mockit.external.asm4.*;
import mockit.internal.state.*;

public final class ClassFile
{
   private static final Map<String, ClassReader> CLASS_FILES = new ConcurrentHashMap<String, ClassReader>();

   private ClassFile() {}

   public static final class NotFoundException extends RuntimeException
   {
      private NotFoundException(String className)
      {
         super("Unable to find class file for " + className.replace('/', '.'));
      }
   }

   private static void verifyClassFileFound(InputStream classFile, String className)
   {
      if (classFile == null) {
         throw new NotFoundException(className);
      }
   }

   public static ClassReader createClassFileReader(Class<?> aClass)
   {
      byte[] cachedClassfile = CachedClassfiles.getClassfile(aClass);

      if (cachedClassfile != null) {
         return new ClassReader(cachedClassfile);
      }

      String className = aClass.getName();
      InputStream classFile = aClass.getResourceAsStream('/' + internalClassName(className) + ".class");
      verifyClassFileFound(classFile, className);

      try {
         return new ClassReader(classFile);
      }
      catch (IOException e) {
         throw new RuntimeException("Failed to read class file for " + className, e);
      }
   }

   private static String internalClassName(String className) { return className.replace('.', '/'); }

   public static ClassReader createReaderOrGetFromCache(Class<?> aClass)
   {
      byte[] cachedClassfile = CachedClassfiles.getClassfile(aClass);

      if (cachedClassfile != null) {
         return new ClassReader(cachedClassfile);
      }

      String className = aClass.getName();
      String classDesc = internalClassName(className);
      ClassReader reader = CLASS_FILES.get(classDesc);

      if (reader == null) {
         InputStream classFile = readClassFromClasspath(classDesc);
         verifyClassFileFound(classFile, className);

         try {
            reader = new ClassReader(classFile);
         }
         catch (IOException e) {
            throw new RuntimeException("Failed to read class file for " + className, e);
         }

         CLASS_FILES.put(classDesc, reader);
      }

      return reader;
   }

   private static InputStream readClassFromClasspath(String internalClassName)
   {
      String classFileName = internalClassName + ".class";
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      InputStream inputStream = contextClassLoader.getResourceAsStream(classFileName);

      if (inputStream == null) {
         ClassLoader thisClassLoader = ClassFile.class.getClassLoader();

         if (thisClassLoader != contextClassLoader) {
            inputStream = thisClassLoader.getResourceAsStream(classFileName);

            if (inputStream == null) {
               Class<?> testClass = TestRun.getCurrentTestClass();

               if (testClass != null) {
                  inputStream = testClass.getClassLoader().getResourceAsStream(classFileName);
               }
            }
         }
      }

      return inputStream;
   }

   public static ClassReader createReaderFromLastRedefinitionIfAny(Class<?> aClass)
   {
      byte[] classfile = TestRun.mockFixture().getRedefinedClassfile(aClass);

      if (classfile == null) {
         classfile = CachedClassfiles.getClassfile(aClass);
      }

      if (classfile != null) {
         return new ClassReader(classfile);
      }

      String className = aClass.getName();
      String classDesc = internalClassName(className);
      ClassReader reader;

      try {
         reader = readClass(classDesc);
      }
      catch (IOException e) {
         throw new RuntimeException("Failed to read class file for " + className, e);
      }

      CLASS_FILES.put(classDesc, reader);
      return reader;
   }

   public static ClassReader createClassFileReader(ClassLoader loader, String internalClassName)
   {
      byte[] cachedClassfile = CachedClassfiles.getClassfile(loader, internalClassName);

      if (cachedClassfile != null) {
         return new ClassReader(cachedClassfile);
      }

      try {
         return readClass(internalClassName);
      }
      catch (IOException e) {
         throw new RuntimeException("Failed to read class file for " + internalClassName.replace('/', '.'), e);
      }
   }

   public static ClassReader readClass(String classDesc) throws IOException
   {
      InputStream classFile = readClassFromClasspath(classDesc);

      if (classFile == null) {
         throw new NotFoundException(classDesc.replace('/', '.'));
      }

      return new ClassReader(classFile);
   }

   public static void visitClass(String internalClassName, ClassVisitor visitor)
   {
      InputStream classFile = readClassFromClasspath(internalClassName);
      verifyClassFileFound(classFile, internalClassName);

      try {
         ClassReader cr = new ClassReader(classFile);
         cr.accept(visitor, ClassReader.SKIP_DEBUG);
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
