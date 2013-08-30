/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.capturing;

import java.util.*;

import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.startup.*;
import mockit.internal.state.*;

public abstract class CaptureOfImplementations implements Runnable
{
   private final List<CaptureTransformer> captureTransformers;

   protected CaptureOfImplementations() { captureTransformers = new ArrayList<CaptureTransformer>(); }

   protected abstract ClassSelector createClassSelector();
   protected abstract ClassVisitor createModifier(ClassLoader cl, ClassReader cr, String capturedTypeDesc);

   public final void makeSureAllSubtypesAreModified(Class<?> baseType)
   {
      makeSureAllSubtypesAreModified(baseType, false);
   }

   public final void makeSureAllSubtypesAreModified(Class<?> baseType, boolean registerCapturedClasses)
   {
      if (baseType == null) {
         throw new IllegalArgumentException("Capturing implementations of multiple base types is not supported");
      }

      String baseTypeDesc = Type.getInternalName(baseType);
      ClassSelector classSelector = createClassSelector();
      CapturedType captureMetadata = new CapturedType(baseType, classSelector);

      redefineClassesAlreadyLoaded(captureMetadata, baseTypeDesc);
      createCaptureTransformer(captureMetadata, registerCapturedClasses);
   }

   private void redefineClassesAlreadyLoaded(CapturedType captureMetadata, String baseTypeDesc)
   {
      Class<?>[] classesLoaded = Startup.instrumentation().getAllLoadedClasses();

      for (Class<?> aClass : classesLoaded) {
         if (captureMetadata.isToBeCaptured(aClass)) {
            redefineClass(aClass, baseTypeDesc);
         }
      }
   }

   private void redefineClass(Class<?> realClass, String baseTypeDesc)
   {
      if (!TestRun.mockFixture().containsRedefinedClass(realClass)) {
         ClassReader classReader = new ClassFile(realClass, false).getReader();
         ClassVisitor modifier = createModifier(realClass.getClassLoader(), classReader, baseTypeDesc);
         classReader.accept(modifier, 0);
         byte[] modifiedClass = modifier.toByteArray();

         new RedefinitionEngine(realClass).redefineMethodsWhileRegisteringTheClass(modifiedClass);
      }
   }

   private void createCaptureTransformer(CapturedType captureMetadata, boolean registerCapturedClasses)
   {
      CaptureTransformer transformer = new CaptureTransformer(captureMetadata, this, registerCapturedClasses);
      Startup.instrumentation().addTransformer(transformer);
      captureTransformers.add(transformer);
   }

   public void cleanUp()
   {
      for (CaptureTransformer transformer : captureTransformers) {
         transformer.deactivate();
         Startup.instrumentation().removeTransformer(transformer);
      }

      captureTransformers.clear();
   }

   public final void run() { cleanUp(); }
}
