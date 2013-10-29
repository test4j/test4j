/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.mockups;

import static java.lang.reflect.Modifier.*;
import static mockit.external.asm4.Opcodes.*;

import mockit.*;
import mockit.external.asm4.*;
import mockit.external.asm4.Type;
import mockit.internal.*;
import mockit.internal.startup.*;
import mockit.internal.state.*;

/**
 * Responsible for generating all necessary bytecode in the redefined (real) class.
 * Such code will redirect calls made on "real" methods to equivalent calls on the corresponding "mock" methods.
 * The original code won't be executed by the running JVM until the class redefinition is undone.
 * <p/>
 * Methods in the real class which have no corresponding mock method are unaffected.
 * <p/>
 * Any fields (static or not) in the real class remain untouched.
 */
final class MockupsModifier extends BaseClassModifier
{
   private static final String CLASS_WITH_STATE = "mockit/internal/state/TestRun";

   private final int mockInstanceIndex;
   private final boolean forStartupMock;
   private final MockMethods annotatedMocks;

   private final boolean useMockingBridgeForUpdatingMockState;
   private Class<?> mockedClass;

   // Helper fields:
   private MockMethods.MockMethod mockMethod;
   private int varIndex;

   // Output data:
   private boolean classWasModified;

   /**
    * Initializes the modifier for a given real/mock class pair.
    * <p/>
    * The mock instance provided will receive calls for any instance methods defined in the mock class.
    * Therefore, it needs to be later recovered by the modified bytecode inside the real method.
    * To enable this, the mock instance is added to the end of a global list made available through the
    * {@link mockit.internal.state.TestRun#getMock(int)} method.
    *
    * @param cr the class file reader for the real class
    * @param mock an instance of the mock class, never null
    * @param mockMethods contains the set of mock methods collected from the mock class; each mock method is identified
    * by a pair composed of "name" and "desc", where "name" is the method name, and "desc" is the JVM internal
    * description of the parameters; once the real class modification is complete this set will be empty, unless no
    * corresponding real method was found for any of its method identifiers
    */
   MockupsModifier(ClassReader cr, Class<?> realClass, MockUp<?> mock, MockMethods mockMethods, boolean forStartupMock)
   {
      super(cr);
      mockedClass = realClass;
      mockInstanceIndex = TestRun.getMockClasses().getMocks(forStartupMock).addMock(mock);
      annotatedMocks = mockMethods;
      this.forStartupMock = forStartupMock;

      ClassLoader classLoaderOfRealClass = realClass.getClassLoader();
      useMockingBridgeForUpdatingMockState = classLoaderOfRealClass == null;
      inferUseOfMockingBridge(classLoaderOfRealClass, mock);
   }

   private void inferUseOfMockingBridge(ClassLoader classLoaderOfRealClass, Object mock)
   {
      setUseMockingBridge(classLoaderOfRealClass);

      if (!useMockingBridge && !isPublic(mock.getClass().getModifiers())) {
         useMockingBridge = true;
      }
   }

   /**
    * If the specified method has a mock definition, then generates bytecode to redirect calls made to it to the mock
    * method. If it has no mock, does nothing.
    *
    * @param access not relevant
    * @param name together with desc, used to identity the method in given set of mock methods
    * @param signature not relevant
    * @param exceptions not relevant
    *
    * @return null if the method was redefined, otherwise a MethodWriter that writes out the visited method code without
    * changes
    */
   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
   {
      if ((access & ACC_SYNTHETIC) != 0) {
         return super.visitMethod(access, name, desc, signature, exceptions);
      }

      if (!hasMock(name, desc, signature)) {
         return super.visitMethod(access, name, desc, signature, exceptions);
      }

      validateMethodModifiers(access, name);

      startModifiedMethodVersion(access, name, desc, signature, exceptions);
      classWasModified = true;

      if (mockMethod.isForConstructor()) {
         generateCallToSuperConstructor();
      }

      if (isToPreserveRealImplementation(access)) {
         return getAlternativeMethodWriter(access);
      }

      generateCallToUpdateMockStateIfAny(access);
      generateCallToMockMethod(access);
      generateMethodReturn();
      mw.visitMaxs(1, 0); // dummy values, real ones are calculated by ASM
      return methodAnnotationsVisitor;
   }

   private boolean hasMock(String name, String desc, String signature)
   {
      String mockName = getCorrespondingMockName(name);
      mockMethod = annotatedMocks.containsMethod(mockName, desc, signature);
      return mockMethod != null;
   }

   private String getCorrespondingMockName(String name)
   {
      if ("<init>".equals(name)) {
         return "$init";
      }
      else if ("<clinit>".equals(name)) {
         return "$clinit";
      }

      return name;
   }

   private void validateMethodModifiers(int access, String name)
   {
      if (isAbstract(access)) {
         throw new IllegalArgumentException("Attempted to mock abstract method \"" + name + '\"');
      }
      else if (isNative(access)) {
         mockMethod.markAsNativeRealMethod();

         if (!Startup.isJava6OrLater()) {
            throw new IllegalArgumentException(
               "Mocking of native methods not supported under JDK 1.5: \"" + name + '\"');
         }
      }
   }

   private MethodVisitor getAlternativeMethodWriter(int mockedAccess)
   {
      generateDynamicCallToMock(mockedAccess);

      final boolean forConstructor = methodName.charAt(0) == '<';

      return new MethodVisitor(mw) {
         @Override
         public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
         {
            // Discards debug info with missing information, to avoid a ClassFormatError (happens with EMMA).
            if (end.position > 0) {
               mw.visitLocalVariable(name, desc, signature, start, end, index);
            }
         }

         @Override
         public void visitMethodInsn(int opcode, String owner, String name, String desc)
         {
            if (forConstructor) {
               disregardIfInvokingAnotherConstructor(opcode, owner, name, desc);
            }
            else {
               mw.visitMethodInsn(opcode, owner, name, desc);
            }
         }
      };
   }

   private boolean isToPreserveRealImplementation(int mockedAccess)
   {
      return !isNative(mockedAccess) && (isMockedSuperclass() || mockMethod.isDynamic());
   }

   private boolean isMockedSuperclass()
   {
      return mockedClass != null && mockedClass != mockMethod.getRealClass();
   }

   private void generateDynamicCallToMock(int mockedAccess)
   {
      if (!isStatic(mockedAccess) && !mockMethod.isForConstructor() && isMockedSuperclass()) {
         startOfRealImplementation = new Label();
         mw.visitVarInsn(ALOAD, 0);
         mw.visitTypeInsn(INSTANCEOF, Type.getInternalName(mockMethod.getRealClass()));
         mw.visitJumpInsn(IFEQ, startOfRealImplementation);
      }

      generateCallToUpdateMockStateIfAny(mockedAccess);

      if (mockMethod.isReentrant()) {
         generateCallToReentrantMockMethod(mockedAccess);
      }
      else if (mockMethod.isDynamic()) {
         generateCallToMockMethod(mockedAccess);
         generateDecisionBetweenReturningOrContinuingToRealImplementation();
      }
      else if (startOfRealImplementation != null) {
         generateCallToMockMethod(mockedAccess);
         generateMethodReturn();
         mw.visitLabel(startOfRealImplementation);
      }

      startOfRealImplementation = null;
   }

   private void generateCallToUpdateMockStateIfAny(int mockedAccess)
   {
      int mockStateIndex = mockMethod.getIndexForMockState();

      if (mockStateIndex >= 0) {
         if (useMockingBridgeForUpdatingMockState) {
            generateCallToControlMethodThroughMockingBridge(true, mockedAccess);
            mw.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
            mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
         }
         else {
            mw.visitLdcInsn(annotatedMocks.getMockClassInternalName());
            mw.visitIntInsn(SIPUSH, mockStateIndex);
            mw.visitMethodInsn(INVOKESTATIC, CLASS_WITH_STATE, "updateMockState", "(Ljava/lang/String;I)Z");
         }
      }
   }

   private void generateCallToReentrantMockMethod(int mockedAccess)
   {
      if (startOfRealImplementation == null) {
         startOfRealImplementation = new Label();
      }

      mw.visitJumpInsn(IFEQ, startOfRealImplementation);

      Label l0 = new Label();
      Label l1 = new Label();
      Label l2 = new Label();
      mw.visitTryCatchBlock(l0, l1, l2, null);

      Label l3 = new Label();
      mw.visitTryCatchBlock(l2, l3, l2, null);

      mw.visitLabel(l0);

      generateCallToMockMethod(mockedAccess);

      mw.visitLabel(l1);
      generateCallToExitReentrantMock();
      generateMethodReturn();
      mw.visitLabel(l2);
      mw.visitVarInsn(ASTORE, varIndex);
      mw.visitLabel(l3);
      generateCallToExitReentrantMock();
      mw.visitVarInsn(ALOAD, varIndex);
      mw.visitInsn(ATHROW);

      mw.visitLabel(startOfRealImplementation);
   }

   private void generateCallToControlMethodThroughMockingBridge(boolean enteringMethod, int mockAccess)
   {
      generateCodeToObtainInstanceOfMockingBridge(MockupBridge.MB);

      // First and second "invoke" arguments:
      generateCodeToPassThisOrNullIfStaticMethod(mockAccess);
      mw.visitInsn(ACONST_NULL);

      // Create array for call arguments (third "invoke" argument):
      generateCodeToCreateArrayOfObject(3);

      int i = 0;
      generateCodeToFillArrayElement(i++, enteringMethod);
      generateCodeToFillArrayElement(i++, annotatedMocks.getMockClassInternalName());
      generateCodeToFillArrayElement(i, mockMethod.getIndexForMockState());

      generateCallToInvocationHandler();
   }

   private void generateCallToMockMethod(int access)
   {
      if (mockMethod.isStatic) {
         generateStaticMethodCall(access);
      }
      else {
         generateInstanceMethodCall(access);
      }
   }

   private void generateStaticMethodCall(int access)
   {
      if (shouldUseMockingBridge()) {
         generateCallToMockMethodThroughMockingBridge(false, access);
      }
      else {
         generateMethodOrConstructorArguments(access);
         mw.visitMethodInsn(INVOKESTATIC, annotatedMocks.getMockClassInternalName(), mockMethod.name, mockMethod.desc);
      }
   }

   private boolean shouldUseMockingBridge() { return useMockingBridge || mockMethod.hasInvocationParameter; }

   private void generateCallToMockMethodThroughMockingBridge(boolean callingInstanceMethod, int access)
   {
      generateCodeToObtainInstanceOfMockingBridge(MockMethodBridge.MB);

      // First and second "invoke" arguments:
      boolean isStatic = generateCodeToPassThisOrNullIfStaticMethod(access);
      mw.visitInsn(ACONST_NULL);

      // Create array for call arguments (third "invoke" argument):
      Type[] argTypes = Type.getArgumentTypes(methodDesc);
      generateCodeToCreateArrayOfObject(7 + argTypes.length);

      int i = 0;
      generateCodeToFillArrayElement(i++, callingInstanceMethod);
      generateCodeToFillArrayElement(i++, annotatedMocks.getMockClassInternalName());
      generateCodeToFillArrayElement(i++, mockMethod.name);
      generateCodeToFillArrayElement(i++, mockMethod.desc);
      generateCodeToFillArrayElement(i++, mockMethod.getIndexForMockState());
      generateCodeToFillArrayElement(i++, mockInstanceIndex);
      generateCodeToFillArrayElement(i++, forStartupMock);

      generateCodeToPassMethodArgumentsAsVarargs(argTypes, i, isStatic ? 0 : 1);
      generateCallToInvocationHandler();
   }

   private void generateInstanceMethodCall(int access)
   {
      if (shouldUseMockingBridge()) {
         generateCallToMockMethodThroughMockingBridge(true, access);
         return;
      }

      generateGetMockCallWithMockInstanceIndex();
      generateMockInstanceMethodInvocationWithRealMethodArgs(access);
   }

   private void generateGetMockCallWithMockInstanceIndex()
   {
      mw.visitIntInsn(SIPUSH, mockInstanceIndex);
      String getterName = forStartupMock ? "getStartupMock" : "getMock";
      mw.visitMethodInsn(INVOKESTATIC, "mockit/internal/state/TestRun", getterName, "(I)Ljava/lang/Object;");
      mw.visitTypeInsn(CHECKCAST, annotatedMocks.getMockClassInternalName());
   }

   private void generateMockInstanceMethodInvocationWithRealMethodArgs(int access)
   {
      generateMethodOrConstructorArguments(access);
      mw.visitMethodInsn(INVOKEVIRTUAL, annotatedMocks.getMockClassInternalName(), mockMethod.name, mockMethod.desc);
   }

   private void generateMethodOrConstructorArguments(int access)
   {
      boolean hasInvokedInstance = (access & ACC_STATIC) == 0;
      varIndex = hasInvokedInstance ? 1 : 0;

      Type[] argTypes = Type.getArgumentTypes(mockMethod.desc);
      boolean forGenericMethod = mockMethod.isForGenericMethod();

      for (Type argType : argTypes) {
         int opcode = argType.getOpcode(ILOAD);
         mw.visitVarInsn(opcode, varIndex);

         if (forGenericMethod && argType.getSort() >= Type.ARRAY) {
            mw.visitTypeInsn(CHECKCAST, argType.getInternalName());
         }

         varIndex += argType.getSize();
      }
   }

   private void generateMethodReturn()
   {
      if (shouldUseMockingBridge()) {
         generateReturnWithObjectAtTopOfTheStack(methodDesc);
      }
      else {
         Type returnType = Type.getReturnType(methodDesc);
         mw.visitInsn(returnType.getOpcode(IRETURN));
      }
   }

   private void generateCallToExitReentrantMock()
   {
      if (useMockingBridgeForUpdatingMockState) {
         generateCallToControlMethodThroughMockingBridge(false, ACC_STATIC);
         mw.visitInsn(POP);
      }
      else {
         mw.visitLdcInsn(annotatedMocks.getMockClassInternalName());
         mw.visitIntInsn(SIPUSH, mockMethod.getIndexForMockState());
         mw.visitMethodInsn(INVOKESTATIC, CLASS_WITH_STATE, "exitReentrantMock", "(Ljava/lang/String;I)V");
      }
   }

   boolean wasModified() { return classWasModified; }
}
