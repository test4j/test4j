/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal;

import static java.lang.reflect.Modifier.*;

import mockit.external.asm4.*;
import mockit.internal.filtering.*;
import mockit.internal.startup.*;

import static mockit.external.asm4.Opcodes.*;

public final class StubOutModifier extends BaseClassModifier
{
   private final MockingConfiguration stubbingCfg;

   public StubOutModifier(ClassReader cr, MockingConfiguration stubbingConfiguration)
   {
      super(cr);
      stubbingCfg = stubbingConfiguration;
   }

   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
   {
      if (
         isAbstract(access) || (access & ACC_SYNTHETIC) != 0 || isNative(access) && !Startup.isJava6OrLater() ||
         stubbingCfg != null && !stubbingCfg.matchesFilters(name, desc)
      ) {
         return super.visitMethod(access, name, desc, signature, exceptions);
      }

      startModifiedMethodVersion(access, name, desc, signature, exceptions);

      if ("<init>".equals(name)) {
         generateCallToSuperConstructor();
      }

      generateEmptyImplementation(desc);
      return methodAnnotationsVisitor;
   }
}
