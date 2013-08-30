/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.annotations;

import java.lang.reflect.*;

import static mockit.external.asm4.Opcodes.*;

import mockit.*;
import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

/**
 * Responsible for collecting the signatures of all methods defined in a given mock class which are explicitly annotated
 * as {@link mockit.Mock mocks}.
 */
final class AnnotatedMockMethodCollector extends ClassVisitor
{
   private static final int INVALID_FIELD_ACCESSES = ACC_FINAL + ACC_STATIC + ACC_SYNTHETIC;
   private static final int INVALID_METHOD_ACCESSES = ACC_BRIDGE + ACC_SYNTHETIC + ACC_ABSTRACT + ACC_NATIVE;

   private final AnnotatedMockMethods mockMethods;

   // Helper fields:
   private Class<?> classToCollectMocksFrom;
   private boolean collectingFromSuperClass;
   private String enclosingClassDescriptor;

   AnnotatedMockMethodCollector(AnnotatedMockMethods mockMethods) { this.mockMethods = mockMethods; }

   void collectMockMethods(Class<?> mockClass)
   {
      Utilities.registerLoadedClass(mockClass);

      classToCollectMocksFrom = mockClass;

      do {
         ClassReader mcReader = ClassFile.createClassFileReader(classToCollectMocksFrom);
         mcReader.accept(this, ClassReader.SKIP_FRAMES);
         classToCollectMocksFrom = classToCollectMocksFrom.getSuperclass();
         collectingFromSuperClass = true;
      }
      while (classToCollectMocksFrom != Object.class && classToCollectMocksFrom != MockUp.class);
   }

   @Override
   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
   {
      if (!collectingFromSuperClass) {
         mockMethods.setMockClassInternalName(name);

         int p = name.lastIndexOf('$');

         if (p > 0) {
            enclosingClassDescriptor = "(L" + name.substring(0, p) + ";)V";
         }
      }
   }

   @Override
   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
   {
      if ((access & INVALID_FIELD_ACCESSES) == 0 && "it".equals(name)) {
         mockMethods.setWithItField(true);
      }

      return null;
   }

   /**
    * Adds the method specified to the set of mock methods, as long as it's annotated with {@code @Mock}.
    *
    * @param signature generic signature for a Java 5 generic method, ignored since redefinition only needs to consider
    * the "erased" signature
    * @param exceptions zero or more thrown exceptions in the method "throws" clause, also ignored
    */
   @Override
   public MethodVisitor visitMethod(
      final int access, final String methodName, final String methodDesc, String signature, String[] exceptions)
   {
      if ((access & INVALID_METHOD_ACCESSES) != 0) {
         return null;
      }

      if ("<init>".equals(methodName)) {
         if (
            !collectingFromSuperClass && enclosingClassDescriptor != null && methodDesc.equals(enclosingClassDescriptor)
         ) {
            mockMethods.setInnerMockClass(true);
            enclosingClassDescriptor = null;
         }

         return null;
      }

      return new MethodVisitor()
      {
         private boolean annotatedAsMockMethod;

         @Override
         public AnnotationVisitor visitAnnotation(String desc, boolean visible)
         {
            if ("Lmockit/Mock;".equals(desc)) {
               AnnotatedMockMethods.MockMethod mockMethod =
                  mockMethods.addMethod(collectingFromSuperClass, methodName, methodDesc, Modifier.isStatic(access));

               if (mockMethod != null) {
                  annotatedAsMockMethod = true;
                  return new MockAnnotationVisitor(mockMethod);
               }
            }

            return null;
         }

         @Override
         public void visitLocalVariable(
            String paramName, String paramDesc, String paramSignature, Label start, Label end, int index)
         {
            ParameterNames.registerName(mockMethods.getMockClassInternalName(), methodName, methodDesc, paramName);
         }

         @Override
         public void visitEnd()
         {
            if (
               !annotatedAsMockMethod && mockMethods.classWithMethodToSelectSubclasses == null &&
               "shouldBeMocked".equals(methodName) && "(Ljava/lang/ClassLoader;Ljava/lang/String;)Z".equals(methodDesc)
            ) {
               mockMethods.classWithMethodToSelectSubclasses = classToCollectMocksFrom;
            }
         }
      };
   }

   private final class MockAnnotationVisitor extends AnnotationVisitor
   {
      private final AnnotatedMockMethods.MockMethod mockMethod;
      private MockState mockState;
      private Boolean reentrant;

      private MockAnnotationVisitor(AnnotatedMockMethods.MockMethod mockMethod)
      {
         this.mockMethod = mockMethod;

         if (mockMethod.hasInvocationParameter) {
            getMockState();
         }
      }

      @Override
      public void visit(String name, Object value)
      {
         if ("invocations".equals(name)) {
            getMockState().expectedInvocations = (Integer) value;
         }
         else if ("minInvocations".equals(name)) {
            getMockState().minExpectedInvocations = (Integer) value;
         }
         else if ("maxInvocations".equals(name)) {
            getMockState().maxExpectedInvocations = (Integer) value;
         }
         else { // name == "reentrant"
            reentrant = (Boolean) value;

            if (reentrant) {
               if (mockMethod.isForConstructor()) {
                  throw new IllegalArgumentException("Invalid reentrant mock method for constructor");
               }

               getMockState();
            }
         }
      }

      private MockState getMockState()
      {
         if (mockState == null) {
            mockState = new MockState(mockMethod);
         }

         return mockState;
      }

      @Override
      public void visitEnd()
      {
         if (mockState != null) {
            if (
               reentrant == Boolean.TRUE ||
               reentrant == null && mockMethod.hasInvocationParameter && !mockMethod.isForConstructor()
            ) {
               mockState.makeReentrant();
            }

            mockMethods.addMockState(mockState);
         }
      }
   }
}
