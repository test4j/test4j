/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.*;

import static java.util.Arrays.*;
import static mockit.external.asm4.Opcodes.*;

import mockit.external.asm4.*;
import mockit.internal.*;
import mockit.internal.util.*;

public final class SubclassGenerationModifier extends MockedTypeModifier
{
   private static final int CLASS_ACCESS_MASK = 0xFFFF - ACC_ABSTRACT;
   private static final int CONSTRUCTOR_ACCESS_MASK = ACC_PUBLIC + ACC_PROTECTED;

   // Fixed initial state:
   private final MockingConfiguration mockingCfg;
   private final Class<?> abstractClass;
   private final String subclassName;
   private boolean copyConstructors;

   // Helper fields for mutable state:
   private String superClassOfSuperClass;
   private Set<String> superInterfaces;
   private final List<String> implementedMethods;

   public SubclassGenerationModifier(Type mockedType, ClassReader classReader, String subclassName)
   {
      this(null, mockedType, classReader, subclassName);
      copyConstructors = true;
   }

   SubclassGenerationModifier(
      MockingConfiguration mockingConfiguration, Type mockedType, ClassReader classReader, String subclassName)
   {
      super(classReader, mockedType);
      mockingCfg = mockingConfiguration;
      abstractClass = Utilities.getClassType(mockedType);
      this.subclassName = subclassName.replace('.', '/');
      implementedMethods = new ArrayList<String>();
      implementationSignature = 'L' + abstractClass.getName().replace('.', '/') + implementationSignature;
   }

   @Override
   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
   {
      int subclassAccess = access & CLASS_ACCESS_MASK | ACC_FINAL;
      super.visit(version, subclassAccess, subclassName, implementationSignature, name, null);
      superClassOfSuperClass = superName;
      superInterfaces = new HashSet<String>(asList(interfaces));
   }

   @Override
   public void visitInnerClass(String name, String outerName, String innerName, int access) {}

   @Override
   public void visitOuterClass(String owner, String name, String desc) {}

   @Override
   public void visitAttribute(Attribute attr) {}

   @Override
   public void visitSource(String file, String debug) {}

   @Override
   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) { return null; }

   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
   {
      if (copyConstructors && "<init>".equals(name)) {
         if ((access & CONSTRUCTOR_ACCESS_MASK) != 0) {
            generateConstructorDelegatingToSuper(desc, signature, exceptions);
         }
      }
      else {
         // Inherits from super-class when non-abstract.
         // Otherwise, creates implementation for abstract method with call to "recordOrReplay".
         generateImplementationIfAbstractMethod(superClassName, access, name, desc, signature, exceptions);
      }

      return null;
   }

   private void generateConstructorDelegatingToSuper(String desc, String signature, String[] exceptions)
   {
      mw = super.visitMethod(ACC_PUBLIC, "<init>", desc, signature, exceptions);
      mw.visitVarInsn(ALOAD, 0);
      int var = 1;

      for (mockit.external.asm4.Type paramType : mockit.external.asm4.Type.getArgumentTypes(desc)) {
         int loadOpcode = getLoadOpcodeForParameterType(paramType.getSort());
         mw.visitVarInsn(loadOpcode, var);
         var++;
      }

      mw.visitMethodInsn(INVOKESPECIAL, superClassName, "<init>", desc);
      generateEmptyImplementation();
   }

   private int getLoadOpcodeForParameterType(int paramType)
   {
      if (paramType <= mockit.external.asm4.Type.INT) {
         return ILOAD;
      }

      switch (paramType) {
         case mockit.external.asm4.Type.FLOAT:  return FLOAD;
         case mockit.external.asm4.Type.LONG:   return LLOAD;
         case mockit.external.asm4.Type.DOUBLE: return DLOAD;
         default: return ALOAD;
      }
   }

   private void generateImplementationIfAbstractMethod(
      String className, int access, String name, String desc, String signature, String[] exceptions)
   {
      if (!"<init>".equals(name)) {
         String methodNameAndDesc = name + desc;

         if (!implementedMethods.contains(methodNameAndDesc)) {
            if ((access & ACC_ABSTRACT) != 0) {
               generateMethodImplementation(className, access, name, desc, signature, exceptions);
            }

            implementedMethods.add(methodNameAndDesc);
         }
      }
   }

   @SuppressWarnings("AssignmentToMethodParameter")
   private void generateMethodImplementation(
      String className, int access, String name, String desc, String signature, String[] exceptions)
   {
      if (signature != null) {
         signature = genericTypeMap.resolveReturnType(signature);
      }

      mw = super.visitMethod(ACC_PUBLIC, name, desc, signature, exceptions);

      boolean noFiltersToMatch = mockingCfg == null;

      if (
         noFiltersToMatch && !isMethodFromObject(name, desc) ||
         !noFiltersToMatch && mockingCfg.matchesFilters(name, desc)
      ) {
         generateDirectCallToHandler(className, access, name, desc, signature, exceptions, 0);
         generateReturnWithObjectAtTopOfTheStack(desc);
         mw.visitMaxs(1, 0);
      }
      else {
         generateEmptyImplementation(desc);
      }
   }

   @Override
   public void visitEnd()
   {
      generateImplementationsForInheritedAbstractMethods(superClassOfSuperClass);

      while (!superInterfaces.isEmpty()) {
         String superInterface = superInterfaces.iterator().next();
         generateImplementationsForInterfaceMethods(superInterface);
         superInterfaces.remove(superInterface);
      }
   }

   private void generateImplementationsForInheritedAbstractMethods(String superName)
   {
      if (!"java/lang/Object".equals(superName)) {
         new MethodModifierForSuperclass(superName);
      }
   }

   private void generateImplementationsForInterfaceMethods(String superName)
   {
      if (!"java/lang/Object".equals(superName)) {
         new MethodModifierForImplementedInterface(superName);
      }
   }

   private class BaseMethodModifier extends ClassVisitor
   {
      final String typeName;

      BaseMethodModifier(String typeName)
      {
         this.typeName = typeName;
         ClassFile.visitClass(typeName, this);
      }

      @Override
      public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
      {
         return null;
      }
   }

   private final class MethodModifierForSuperclass extends BaseMethodModifier
   {
      String superName;

      MethodModifierForSuperclass(String className) { super(className); }

      @Override
      public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
      {
         this.superName = superName;

         if (interfaces != null) {
            superInterfaces.addAll(asList(interfaces));
         }
      }

      @Override
      public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
      {
         generateImplementationIfAbstractMethod(typeName, access, name, desc, signature, exceptions);
         return null;
      }

      @Override
      public void visitEnd()
      {
         generateImplementationsForInheritedAbstractMethods(superName);
      }
   }

   private final class MethodModifierForImplementedInterface extends BaseMethodModifier
   {
      MethodModifierForImplementedInterface(String interfaceName) { super(interfaceName); }

      @Override
      public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
      {
         superInterfaces.addAll(asList(interfaces));
      }

      @Override
      public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
      {
         generateImplementationForInterfaceMethodIfMissing(access, name, desc, signature, exceptions);
         return null;
      }

      private void generateImplementationForInterfaceMethodIfMissing(
         int access, String name, String desc, String signature, String[] exceptions)
      {
         String methodNameAndDesc = name + desc;

         if (!implementedMethods.contains(methodNameAndDesc)) {
            if (!hasMethodImplementation(name, desc)) {
               generateMethodImplementation(typeName, access, name, desc, signature, exceptions);
            }

            implementedMethods.add(methodNameAndDesc);
         }
      }

      private boolean hasMethodImplementation(String name, String desc)
      {
         Class<?>[] paramTypes = TypeDescriptor.getParameterTypes(desc);

         try {
            Method method = abstractClass.getMethod(name, paramTypes);
            return !method.getDeclaringClass().isInterface();
         }
         catch (NoSuchMethodException ignore) {
            return false;
         }
      }
   }
}
