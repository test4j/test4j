/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.reflect.*;
import java.lang.reflect.Type;

import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.util.*;

import static mockit.external.asm4.Opcodes.*;

class MockedTypeModifier extends BaseClassModifier
{
   protected GenericTypeReflection genericTypeMap;
   protected String implementationSignature;

   protected MockedTypeModifier(ClassReader classReader, Type mockedType)
   {
      super(classReader);

      if (mockedType != null) {
         Class<?> mockedClass = Utilities.getClassType(mockedType);
         genericTypeMap = new GenericTypeReflection(mockedClass, mockedType);
         implementationSignature = getGenericClassSignature(mockedType);
      }
   }

   protected final String getGenericClassSignature(Type mockedType)
   {
      StringBuilder signature = new StringBuilder(100);

      if (mockedType instanceof ParameterizedType) {
         ParameterizedType parameterizedType = (ParameterizedType) mockedType;
         Type[] typeArguments = parameterizedType.getActualTypeArguments();

         if (typeArguments.length > 0) {
            signature.append('<');

            for (Type typeArg : typeArguments) {
               if (typeArg instanceof Class<?>) {
                  Class<?> classArg = (Class<?>) typeArg;
                  signature.append('L').append(classArg.getName().replace('.', '/')).append(';');
               }
               else {
                  signature.append('*');
               }
            }

            signature.append('>');
         }
      }

      signature.append(';');
      return signature.toString();
   }

   protected final void generateDirectCallToHandler(
      String className, int access, String name, String desc, String genericSignature, String[] exceptions,
      int executionMode)
   {
      // First argument: the mock instance, if any.
      boolean isStatic = generateCodeToPassThisOrNullIfStaticMethod(access);

      // Second argument: method access flags.
      mw.visitLdcInsn(access);

      // Third argument: class name.
      mw.visitLdcInsn(className);

      // Fourth argument: method signature.
      mw.visitLdcInsn(name + desc);

      // Fifth argument: generic signature, or null if none.
      generateInstructionToLoadNullableString(genericSignature);

      // Sixth argument: checked exceptions thrown, or null if none.
      String exceptionsStr = getListOfExceptionsAsSingleString(exceptions);
      generateInstructionToLoadNullableString(exceptionsStr);

      // Seventh argument: indicate regular or special modes of execution.
      mw.visitLdcInsn(executionMode);
      
      // Sixth argument: call arguments.
      mockit.external.asm4.Type[] argTypes = mockit.external.asm4.Type.getArgumentTypes(desc);
      generateCodeToPassMethodArgumentsAsVarargs(isStatic, argTypes);

      mw.visitMethodInsn(
         INVOKESTATIC, "mockit/internal/expectations/RecordAndReplayExecution", "recordOrReplay",
         "(Ljava/lang/Object;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I" +
         "[Ljava/lang/Object;)Ljava/lang/Object;");
   }

   private void generateInstructionToLoadNullableString(String text)
   {
      if (text == null) {
         mw.visitInsn(ACONST_NULL);
      }
      else {
         mw.visitLdcInsn(text);
      }
   }

   private void generateCodeToPassMethodArgumentsAsVarargs(boolean isStatic, mockit.external.asm4.Type[] argTypes)
   {
      generateCodeToCreateArrayOfObject(argTypes.length);
      generateCodeToPassMethodArgumentsAsVarargs(argTypes, 0, isStatic ? 0 : 1);
   }

   protected final String getListOfExceptionsAsSingleString(String[] exceptions)
   {
      if (exceptions == null) {
         return null;
      }
      else if (exceptions.length == 1) {
         return exceptions[0];
      }

      StringBuilder buf = new StringBuilder(200);
      String sep = "";

      for (String exception : exceptions) {
         buf.append(sep).append(exception);
         sep = " ";
      }

      return buf.toString();
   }

   protected final boolean isMethodFromObject(String name, String desc)
   {
      return
         "equals".equals(name)   && "(Ljava/lang/Object;)Z".equals(desc) ||
         "hashCode".equals(name) && "()I".equals(desc) ||
         "toString".equals(name) && "()Ljava/lang/String;".equals(desc) ||
         "finalize".equals(name) && "()V".equals(desc);
   }
}
