/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.mockups;

import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.startup.*;
import mockit.internal.state.*;

public final class ClassStubbing
{
   private final Class<?> realClass;

   public ClassStubbing(Class<?> realClass) { this.realClass = realClass; }

   public void stubOut()
   {
      byte[] modifiedClassFile = stubOutClass();

      String classDesc = Type.getInternalName(realClass);
      TestRun.mockFixture().addRedefinedClass(classDesc, realClass, modifiedClassFile);
   }

   private byte[] stubOutClass()
   {
      if (realClass.isInterface() || realClass.isArray()) {
         throw new IllegalArgumentException("Not a modifiable class: " + realClass.getName());
      }

      ClassReader rcReader = ClassFile.createReaderFromLastRedefinitionIfAny(realClass);
      ClassVisitor rcWriter = new StubOutModifier(rcReader);
      rcReader.accept(rcWriter, 0);
      byte[] modifiedClassFile = rcWriter.toByteArray();

      Startup.redefineMethods(realClass, modifiedClassFile);

      return modifiedClassFile;
   }

   public void stubOutAtStartup() { stubOutClass(); }
}
