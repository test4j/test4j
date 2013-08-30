/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal;

import java.lang.reflect.*;

import static mockit.external.asm4.Opcodes.*;

import mockit.external.asm4.*;
import mockit.external.asm4.Type;
import mockit.internal.state.*;
import mockit.internal.util.*;

public class BaseClassModifier extends ClassVisitor
{
   private static final int ACCESS_MASK = 0xFFFF - ACC_ABSTRACT - ACC_NATIVE;
   private static final Type VOID_TYPE = Type.getType("Ljava/lang/Void;");

   protected final MethodVisitor methodAnnotationsVisitor = new MethodVisitor()
   {
      @Override
      public AnnotationVisitor visitAnnotation(String annotationDesc, boolean visible)
      {
         return mw.visitAnnotation(annotationDesc, visible);
      }

      @Override
      public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
      {
         registerParameterName(name);
      }

      @Override
      public AnnotationVisitor visitParameterAnnotation(int parameter, String annotationDesc, boolean visible)
      {
         return mw.visitParameterAnnotation(parameter, annotationDesc, visible);
      }
   };

   protected final void registerParameterName(String name)
   {
      ParameterNames.registerName(classDesc, methodName, methodDesc, name);
   }

   protected MethodVisitor mw;
   protected boolean useMockingBridge;
   protected String superClassName;
   private String classDesc;
   private String methodName;
   private String methodDesc;
   private boolean callToAnotherConstructorAlreadyDisregarded;

   protected BaseClassModifier(ClassReader classReader)
   {
      super(new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS));
   }

   protected final void setUseMockingBridge(ClassLoader classLoader)
   {
      useMockingBridge = classLoader == null;
   }

   @Override
   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
   {
      int modifiedVersion = version;
      int originalVersion = version & 0xFFFF;

      if (originalVersion < V1_5) {
         // LDC instructions (see MethodVisitor#visitLdcInsn) are more capable in JVMs with support for class files of
         // version 49 (Java 1.5) or newer, so we "upgrade" it to avoid a VerifyError:
         modifiedVersion = V1_5;
      }
      else if (originalVersion == V1_7) {
         // For some unknown reason the Java 7 JVM throws a VerifyError for the bytecode generated for dynamic mocking:
         modifiedVersion = V1_6;
      }

      super.visit(modifiedVersion, access, name, signature, superName, interfaces);
      superClassName = superName;
      classDesc = name;
   }

   /**
    * Just creates a new MethodWriter which will write out the method bytecode when visited.
    * <p/>
    * Removes any "abstract" or "native" modifiers for the modified version.
    */
   protected final void startModifiedMethodVersion(
      int access, String name, String desc, String signature, String[] exceptions)
   {
      //noinspection UnnecessarySuperQualifier
      mw = super.visitMethod(access & ACCESS_MASK, name, desc, signature, exceptions);

      methodName = name;
      methodDesc = desc;
      callToAnotherConstructorAlreadyDisregarded = false;

      if (Modifier.isNative(access)) {
         TestRun.mockFixture().addRedefinedClassWithNativeMethods(classDesc);
      }
   }

   protected final void generateCallToSuperConstructor()
   {
      mw.visitVarInsn(ALOAD, 0);

      String constructorDesc;

      if ("java/lang/Object".equals(superClassName)) {
         constructorDesc = "()V";
      }
      else {
         constructorDesc = SuperConstructorCollector.INSTANCE.findConstructor(classDesc, superClassName);

         for (Type paramType : Type.getArgumentTypes(constructorDesc)) {
            pushDefaultValueForType(paramType);
         }
      }

      mw.visitMethodInsn(INVOKESPECIAL, superClassName, "<init>", constructorDesc);
   }

   protected final void generateReturnWithObjectAtTopOfTheStack(String methodDesc)
   {
      Type returnType = Type.getReturnType(methodDesc);
      TypeConversion.generateCastFromObject(mw, returnType);
      mw.visitInsn(returnType.getOpcode(IRETURN));
   }

   protected final boolean generateCodeToPassThisOrNullIfStaticMethod(int access)
   {
      boolean isStatic = Modifier.isStatic(access);

      if (isStatic) {
         mw.visitInsn(ACONST_NULL);
      }
      else {
         mw.visitVarInsn(ALOAD, 0);
      }

      return isStatic;
   }

   protected final void generateCodeToCreateArrayOfObject(int arrayLength)
   {
      mw.visitIntInsn(BIPUSH, arrayLength);
      mw.visitTypeInsn(ANEWARRAY, "java/lang/Object");
   }

   protected final void generateCodeToPassMethodArgumentsAsVarargs(
      Type[] argTypes, int initialArrayIndex, int initialParameterIndex)
   {
      int i = initialArrayIndex;
      int j = initialParameterIndex;

      for (Type argType : argTypes) {
         mw.visitInsn(DUP);
         mw.visitIntInsn(BIPUSH, i++);
         mw.visitVarInsn(argType.getOpcode(ILOAD), j);
         TypeConversion.generateCastToObject(mw, argType);
         mw.visitInsn(AASTORE);
         j += argType.getSize();
      }
   }

   protected final void generateCodeToObtainInstanceOfMockingBridge(String mockingBridgeSubclassName)
   {
      mw.visitLdcInsn(mockingBridgeSubclassName);
      mw.visitInsn(ICONST_1);
      mw.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
      mw.visitMethodInsn(
         INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;");
      mw.visitLdcInsn("MB");
      mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;");
      mw.visitInsn(ACONST_NULL);
      mw.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Field", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
   }

   protected final void generateCodeToFillArrayElement(int arrayIndex, Object value)
   {
      mw.visitInsn(DUP);
      mw.visitIntInsn(BIPUSH, arrayIndex);

      if (value == null) {
         mw.visitInsn(ACONST_NULL);
      }
      else if (value instanceof Integer) {
         mw.visitIntInsn(SIPUSH, (Integer) value);
         mw.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
      }
      else if (value instanceof Boolean) {
         mw.visitInsn((Boolean) value ? ICONST_1 : ICONST_0);
         mw.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
      }
      else {
         mw.visitLdcInsn(value);
      }

      mw.visitInsn(AASTORE);
   }

   private void pushDefaultValueForType(Type type)
   {
      switch (type.getSort()) {
         case Type.VOID: break;
         case Type.BOOLEAN:
         case Type.CHAR:
         case Type.BYTE:
         case Type.SHORT:
         case Type.INT:    mw.visitInsn(ICONST_0); break;
         case Type.LONG:   mw.visitInsn(LCONST_0); break;
         case Type.FLOAT:  mw.visitInsn(FCONST_0); break;
         case Type.DOUBLE: mw.visitInsn(DCONST_0); break;
         case Type.ARRAY:  generateCreationOfEmptyArray(type); break;
         default:          mw.visitInsn(ACONST_NULL);
      }
   }

   private void generateCreationOfEmptyArray(Type arrayType)
   {
      int dimensions = arrayType.getDimensions();

      for (int dimension = 0; dimension < dimensions; dimension++) {
         mw.visitInsn(ICONST_0);
      }

      if (dimensions > 1) {
         mw.visitMultiANewArrayInsn(arrayType.getDescriptor(), dimensions);
         return;
      }

      Type elementType = arrayType.getElementType();
      int elementSort = elementType.getSort();

      if (elementSort == Type.OBJECT) {
         mw.visitTypeInsn(ANEWARRAY, elementType.getInternalName());
      }
      else {
         int typ = getArrayElementTypeCode(elementSort);
         mw.visitIntInsn(NEWARRAY, typ);
      }
   }

   private int getArrayElementTypeCode(int elementSort)
   {
      switch (elementSort) {
          case Type.BOOLEAN: return T_BOOLEAN;
          case Type.CHAR:    return T_CHAR;
          case Type.BYTE:    return T_BYTE;
          case Type.SHORT:   return T_SHORT;
          case Type.INT:     return T_INT;
          case Type.FLOAT:   return T_FLOAT;
          case Type.LONG:    return T_LONG;
          default:           return T_DOUBLE;
      }
   }

   protected final void generateCallToInvocationHandler()
   {
      mw.visitMethodInsn(
         INVOKEINTERFACE, "java/lang/reflect/InvocationHandler", "invoke",
         "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;");
   }

   protected final void generateDecisionBetweenReturningOrContinuingToRealImplementation(String desc)
   {
      mw.visitInsn(DUP);
      mw.visitLdcInsn(VOID_TYPE);

      Label startOfRealImplementation = new Label();
      mw.visitJumpInsn(IF_ACMPEQ, startOfRealImplementation);

      generateReturnWithObjectAtTopOfTheStack(desc);

      mw.visitLabel(startOfRealImplementation);
      mw.visitInsn(POP);
   }

   protected final void generateEmptyImplementation(String desc)
   {
      Type returnType = Type.getReturnType(desc);
      pushDefaultValueForType(returnType);
      mw.visitInsn(returnType.getOpcode(IRETURN));
      mw.visitMaxs(1, 0);
   }

   protected final void generateEmptyImplementation()
   {
      mw.visitInsn(RETURN);
      mw.visitMaxs(1, 0);
   }

   protected final void disregardIfInvokingAnotherConstructor(int opcode, String owner, String name, String desc)
   {
      if (
         callToAnotherConstructorAlreadyDisregarded ||
         opcode != INVOKESPECIAL || !"<init>".equals(name) ||
         !owner.equals(superClassName) && !owner.equals(classDesc)
      ) {
         mw.visitMethodInsn(opcode, owner, name, desc);
      }
      else {
         mw.visitInsn(POP);
         callToAnotherConstructorAlreadyDisregarded = true;
      }
   }
}
