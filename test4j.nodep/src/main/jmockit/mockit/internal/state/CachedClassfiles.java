/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.lang.instrument.*;
import java.security.*;
import java.util.*;

/**
 * Holds a map of internal class names to the corresponding class files (bytecode arrays), for the classes
 * that have already been loaded during the test run.
 * These classfiles are not necessarily the same as those stored in the corresponding ".class" files
 * available from the runtime classpath.
 * If any third-party {@link java.lang.instrument.ClassFileTransformer}s are active, those original classfiles
 * may have been modified before being loaded by the JVM.
 * JMockit installs a {@code ClassFileTransformer} of its own which saves all potentially modified classfiles
 * here.
 * <p/>
 * This bytecode cache allows classes to be mocked and un-mocked correctly, even in the presence of other
 * bytecode modification agents such as the AspectJ load-time weaver.
 */
public final class CachedClassfiles implements ClassFileTransformer
{
   public static final CachedClassfiles INSTANCE = new CachedClassfiles();

   private final Map<ClassLoader, Map<String, byte[]>> classLoadersAndClassfiles =
      new WeakHashMap<ClassLoader, Map<String, byte[]>>(2);

   private ClassDefinition[] classesBeingMocked;
   public void setClassesBeingMocked(ClassDefinition[] classDefs) { classesBeingMocked = classDefs; }

   private CachedClassfiles() {}

   public byte[] transform(
      ClassLoader loader, String classDesc, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
   {
      if (classBeingRedefined == null) { // class definition
         if (!isExcluded(classDesc)) {
            addClassfile(loader, classDesc, classfileBuffer);
         }
      }
      else if (!isBeingMocked(classBeingRedefined) && !isExcluded(classDesc)) { // class redefinition
         addClassfileIfNotYetPresent(loader, classDesc, classfileBuffer);
      }

      return null;
   }

   private boolean isExcluded(String classDesc)
   {
      return
         classDesc.startsWith("org/junit/") ||
         classDesc.startsWith("sun/") && !classDesc.startsWith("sun/proxy/$") ||
         classDesc.startsWith("com/intellij/") ||
         classDesc.startsWith("mockit/internal/") || classDesc.startsWith("mockit/integration/") ||
         classDesc.startsWith("mockit/coverage/") ||
         classDesc.startsWith("mockit/") && classDesc.indexOf('$') < 0;
   }

   private boolean isBeingMocked(Class<?> classBeingRedefined)
   {
      if (classesBeingMocked != null) {
         for (ClassDefinition classDef : classesBeingMocked) {
            if (classDef.getDefinitionClass() == classBeingRedefined) {
               return true;
            }
         }
      }

      return false;
   }

   private synchronized void addClassfile(ClassLoader loader, String classDesc, byte[] classfile)
   {
      Map<String, byte[]> classfiles = getClassfiles(loader);
      classfiles.put(classDesc, classfile);
   }

   private Map<String, byte[]> getClassfiles(ClassLoader loader)
   {
      Map<String, byte[]> classfiles = classLoadersAndClassfiles.get(loader);

      if (classfiles == null) {
         classfiles = new HashMap<String, byte[]>(100);
         classLoadersAndClassfiles.put(loader, classfiles);
      }

      return classfiles;
   }

   private synchronized void addClassfileIfNotYetPresent(ClassLoader loader, String classDesc, byte[] classfile)
   {
      Map<String, byte[]> classfiles = getClassfiles(loader);

      if (!classfiles.containsKey(classDesc)) {
         classfiles.put(classDesc, classfile);
      }
   }

   private synchronized byte[] findClassfile(Class<?> aClass)
   {
      Map<String, byte[]> classfiles = getClassfiles(aClass.getClassLoader());
      return classfiles.get(aClass.getName().replace('.', '/'));
   }

   private synchronized byte[] findClassfile(ClassLoader loader, String classDesc)
   {
      Map<String, byte[]> classfiles = getClassfiles(loader);
      return classfiles.get(classDesc);
   }

   public static byte[] getClassfile(Class<?> aClass) { return INSTANCE.findClassfile(aClass); }

   public static byte[] getClassfile(ClassLoader loader, String internalClassName)
   {
      return INSTANCE.findClassfile(loader, internalClassName);
   }

   public static void addClassfile(Class<?> aClass, byte[] classfile)
   {
      INSTANCE.addClassfile(aClass.getClassLoader(), aClass.getName().replace('.', '/'), classfile);
   }
}
