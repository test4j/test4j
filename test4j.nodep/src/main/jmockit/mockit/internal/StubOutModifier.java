/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal;

import static java.lang.reflect.Modifier.*;

import mockit.external.asm4.*;
import mockit.internal.startup.*;

import static mockit.external.asm4.Opcodes.*;

public final class StubOutModifier extends BaseClassModifier
{
   public StubOutModifier(ClassReader cr) { super(cr); }

   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
   {
      if (isAbstract(access) || (access & ACC_SYNTHETIC) != 0 || isNative(access) && !Startup.isJava6OrLater()) {
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
