/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal;

import java.io.*;
import java.lang.instrument.*;
import java.util.*;
import java.util.Map.*;

import mockit.internal.startup.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

public final class RedefinitionEngine
{
   private Class<?> realClass;

   public RedefinitionEngine() {}
   public RedefinitionEngine(Class<?> realClass) { this.realClass = realClass; }

   public static void redefineClasses(ClassDefinition... definitions)
   {
      Startup.redefineMethods(definitions);

      MockFixture mockFixture = TestRun.mockFixture();
      
      for (ClassDefinition def : definitions) {
         mockFixture.addRedefinedClass(def.getDefinitionClass(), def.getDefinitionClassFile());
      }
   }

   public void redefineMethodsWhileRegisteringTheClass(byte[] modifiedClassfile)
   {
      redefineMethods(modifiedClassfile);
      addToMapOfRedefinedClasses(null, modifiedClassfile);
   }

   private void addToMapOfRedefinedClasses(String mockClassInternalName, byte[] modifiedClassfile)
   {
      TestRun.mockFixture().addRedefinedClass(mockClassInternalName, realClass, modifiedClassfile);
   }

   private void redefineMethods(byte[] modifiedClassfile)
   {
      Startup.redefineMethods(realClass, modifiedClassfile);
   }

   public void redefineMethods(Map<Class<?>, byte[]> modifiedClassfiles)
   {
      ClassDefinition[] classDefs = new ClassDefinition[modifiedClassfiles.size()];
      int i = 0;

      for (Entry<Class<?>, byte[]> classAndBytecode : modifiedClassfiles.entrySet()) {
         realClass = classAndBytecode.getKey();
         byte[] modifiedClassfile = classAndBytecode.getValue();

         classDefs[i++] = new ClassDefinition(realClass, modifiedClassfile);
         addToMapOfRedefinedClasses(null, modifiedClassfile);
      }

      Startup.redefineMethods(classDefs);
   }

   public void restoreDefinition(Class<?> aClass, byte[] previousDefinition)
   {
      if (previousDefinition == null) {
         restoreOriginalDefinition(aClass);
      }
      else {
         restoreToDefinition(aClass, previousDefinition);
      }
   }

   public void restoreOriginalDefinition(Class<?> aClass)
   {
      if (!Utilities.isGeneratedImplementationClass(aClass)) {
         realClass = aClass;
         byte[] realClassFile = new ClassFile(aClass, false).getBytecode();
         redefineMethods(realClassFile);
      }
   }

   public void restoreToDefinitionBeforeStartup(Class<?> aClass) throws IOException
   {
      realClass = aClass;
      byte[] realClassFile = ClassFile.readClass(aClass).b;
      redefineMethods(realClassFile);
   }

   private void restoreToDefinition(Class<?> aClass, byte[] definitionToRestore)
   {
      realClass = aClass;
      redefineMethods(definitionToRestore);
   }

   public void restoreToDefinition(String className, byte[] definitionToRestore)
   {
      Class<?> aClass = Utilities.loadClass(className);
      restoreToDefinition(aClass, definitionToRestore);
   }
}
