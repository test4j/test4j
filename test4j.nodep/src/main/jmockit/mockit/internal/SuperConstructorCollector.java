/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import mockit.external.asm4.*;
import mockit.internal.util.*;

@SuppressWarnings("FieldAccessedSynchronizedAndUnsynchronized")
final class SuperConstructorCollector extends ClassVisitor
{
   static final SuperConstructorCollector INSTANCE = new SuperConstructorCollector();

   private final Map<String, String> cache = new HashMap<String, String>();
   private String constructorDesc;
   private boolean samePackage;

   private SuperConstructorCollector() {}

   synchronized String findConstructor(String classDesc, String superClassDesc)
   {
      constructorDesc = cache.get(superClassDesc);

      if (constructorDesc != null) {
         return constructorDesc;
      }

      findIfBothClassesAreInSamePackage(classDesc, superClassDesc);

      ClassReader cr = createClassReader(superClassDesc);
      try { cr.accept(this, ClassReader.SKIP_DEBUG); } catch (VisitInterruptedException ignore) {}

      cache.put(superClassDesc, constructorDesc);
      
      return constructorDesc;
   }

   private void findIfBothClassesAreInSamePackage(String classDesc, String superClassDesc)
   {
      int p1 = classDesc.lastIndexOf('/');
      int p2 = superClassDesc.lastIndexOf('/');
      samePackage = p1 == p2 && (p1 < 0 || classDesc.substring(0, p1).equals(superClassDesc.substring(0, p2)));
   }

   private ClassReader createClassReader(String className)
   {
      try {
         return ClassFile.readClass(className);
      }
      catch (IOException e) {
         throw new RuntimeException("Failed to read class file for " + className, e);
      }
   }

   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
   {
      if (isAccessible(access) && "<init>".equals(name)) {
         constructorDesc = desc;
         throw VisitInterruptedException.INSTANCE;
      }

      return null;
   }

   private boolean isAccessible(int access) { return access != Modifier.PRIVATE && (access != 0 || samePackage); }
}
