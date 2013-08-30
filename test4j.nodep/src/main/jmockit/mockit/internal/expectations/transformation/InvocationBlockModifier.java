/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.transformation;

import java.util.*;

import mockit.external.asm4.*;

import static mockit.external.asm4.Opcodes.*;
import static mockit.internal.util.TypeConversion.*;

final class InvocationBlockModifier extends MethodVisitor
{
   private static final String CLASS_DESC = Type.getInternalName(ActiveInvocations.class);

   // Input data:
   private final String owner;
   private final boolean callEndInvocations;

   // Helper fields that allow argument matchers to be moved to the correct positions of their
   // corresponding parameters:
   private final int[] matcherStacks;
   private int matchersToMove;
   private Type[] argTypes;

   // Helper field used to prevent NPEs from calls to certain "with" methods, when the associated
   // parameter is of a primitive type:
   private boolean nullOnTopOfStack;

   // Helper fields used to deal with "withCapture()" matchers:
   private int matchersFound;
   private List<Capture> captures;
   private boolean parameterForCapture;

   private final class Capture
   {
      final int opcode;
      private int parameterIndex;
      private boolean parameterIndexFixed;
      private final int var;

      Capture(int opcode, int var)
      {
         this.opcode = opcode;
         this.var = var;
         parameterIndex = matchersFound - 1;
      }

      /**
       * Responsible for performing the following steps:
       * 1. Get the argument value (an Object) for the last matched invocation.
       * 2. Typecheck and unbox the Object value to a primitive type, as needed.
       * 3. Store the converted value in its local variable.
       */
      void generateCodeToStoreCapturedValue()
      {
         mv.visitIntInsn(SIPUSH, parameterIndex);
         generateCallToActiveInvocationsMethod("matchedArgument", "(I)Ljava/lang/Object;");
         generateUnboxing(mv, argTypes[parameterIndex], opcode);
         mv.visitVarInsn(opcode, var);
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
   }

   private void addCapture(Capture capture)
   {
      if (captures == null) {
         captures = new ArrayList<Capture>();
      }

      captures.add(capture);
   }

   InvocationBlockModifier(MethodVisitor mw, String owner, boolean callEndInvocations)
   {
      super(mw);
      this.owner = owner;
      this.callEndInvocations = callEndInvocations;
      matcherStacks = new int[20];
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
      matcherStacks[matchersToMove++] = mv.stackSize2;
      matchersFound++;
   }

   @Override
   public void visitMethodInsn(int opcode, String owner, String name, String desc)
   {
      if (opcode == INVOKESTATIC && (isBoxing(owner, name, desc) || isAccessMethod(owner, name))) {
         // It's an invocation to a primitive boxing method or to a synthetic method for private access, just ignore it.
         mv.visitMethodInsn(INVOKESTATIC, owner, name, desc);
         return;
      }
      else if (opcode == INVOKEVIRTUAL && owner.equals(this.owner) && name.startsWith("with")) {
         mv.visitMethodInsn(INVOKEVIRTUAL, owner, name, desc);
         matcherStacks[matchersToMove++] = mv.stackSize2;
         nullOnTopOfStack = createPendingCaptureIfNeeded(name, desc);
         matchersFound++;
         return;
      }
      else if (isUnboxing(opcode, owner, desc)) {
         if (nullOnTopOfStack) {
            generateCodeToReplaceNullWithZeroOnTopOfStack(desc.charAt(2));
            nullOnTopOfStack = false;
         }
         else {
            mv.visitMethodInsn(opcode, owner, name, desc);
         }

         return;
      }
      else if (matchersToMove > 0) {
         argTypes = Type.getArgumentTypes(desc);
         int stackSize = mv.stackSize2;
         int stackAfter = stackSize - sumOfParameterSizes();

         if (stackAfter < matcherStacks[0]) {
            generateCallsToMoveArgMatchers(stackAfter);
            matchersToMove = 0;
         }
      }

      mv.visitMethodInsn(opcode, owner, name, desc);
      generateCallsToCaptureMatchedArgumentsIfPending();
      nullOnTopOfStack = false;
   }

   private boolean isAccessMethod(String owner, String name)
   {
      return !owner.equals(this.owner) && name.startsWith("access$");
   }

   private boolean createPendingCaptureIfNeeded(String name, String desc)
   {
      boolean withCapture = "withCapture".equals(name);
      parameterForCapture = withCapture && !desc.contains("List");
      return withCapture;
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

      for (Type argType : argTypes) {
         sum += argType.getSize();
      }

      return sum;
   }

   private void generateCallsToMoveArgMatchers(int initialStack)
   {
      int stack = initialStack;
      int nextMatcher = 0;
      int matcherStack = matcherStacks[0];

      for (int i = 0; i < argTypes.length && nextMatcher < matchersToMove; i++) {
         stack += argTypes[i].getSize();

         if (stack == matcherStack || stack == matcherStack + 1) {
            if (nextMatcher < i) {
               generateCallToMoveArgMatcher(nextMatcher, i);
               updateCaptureIfAny(nextMatcher, i);
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

   private void updateCaptureIfAny(int originalIndex, int newIndex)
   {
      if (captures != null) {
         for (int i = captures.size() - 1; i >= 0; i--) {
            Capture capture = captures.get(i);

            if (capture.fixParameterIndex(originalIndex, newIndex)) {
               break;
            }
         }
      }
   }

   private void generateCallsToCaptureMatchedArgumentsIfPending()
   {
      if (matchersToMove == 0) {
         if (captures != null) {
            for (Capture capture : captures) {
               capture.generateCodeToStoreCapturedValue();
            }

            captures = null;
         }

         matchersFound = 0;
      }
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
   public void visitVarInsn(int opcode, int var)
   {
      if (opcode >= ISTORE && opcode <= ASTORE && parameterForCapture) {
         addCapture(new Capture(opcode, var));
         parameterForCapture = false;
      }

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
