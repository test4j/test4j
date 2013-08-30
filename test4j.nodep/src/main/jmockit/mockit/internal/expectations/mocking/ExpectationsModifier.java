/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.util.*;

import static java.lang.reflect.Modifier.*;

import static mockit.external.asm4.Opcodes.*;

import mockit.external.asm4.*;
import mockit.internal.filtering.*;
import mockit.internal.startup.*;

final class ExpectationsModifier extends MockedTypeModifier
{
   private static final int METHOD_ACCESS_MASK = ACC_SYNTHETIC + ACC_ABSTRACT;
   private static final Map<String, String> DEFAULT_FILTERS = new HashMap<String, String>() {{
      put("java/lang/Object", "<init> getClass hashCode");
      put("java/lang/String", "");
      put("java/lang/AbstractStringBuilder", "");
      put("java/lang/StringBuilder", "");
      put("java/lang/StringBuffer", "");
      put("java/lang/System", "arraycopy getProperties getSecurityManager");
      put("java/util/Hashtable", "get");
      put("java/lang/Throwable", "<init> fillInStackTrace");
      put("java/lang/Exception", "<init>");
      put("java/lang/Thread", "currentThread isInterrupted");
   }};

   private final MockingConfiguration mockingCfg;
   private String className;
   private String baseClassNameForCapturedInstanceMethods;
   private boolean stubOutClassInitialization;
   private boolean ignoreConstructors;
   private int executionMode;
   private boolean isProxy;
   private String defaultFilters;

   ExpectationsModifier(ClassLoader classLoader, ClassReader classReader, MockedType typeMetadata)
   {
      super(classReader);

      if (typeMetadata == null) {
         mockingCfg = null;
      }
      else {
         mockingCfg = typeMetadata.mockingCfg;
         stubOutClassInitialization = typeMetadata.isClassInitializationToBeStubbedOut();
      }

      setUseMockingBridge(classLoader);
   }

   public void setClassNameForCapturedInstanceMethods(String internalClassName)
   {
      baseClassNameForCapturedInstanceMethods = internalClassName;
   }

   public void useDynamicMocking(boolean methodsOnly)
   {
      ignoreConstructors = methodsOnly;
      executionMode = 1;
   }

   public void useDynamicMockingForInstanceMethods(MockedType typeMetadata)
   {
      ignoreConstructors = typeMetadata == null || typeMetadata.getMaxInstancesToCapture() <= 0;
      executionMode = 2;
   }

   void useDynamicMockingForSuperClass()
   {
      if (executionMode == 0) {
         executionMode = 3;
      }
   }

   @Override
   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
   {
      if ("java/lang/Class".equals(name)) {
         throw new IllegalArgumentException("Class " + name.replace('/', '.') + " is not mockable");
      }

      super.visit(version, access, name, signature, superName, interfaces);
      isProxy = "java/lang/reflect/Proxy".equals(superName);

      if (isProxy) {
         className = interfaces[0];
         defaultFilters = null;
      }
      else {
         className = name;
         defaultFilters = DEFAULT_FILTERS.get(name);
      }
   }

   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
   {
      boolean syntheticOrAbstractMethod = (access & METHOD_ACCESS_MASK) != 0;

      if (syntheticOrAbstractMethod || isProxy && isConstructorOrSystemMethodNotToBeMocked(name, desc)) {
         return unmodifiedBytecode(access, name, desc, signature, exceptions);
      }

      boolean noFiltersToMatch = mockingCfg == null;
      boolean matchesFilters = noFiltersToMatch || mockingCfg.matchesFilters(name, desc);

      if ("<clinit>".equals(name)) {
         return stubOutClassInitializationIfApplicable(access, noFiltersToMatch, matchesFilters);
      }
      else if (stubOutFinalizeMethod(access, name, desc)) {
         return null;
      }

      if (
         !matchesFilters ||
         isMethodFromCapturedClassNotToBeMocked(access) ||
         noFiltersToMatch && isMethodOrConstructorNotToBeMocked(access, name)
      ) {
         return unmodifiedBytecode(access, name, desc, signature, exceptions);
      }

      // Otherwise, replace original implementation with redirect to JMockit.
      validateModificationOfNativeMethod(access, name);
      startModifiedMethodVersion(access, name, desc, signature, exceptions);

      boolean visitingConstructor = "<init>".equals(name);

      if (visitingConstructor && superClassName != null) {
         generateCallToSuperConstructor();
      }

      String internalClassName = className;

      if (!visitingConstructor && baseClassNameForCapturedInstanceMethods != null) {
         internalClassName = baseClassNameForCapturedInstanceMethods;
      }

      int actualExecutionMode = determineAppropriateExecutionMode(access, visitingConstructor);

      if (useMockingBridge) {
         return
            generateCallToHandlerThroughMockingBridge(
               access, name, desc, signature, exceptions, internalClassName, actualExecutionMode);
      }

      generateDirectCallToHandler(internalClassName, access, name, desc, signature, exceptions, actualExecutionMode);

      if (actualExecutionMode > 0) {
         generateDecisionBetweenReturningOrContinuingToRealImplementation(desc);

         // Constructors of non-JRE classes can't be modified (unless running with "-noverify") in a way that
         // "super(...)/this(...)" get called twice, so we disregard such calls when copying the original bytecode.
         return visitingConstructor ? new DynamicConstructorModifier() : copyOriginalImplementationCode(access, desc);
      }

      generateReturnWithObjectAtTopOfTheStack(desc);
      mw.visitMaxs(1, 0);
      return methodAnnotationsVisitor;
   }

   private MethodVisitor unmodifiedBytecode(int access, String name, String desc, String signature, String[] exceptions)
   {
      return super.visitMethod(access, name, desc, signature, exceptions);
   }

   private boolean isConstructorOrSystemMethodNotToBeMocked(String name, String desc)
   {
      return
         "<init>".equals(name) || isMethodFromObject(name, desc) ||
         "annotationType".equals(name) && "()Ljava/lang/Class;".equals(desc);
   }

   private MethodVisitor stubOutClassInitializationIfApplicable(int access, boolean noFilters, boolean matchesFilters)
   {
      mw = super.visitMethod(access, "<clinit>", "()V", null, null);

      if (!noFilters && matchesFilters || noFilters && stubOutClassInitialization) {
         generateEmptyImplementation();
         return null;
      }

      return mw;
   }

   private boolean stubOutFinalizeMethod(int access, String name, String desc)
   {
      if ("finalize".equals(name) && "()V".equals(desc)) {
         mw = super.visitMethod(access, name, desc, null, null);
         generateEmptyImplementation();
         return true;
      }
      
      return false;
   }
   
   private boolean isMethodFromCapturedClassNotToBeMocked(int access)
   {
      return baseClassNameForCapturedInstanceMethods != null && (isStatic(access) || isPrivate(access));
   }

   private boolean isMethodOrConstructorNotToBeMocked(int access, String name)
   {
      return
         isConstructorToBeIgnored(name) ||
         isStaticMethodToBeIgnored(access) ||
         isNativeMethodForDynamicMocking(access) ||
         useMockingBridge && isPrivate(access) && isNative(access) ||
         defaultFilters != null && (defaultFilters.length() == 0 || defaultFilters.contains(name));
   }

   private boolean isConstructorToBeIgnored(String name) { return ignoreConstructors && "<init>".equals(name); }
   private boolean isStaticMethodToBeIgnored(int access) { return executionMode == 2 && isStatic(access); }
   private boolean isNativeMethodForDynamicMocking(int access) { return executionMode > 0 && isNative(access); }

   private void validateModificationOfNativeMethod(int access, String name)
   {
      if (isNative(access) && !Startup.isJava6OrLater()) {
         throw new IllegalArgumentException(
            "Mocking of native methods not supported under JDK 1.5; please filter out method \"" +
            name + "\", or run under JDK 1.6+");
      }
   }

   private int determineAppropriateExecutionMode(int access, boolean visitingConstructor)
   {
      if (executionMode == 2) {
         if (visitingConstructor) {
            return ignoreConstructors ? 0 : 1;
         }
         else if (isStatic(access)) {
            return 0;
         }
      }

      return executionMode;
   }

   private MethodVisitor generateCallToHandlerThroughMockingBridge(
      int access, String name, String desc, String genericSignature, String[] exceptions, String internalClassName,
      int executionMode)
   {
      generateCodeToObtainInstanceOfMockingBridge(MockedBridge.class.getName());

      // First and second "invoke" arguments:
      boolean isStatic = generateCodeToPassThisOrNullIfStaticMethod(access);
      mw.visitInsn(ACONST_NULL);

      // Create array for call arguments (third "invoke" argument):
      Type[] argTypes = Type.getArgumentTypes(desc);
      generateCodeToCreateArrayOfObject(7 + argTypes.length);

      int i = 0;
      generateCodeToFillArrayElement(i++, access);
      generateCodeToFillArrayElement(i++, internalClassName);
      generateCodeToFillArrayElement(i++, name);
      generateCodeToFillArrayElement(i++, desc);
      generateCodeToFillArrayElement(i++, genericSignature);
      generateCodeToFillArrayElement(i++, getListOfExceptionsAsSingleString(exceptions));
      generateCodeToFillArrayElement(i++, executionMode);

      generateCodeToPassMethodArgumentsAsVarargs(argTypes, i, isStatic ? 0 : 1);
      generateCallToInvocationHandler();

      generateDecisionBetweenReturningOrContinuingToRealImplementation(desc);

      // Copies the entire original implementation even for a constructor, in which case the complete bytecode inside
      // the constructor fails the strict verification activated by "-Xfuture". However, this is necessary to allow the
      // full execution of a JRE constructor when the call was not meant to be mocked.
      return copyOriginalImplementationCode(access, desc);
   }

   private MethodVisitor copyOriginalImplementationCode(int access, String desc)
   {
      if (isNative(access)) {
         generateEmptyImplementation(desc);
         return methodAnnotationsVisitor;
      }

      return new DynamicModifier();
   }

   private class DynamicModifier extends MethodVisitor
   {
      DynamicModifier() { super(mw); }

      @Override
      public final void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int idx)
      {
         registerParameterName(name);

         // For some reason, the start position for "this" gets displaced by bytecode inserted at the beginning,
         // in a method modified by the EMMA tool. If not treated, this causes a ClassFormatError.
         if (end.position > 0 && start.position > end.position) {
            start.position = end.position;
         }

         // Ignores any local variable with required information missing, to avoid a VerifyError/ClassFormatError.
         if (start.position > 0 && end.position > 0) {
            super.visitLocalVariable(name, desc, signature, start, end, idx);
         }
      }
   }

   private final class DynamicConstructorModifier extends DynamicModifier
   {
      @Override
      public void visitMethodInsn(int opcode, String owner, String name, String desc)
      {
         disregardIfInvokingAnotherConstructor(opcode, owner, name, desc);
      }
   }
}
