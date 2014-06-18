/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.transformation;

import java.util.*;

import static mockit.external.asm4.Opcodes.*;

import mockit.internal.expectations.transformation.InvocationBlockModifier.Capture;

final class ArgumentCapturing
{
   boolean justAfterWithCaptureInvocation;
   private List<Capture> captures;
   private boolean parameterForCapture;
   private String capturedTypeDesc;

   void registerCapturingMatcherIfApplicable(String methodName, String methodDesc)
   {
      justAfterWithCaptureInvocation = "withCapture".equals(methodName);
      parameterForCapture = justAfterWithCaptureInvocation && !methodDesc.contains("List");
   }

   void registerTypeToCaptureIfApplicable(int opcode, String type)
   {
      if (opcode == CHECKCAST && parameterForCapture) {
         capturedTypeDesc = type;
      }
   }

   void registerAssignmentToCaptureVariableIfApplicable(InvocationBlockModifier modifier, int opcode, int var)
   {
      if (opcode >= ISTORE && opcode <= ASTORE && parameterForCapture) {
         Capture capture = modifier.createCapture(opcode, var, capturedTypeDesc);
         addCapture(capture);
         parameterForCapture = false;
         capturedTypeDesc = null;
      }
   }

   private void addCapture(Capture capture)
   {
      if (captures == null) {
         captures = new ArrayList<Capture>();
      }

      captures.add(capture);
   }

   void updateCaptureIfAny(int originalIndex, int newIndex)
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

   void generateCallsToSetArgumentTypesToCaptureIfAny()
   {
      if (captures != null) {
         for (Capture capture : captures) {
            capture.generateCallToSetArgumentTypeIfNeeded();
         }
      }
   }

   void generateCallsToCaptureMatchedArgumentsIfPending()
   {
      if (captures != null) {
         for (Capture capture : captures) {
            capture.generateCodeToStoreCapturedValue();
         }

         captures = null;
      }
   }
}
