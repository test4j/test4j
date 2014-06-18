/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.transformation;

import mockit.external.asm4.*;

import static mockit.external.asm4.Opcodes.*;
import static mockit.internal.util.TypeConversion.*;

final class InvocationBlockModifier extends MethodVisitor
{
   private static final String CLASS_DESC = Type.getInternalName(ActiveInvocations.class);

   // Input data:
   private final String owner;
   private final boolean callEndInvocations;

   // Takes care of "withCapture()" matchers, if any:
   private final ArgumentCapturing argumentCapturing;

   // Helper fields that allow argument matchers to be moved to the correct positions of their
   // corresponding parameters:
   private final int[] matcherStacks;
   private int matcherCount;
   private Type[] parameterTypes;

   Capture createCapture(int opcode, int var, String typeToCapture)
   {
      return new Capture(opcode, var, typeToCapture);
   }

   final class Capture
   {
      final int opcode;
      private final int var;
      private final String typeToCapture;
      private int parameterIndex;
      private boolean parameterIndexFixed;

      Capture(int opcode, int var, String typeToCapture)
      {
         this.opcode = opcode;
         this.var = var;
         this.typeToCapture = typeToCapture;
         parameterIndex = matcherCount - 1;
      }

      /**
       * Generates bytecode that will be responsible for performing the following steps:
       * 1. Get the argument value (an Object) for the last matched invocation.
       * 2. Cast to a reference type or unbox to a primitive type, as needed.
       * 3. Store the converted value in its local variable.
       */
      void generateCodeToStoreCapturedValue()
      {
         mv.visitIntInsn(SIPUSH, parameterIndex);
         generateCallToActiveInvocationsMethod("matchedArgument", "(I)Ljava/lang/Object;");

         Type argType = getArgumentType();
         generateCastOrUnboxing(mv, argType, opcode);

         mv.visitVarInsn(opcode, var);
      }

      private Type getArgumentType()
      {
         if (typeToCapture == null) {
            return parameterTypes[parameterIndex];
         }
         else if (typeToCapture.charAt(0) == '[') {
            return Type.getType(typeToCapture);
         }
         else {
            return Type.getType('L' + typeToCapture + ';');
         }
      }

      boolean fixParameterIndex(int originalIndex, int newIndex)
      {
         if (!parameterIndexFixed && parameterIndex == originalIndex) {
            parameterIndex = newIndex;
            parameterIndexFixed = true;
            return true;
         }

         return false;
      }

      void generateCallToSetArgumentTypeIfNeeded()
      {
         if (typeToCapture != null && !isTypeToCaptureSameAsParameterType()) {
            mv.visitIntInsn(SIPUSH, parameterIndex);
            mv.visitLdcInsn(typeToCapture);
            generateCallToActiveInvocationsMethod("setExpectedArgumentType", "(ILjava/lang/String;)V");
         }
      }

      private boolean isTypeToCaptureSameAsParameterType()
      {
         Type parameterType = parameterTypes[parameterIndex];
         int sort = parameterType.getSort();

         if (sort == Type.OBJECT || sort == Type.ARRAY) {
            return typeToCapture.equals(parameterType.getInternalName());
         }
         else {
            return isPrimitiveWrapper(typeToCapture);
         }
      }
   }

   InvocationBlockModifier(MethodVisitor mw, String owner, boolean callEndInvocations)
   {
      super(mw);
      this.owner = owner;
      this.callEndInvocations = callEndInvocations;
      matcherStacks = new int[40];
      argumentCapturing = new ArgumentCapturing();
   }

   private void generateCallToActiveInvocationsMethod(String name, String desc)
   {
      mv.visitMethodInsn(INVOKESTATIC, CLASS_DESC, name, desc);
   }

   @Override
   public void visitFieldInsn(int opcode, String owner, String name, String desc)
   {
      if ((opcode == GETSTATIC || opcode == PUTSTATIC) && isFieldDefinedByInvocationBlock(owner)) {
         if (opcode == PUTSTATIC) {
            if (generateCodeThatReplacesAssignmentToSpecialField(name)) return;
         }
         else if (name.startsWith("any")) {
            generateCodeToAddArgumentMatcherForAnyField(owner, name, desc);
            return;
         }
      }

      mv.visitFieldInsn(opcode, owner, name, desc);
   }

   private boolean isFieldDefinedByInvocationBlock(String owner)
   {
      return
         this.owner.equals(owner) ||
         ("mockit/Expectations mockit/NonStrictExpectations " +
          "mockit/Verifications mockit/VerificationsInOrder " +
          "mockit/FullVerifications mockit/FullVerificationsInOrder").contains(owner);
   }

   private boolean generateCodeThatReplacesAssignmentToSpecialField(String fieldName)
   {
      if ("result".equals(fieldName)) {
         generateCallToActiveInvocationsMethod("addResult", "(Ljava/lang/Object;)V");
         return true;
      }
      else if ("forEachInvocation".equals(fieldName)) {
         generateCallToActiveInvocationsMethod("setHandler", "(Ljava/lang/Object;)V");
         return true;
      }
      else if ("times".equals(fieldName) || "minTimes".equals(fieldName) || "maxTimes".equals(fieldName)) {
         generateCallToActiveInvocationsMethod(fieldName, "(I)V");
         return true;
      }
      else if ("$".equals(fieldName)) {
         generateCallToActiveInvocationsMethod("setErrorMessage", "(Ljava/lang/CharSequence;)V");
         return true;
      }

      return false;
   }

   private void generateCodeToAddArgumentMatcherForAnyField(String owner, String name, String desc)
   {
      mv.visitFieldInsn(GETSTATIC, owner, name, desc);
      generateCallToActiveInvocationsMethod("addArgMatcher", "()V");
      matcherStacks[matcherCount++] = mv.stackSize2;
   }

   @Override
   public void visitMethodInsn(int opcode, String owner, String name, String desc)
   {
      if (opcode == INVOKESTATIC && (isBoxing(owner, name, desc) || isAccessMethod(owner, name))) {
         // It's an invocation to a primitive boxing method or to a synthetic method for private access, just ignore it.
         mv.visitMethodInsn(INVOKESTATIC, owner, name, desc);
      }
      else if (opcode == INVOKEVIRTUAL && owner.equals(this.owner) && name.startsWith("with")) {
         mv.visitMethodInsn(INVOKEVIRTUAL, owner, name, desc);
         matcherStacks[matcherCount++] = mv.stackSize2;
         argumentCapturing.registerCapturingMatcherIfApplicable(name, desc);
      }
      else if (isUnboxing(opcode, owner, desc)) {
         if (argumentCapturing.justAfterWithCaptureInvocation) {
            generateCodeToReplaceNullWithZeroOnTopOfStack(desc.charAt(2));
            argumentCapturing.justAfterWithCaptureInvocation = false;
         }
         else {
            mv.visitMethodInsn(opcode, owner, name, desc);
         }
      }
      else if (matcherCount == 0) {
         mv.visitMethodInsn(opcode, owner, name, desc);
      }
      else {
         parameterTypes = Type.getArgumentTypes(desc);
         int stackSize = mv.stackSize2;
         int stackAfter = stackSize - sumOfParameterSizes();
         boolean mockedInvocationUsingTheMatchers = stackAfter < matcherStacks[0];

         if (mockedInvocationUsingTheMatchers) {
            generateCallsToMoveArgMatchers(stackAfter);
            argumentCapturing.generateCallsToSetArgumentTypesToCaptureIfAny();
            matcherCount = 0;
         }

         mv.visitMethodInsn(opcode, owner, name, desc);

         if (mockedInvocationUsingTheMatchers) {
            argumentCapturing.generateCallsToCaptureMatchedArgumentsIfPending();
         }

         argumentCapturing.justAfterWithCaptureInvocation = false;
      }
   }

   private boolean isAccessMethod(String owner, String name)
   {
      return !owner.equals(this.owner) && name.startsWith("access$");
   }

   private void generateCodeToReplaceNullWithZeroOnTopOfStack(char primitiveTypeCode)
   {
      mv.visitInsn(POP);

      int zeroOpcode;
      switch (primitiveTypeCode) {
         case 'J': zeroOpcode = LCONST_0; break;
         case 'F': zeroOpcode = FCONST_0; break;
         case 'D': zeroOpcode = DCONST_0; break;
         default: zeroOpcode = ICONST_0;
      }
      mv.visitInsn(zeroOpcode);
   }

   private int sumOfParameterSizes()
   {
      int sum = 0;

      for (Type argType : parameterTypes) {
         sum += argType.getSize();
      }

      return sum;
   }

   private void generateCallsToMoveArgMatchers(int initialStack)
   {
      int stack = initialStack;
      int nextMatcher = 0;
      int matcherStack = matcherStacks[0];

      for (int i = 0; i < parameterTypes.length && nextMatcher < matcherCount; i++) {
         stack += parameterTypes[i].getSize();

         if (stack == matcherStack || stack == matcherStack + 1) {
            if (nextMatcher < i) {
               generateCallToMoveArgMatcher(nextMatcher, i);
               argumentCapturing.updateCaptureIfAny(nextMatcher, i);
            }

            matcherStack = matcherStacks[++nextMatcher];
         }
      }
   }

   private void generateCallToMoveArgMatcher(int originalMatcherIndex, int toIndex)
   {
      mv.visitIntInsn(SIPUSH, originalMatcherIndex);
      mv.visitIntInsn(SIPUSH, toIndex);
      generateCallToActiveInvocationsMethod("moveArgMatcher", "(II)V");
   }

   @Override
   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
   {
      // In classes instrumented with EMMA some local variable information can be lost, so we discard it entirely to
      // avoid a ClassFormatError.
      if (end.position > 0) {
         super.visitLocalVariable(name, desc, signature, start, end, index);
      }
   }

   @Override
   public void visitTypeInsn(int opcode, String type)
   {
      argumentCapturing.registerTypeToCaptureIfApplicable(opcode, type);
      mv.visitTypeInsn(opcode, type);
   }

   @Override
   public void visitVarInsn(int opcode, int var)
   {
      argumentCapturing.registerAssignmentToCaptureVariableIfApplicable(this, opcode, var);
      mv.visitVarInsn(opcode, var);
   }

   @Override
   public void visitInsn(int opcode)
   {
      if (opcode == RETURN && callEndInvocations) {
         generateCallToActiveInvocationsMethod("endInvocations", "()V");
      }

      mv.visitInsn(opcode);
   }
}
