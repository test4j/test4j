/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.capturing;

import java.lang.instrument.*;
import java.security.*;
import java.util.*;

import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

final class CaptureTransformer implements ClassFileTransformer
{
   private final CapturedType metadata;
   private final String capturedType;
   private final CaptureOfImplementations modifierFactory;
   private final SuperTypeCollector superTypeCollector;
   private final Map<String, byte[]> transformedClasses;
   private boolean inactive;

   CaptureTransformer(
      CapturedType metadata, CaptureOfImplementations modifierFactory, boolean registerTransformedClasses)
   {
      this.metadata = metadata;
      capturedType = Type.getInternalName(metadata.baseType);
      this.modifierFactory = modifierFactory;
      superTypeCollector = new SuperTypeCollector();
      transformedClasses = registerTransformedClasses ? new HashMap<String, byte[]>(2) : null;
   }

   void deactivate()
   {
      inactive = true;

      if (transformedClasses != null) {
         RedefinitionEngine redefinitionEngine = new RedefinitionEngine();

         for (Map.Entry<String, byte[]> classNameAndOriginalBytecode : transformedClasses.entrySet()) {
            String className = classNameAndOriginalBytecode.getKey();
            byte[] originalBytecode = classNameAndOriginalBytecode.getValue();
            redefinitionEngine.restoreToDefinition(className, originalBytecode);
         }

         transformedClasses.clear();
      }
   }

   public byte[] transform(
      ClassLoader loader, String internalClassName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
   {
      if (
         inactive || classBeingRedefined != null || TestRun.getCurrentTestInstance() == null ||
         internalClassName.startsWith("mockit/internal/"))
      {
         return null;
      }

      ClassReader cr = new ClassReader(classfileBuffer);

      try {
         cr.accept(superTypeCollector, ClassReader.SKIP_DEBUG);
      }
      catch (VisitInterruptedException ignore) {
         if (superTypeCollector.classExtendsCapturedType) {
            String className = internalClassName.replace('/', '.');

            if (metadata.isToBeCaptured(loader, className)) {
               return modifyAndRegisterClass(loader, className, cr);
            }
         }
      }

      return null;
   }

   private byte[] modifyAndRegisterClass(ClassLoader loader, String className, ClassReader cr)
   {
      ClassVisitor modifier = modifierFactory.createModifier(loader, cr, capturedType);
      cr.accept(modifier, 0);

      byte[] originalBytecode = cr.b;

      if (transformedClasses == null) {
         TestRun.mockFixture().addTransformedClass(className, originalBytecode);
      }
      else {
         transformedClasses.put(className, originalBytecode);
      }

      return modifier.toByteArray();
   }

   private final class SuperTypeCollector extends ClassVisitor
   {
      boolean classExtendsCapturedType;

      @Override
      public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
      {
         classExtendsCapturedType = false;

         if (capturedType.equals(superName)) {
            classExtendsCapturedType = true;
         }
         else {
            for (String itfc : interfaces) {
               if (capturedType.equals(itfc)) {
                  classExtendsCapturedType = true;
                  break;
               }
            }
         }

         if (!classExtendsCapturedType && !"java/lang/Object".equals(superName)) {
            String superClassName = superName.replace('/', '.');
            ClassReader cr = ClassFile.createClassFileReader(superClassName);
            cr.accept(superTypeCollector, ClassReader.SKIP_DEBUG);
         }

         throw VisitInterruptedException.INSTANCE;
      }
   }
}
