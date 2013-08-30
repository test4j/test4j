/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.annotations;

import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.filtering.*;
import mockit.internal.startup.*;
import mockit.internal.state.*;

public final class ClassStubbing
{
   private final Class<?> realClass;
   private final MockingConfiguration stubbingConfiguration;

   public ClassStubbing(Class<?> realClass)
   {
      this.realClass = realClass;
      stubbingConfiguration = null;
   }

   public ClassStubbing(Class<?> realClass, boolean filtersNotInverted, String... filters)
   {
      this.realClass = realClass;
      stubbingConfiguration = filters.length == 0 ? null : new MockingConfiguration(filters, filtersNotInverted);
   }

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

      ClassReader rcReader = new ClassFile(realClass, true).getReader();
      ClassVisitor rcWriter = new StubOutModifier(rcReader, stubbingConfiguration);
      rcReader.accept(rcWriter, 0);
      byte[] modifiedClassFile = rcWriter.toByteArray();

      Startup.redefineMethods(realClass, modifiedClassFile);

      return modifiedClassFile;
   }

   public void stubOutAtStartup() { stubOutClass(); }
}
