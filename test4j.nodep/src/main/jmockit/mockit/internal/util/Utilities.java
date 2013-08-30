/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.util;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.reflect.Modifier.*;

import mockit.internal.state.*;

/**
 * Miscellaneous utility methods which don't fit into any other class, most of them related to the
 * use of Reflection.
 */
@SuppressWarnings({"unchecked", "ClassWithTooManyMethods", "OverlyComplexClass"})
public final class Utilities
{
   public static final String GENERATED_SUBCLASS_PREFIX = "$Subclass_";
   public static final String GENERATED_IMPLCLASS_PREFIX = "$Impl_";
   public static final Object[] NO_ARGS = {};

   private static final Class<?>[] PRIMITIVE_TYPES = {
      null, boolean.class, char.class, byte.class, short.class, int.class, float.class, long.class, double.class
   };
   private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = new HashMap<Class<?>, Class<?>>()
   {{
      put(Boolean.class, boolean.class);
      put(Character.class, char.class);
      put(Byte.class, byte.class);
      put(Short.class, short.class);
      put(Integer.class, int.class);
      put(Float.class, float.class);
      put(Long.class, long.class);
      put(Double.class, double.class);
   }};
   public static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<Class<?>, Class<?>>()
   {{
      put(boolean.class, Boolean.class);
      put(char.class, Character.class);
      put(byte.class, Byte.class);
      put(short.class, Short.class);
      put(int.class, Integer.class);
      put(float.class, Float.class);
      put(long.class, Long.class);
      put(double.class, Double.class);
   }};
   private static final Class<?>[] NO_PARAMETERS = new Class<?>[0];

   private static final Map<String, Class<?>> LOADED_CLASSES = new ConcurrentHashMap<String, Class<?>>();

   private Utilities() {}

   public static void registerLoadedClass(Class<?> aClass)
   {
      LOADED_CLASSES.put(aClass.getName(), aClass);
   }

   public static <T> Class<T> loadClassByInternalName(String internalClassName)
   {
      return loadClass(internalClassName.replace('/', '.'));
   }

   public static <T> Class<T> loadClass(String className)
   {
      Class<?> loadedClass = LOADED_CLASSES.get(className);

      try {
         if (loadedClass == null) {
            loadedClass = loadClass(Thread.currentThread().getContextClassLoader(), className);

            if (loadedClass == null) {
               Class<?> testClass = TestRun.getCurrentTestClass();
               loadedClass = testClass == null ? null : loadClass(testClass.getClassLoader(), className);

               if (loadedClass == null) {
                  loadedClass = loadClass(Utilities.class.getClassLoader(), className);

                  if (loadedClass == null) {
                     throw new IllegalArgumentException("No class with name \"" + className + "\" found");
                  }
               }
            }
         }
      }
      catch (LinkageError e) {
         e.printStackTrace();
         throw e;
      }

      return (Class<T>) loadedClass;
   }

   private static Class<?> loadClass(ClassLoader loader, String className)
   {
      try { return Class.forName(className, true, loader); } catch (ClassNotFoundException ignore) { return null; }
   }

   public static <T> T newInstance(Class<T> aClass)
   {
      return newInstance(aClass, NO_PARAMETERS);
   }

   public static <T> T newInstance(Class<T> aClass, Class<?>[] parameterTypes, Object... initArgs)
   {
      Constructor<?> constructor = findSpecifiedConstructor(aClass, parameterTypes);
      return (T) invoke(constructor, initArgs);
   }

   private static Constructor<?> findSpecifiedConstructor(Class<?> theClass, Class<?>[] paramTypes)
   {
      for (Constructor<?> declaredConstructor : theClass.getDeclaredConstructors()) {
         Class<?>[] declaredParameterTypes = declaredConstructor.getParameterTypes();
         int firstRealParameter = indexOfFirstRealParameter(declaredParameterTypes, paramTypes);

         if (
            firstRealParameter >= 0 &&
            matchesParameterTypes(declaredParameterTypes, paramTypes, firstRealParameter)
         ) {
            return declaredConstructor;
         }
      }

      String paramTypesDesc = getParameterTypesDescription(paramTypes);

      throw new IllegalArgumentException(
         "Specified constructor not found: " + theClass.getSimpleName() + paramTypesDesc);
   }

   private static String getParameterTypesDescription(Type[] paramTypes)
   {
      String paramTypesDesc = Arrays.asList(paramTypes).toString();
      return paramTypesDesc.replace("class ", "").replace('[', '(').replace(']', ')');
   }

   public static <T> T newInstance(String className, Class<?>[] parameterTypes, Object... initArgs)
   {
      Class<T> theClass = loadClass(className);
      return newInstance(theClass, parameterTypes, initArgs);
   }

   public static <T> T newInstance(Class<? extends T> aClass, Object... nonNullArgs)
   {
      Class<?>[] argTypes = getArgumentTypesFromArgumentValues(nonNullArgs);
      Constructor<?> constructor = findCompatibleConstructor(aClass, argTypes);
      return (T) invoke(constructor, nonNullArgs);
   }

   private static Constructor<?> findCompatibleConstructor(Class<?> theClass, Class<?>[] argTypes)
   {
      Constructor<?> found = null;
      Class<?>[] foundParameters = null;

      for (Constructor<?> declaredConstructor : theClass.getDeclaredConstructors()) {
         Class<?>[] declaredParamTypes = declaredConstructor.getParameterTypes();
         int firstRealParameter = indexOfFirstRealParameter(declaredParamTypes, argTypes);

         if (
            firstRealParameter >= 0 &&
            (matchesParameterTypes(declaredParamTypes, argTypes, firstRealParameter) ||
             acceptsArgumentTypes(declaredParamTypes, argTypes, firstRealParameter)) &&
            (found == null || hasMoreSpecificTypes(declaredParamTypes, foundParameters))
         ) {
            found = declaredConstructor;
            foundParameters = declaredParamTypes;
         }
      }

      if (found != null) {
         return found;
      }

      String argTypesDesc = getParameterTypesDescription(argTypes);
      throw new IllegalArgumentException("No compatible constructor found: " + theClass.getSimpleName() + argTypesDesc);
   }

   public static <T> T newInstance(String className, Object... nonNullArgs)
   {
      Class<?>[] argTypes = getArgumentTypesFromArgumentValues(nonNullArgs);
      Class<T> theClass = loadClass(className);
      Constructor<?> constructor = findCompatibleConstructor(theClass, argTypes);
      return (T) invoke(constructor, nonNullArgs);
   }

   private static Class<?>[] getArgumentTypesFromArgumentValues(Object... args)
   {
      if (args.length == 0) {
         return NO_PARAMETERS;
      }

      Class<?>[] argTypes = new Class<?>[args.length];

      for (int i = 0; i < args.length; i++) {
         argTypes[i] = getArgumentTypeFromArgumentValue(i, args);
      }

      return argTypes;
   }

   private static Class<?> getArgumentTypeFromArgumentValue(int i, Object[] args)
   {
      Object arg = args[i];

      if (arg == null) {
         throw new IllegalArgumentException("Invalid null value passed as argument " + i);
      }

      Class<?> argType;

      if (arg instanceof Class<?>) {
         argType = (Class<?>) arg;
         args[i] = null;
      }
      else {
         argType = getMockedClass(arg);
      }

      return argType;
   }

   public static boolean isGeneratedImplementationClass(Class<?> mockedType)
   {
      return Proxy.isProxyClass(mockedType) || isGeneratedImplementationClass(mockedType.getName());
   }

   public static Class<?> getMockedClassOrInterfaceType(Class<?> aClass)
   {
      if (isGeneratedImplementationClass(aClass)) {
         // Assumes that the proxy class implements a single interface.
         return aClass.getInterfaces()[0];
      }

      return getMockedClassType(aClass);
   }

   public static Class<?> getMockedClassType(Class<?> aClass)
   {
      return isGeneratedSubclass(aClass.getName()) ? aClass.getSuperclass() : aClass;
   }

   public static Class<?> getMockedClass(Object mock)
   {
      return getMockedClassOrInterfaceType(mock.getClass());
   }

   private static boolean isGeneratedSubclass(String className)
   {
      return className.contains(GENERATED_SUBCLASS_PREFIX);
   }

   public static String getNameForGeneratedClass(Class<?> aClass, String suffix)
   {
      String prefix = aClass.isInterface() ? GENERATED_IMPLCLASS_PREFIX : GENERATED_SUBCLASS_PREFIX;

      if (isPublic(aClass.getModifiers())) {
         return prefix + suffix;
      }

      Package testPackage = aClass.getPackage();
      String packageName = testPackage == null ? "" : testPackage.getName() + '.';

      return packageName + prefix + suffix;
   }

   private static boolean isGeneratedImplementationClass(String className)
   {
      return className.contains(GENERATED_IMPLCLASS_PREFIX);
   }

   public static boolean isGeneratedClass(String className)
   {
      return isGeneratedSubclass(className) || isGeneratedImplementationClass(className);
   }

   public static <T> T newInnerInstance(Class<? extends T> innerClass, Object outerInstance, Object... nonNullArgs)
   {
      Object[] initArgs = argumentsWithExtraFirstValue(nonNullArgs, outerInstance);
      return newInstance(innerClass, initArgs);
   }

   public static Object[] argumentsWithExtraFirstValue(Object[] args, Object firstValue)
   {
      Object[] args2 = new Object[1 + args.length];
      args2[0] = firstValue;
      System.arraycopy(args, 0, args2, 1, args.length);
      return args2;
   }

   public static <T> T newInnerInstance(String innerClassName, Object outerInstance, Object... nonNullArgs)
   {
      String className = outerInstance.getClass().getName() + '$' + innerClassName;
      Class<T> innerClass = loadClass(className);

      return newInnerInstance(innerClass, outerInstance, nonNullArgs);
   }

   public static <T> T invoke(Class<?> theClass, Object targetInstance, String methodName, Object... methodArgs)
   {
      boolean staticMethod = targetInstance == null;
      Class<?>[] argTypes = getArgumentTypesFromArgumentValues(methodArgs);
      Method method = staticMethod ?
         findCompatibleStaticMethod(theClass, methodName, argTypes) :
         findCompatibleMethod(theClass, methodName, argTypes);

      if (staticMethod && !isStatic(method.getModifiers())) {
         throw new IllegalArgumentException(
            "Attempted to invoke non-static method without an instance to invoke it on");
      }

      T result = (T) invoke(targetInstance, method, methodArgs);
      return result;
   }

   private static Method findCompatibleStaticMethod(Class<?> theClass, String methodName, Class<?>[] argTypes)
   {
      Method methodFound = findCompatibleMethodInClass(theClass, methodName, argTypes);

      if (methodFound != null) {
         return methodFound;
      }

      String argTypesDesc = getParameterTypesDescription(argTypes);
      throw new IllegalArgumentException("No compatible static method found: " + methodName + argTypesDesc);
   }

   public static Method findCompatibleMethod(Class<?> theClass, String methodName, Class<?>[] argTypes)
   {
      Method methodFound = null;

      while (true) {
         Method compatibleMethod = findCompatibleMethodInClass(theClass, methodName, argTypes);

         if (
            compatibleMethod != null &&
            (methodFound == null ||
             hasMoreSpecificTypes(compatibleMethod.getParameterTypes(), methodFound.getParameterTypes()))
         ) {
            methodFound = compatibleMethod;
         }

         Class<?> superClass = theClass.getSuperclass();

         if (superClass == null || superClass == Object.class) {
            break;
         }

         //noinspection AssignmentToMethodParameter
         theClass = superClass;
      }

      if (methodFound != null) {
         return methodFound;
      }

      String argTypesDesc = getParameterTypesDescription(argTypes);
      throw new IllegalArgumentException("No compatible method found: " + methodName + argTypesDesc);
   }

   public static Method findCompatibleMethod(Class<?> theClass, String methodName, Object[] args)
   {
      Class<?>[] argTypes = getArgumentTypesFromArgumentValues(args);
      return findCompatibleMethod(theClass, methodName, argTypes);
   }

   private static Method findCompatibleMethodInClass(Class<?> theClass, String methodName, Class<?>[] argTypes)
   {
      Method found = null;
      Class<?>[] foundParamTypes = null;

      for (Method declaredMethod : theClass.getDeclaredMethods()) {
         if (declaredMethod.getName().equals(methodName)) {
            Class<?>[] declaredParamTypes = declaredMethod.getParameterTypes();
            int firstRealParameter = indexOfFirstRealParameter(declaredParamTypes, argTypes);

            if (
               firstRealParameter >= 0 &&
               (matchesParameterTypes(declaredParamTypes, argTypes, firstRealParameter) ||
                acceptsArgumentTypes(declaredParamTypes, argTypes, firstRealParameter)) &&
               (found == null || hasMoreSpecificTypes(declaredParamTypes, foundParamTypes))
            ) {
               found = declaredMethod;
               foundParamTypes = declaredParamTypes;
            }
         }
      }

      return found;
   }

   private static boolean hasMoreSpecificTypes(Class<?>[] currentTypes, Class<?>[] previousTypes)
   {
      for (int i = 0; i < currentTypes.length; i++) {
         Class<?> current = wrappedIfPrimitive(currentTypes[i]);
         Class<?> previous = wrappedIfPrimitive(previousTypes[i]);

         if (current != previous && previous.isAssignableFrom(current)) {
            return true;
         }
      }

      return false;
   }

   private static Class<?> wrappedIfPrimitive(Class<?> parameterType)
   {
      return parameterType.isPrimitive() ? PRIMITIVE_TO_WRAPPER.get(parameterType) : parameterType;
   }

   private static boolean acceptsArgumentTypes(Class<?>[] paramTypes, Class<?>[] argTypes, int firstParameter)
   {
      for (int i = firstParameter; i < paramTypes.length; i++) {
         Class<?> parType = paramTypes[i];
         Class<?> argType = argTypes[i - firstParameter];

         if (isSameTypeIgnoringAutoBoxing(parType, argType) || parType.isAssignableFrom(argType)) {
            // OK, move to next parameter.
         }
         else {
            return false;
         }
      }

      return true;
   }

   public static Method findNonPrivateHandlerMethod(Object handler)
   {
      Class<?> handlerClass = handler.getClass();
      Method nonPrivateMethod;

      do {
         nonPrivateMethod = findNonPrivateHandlerMethod(handlerClass);

         if (nonPrivateMethod != null) {
            break;
         }

         handlerClass = handlerClass.getSuperclass();
      }
      while (handlerClass != null && handlerClass != Object.class);

      if (nonPrivateMethod == null) {
         throw new IllegalArgumentException("No non-private invocation handler method found");
      }

      return nonPrivateMethod;
   }

   private static Method findNonPrivateHandlerMethod(Class<?> handlerClass)
   {
      Method[] declaredMethods = handlerClass.getDeclaredMethods();
      Method found = null;

      for (Method declaredMethod : declaredMethods) {
         if (!isPrivate(declaredMethod.getModifiers())) {
            if (found != null) {
               throw new IllegalArgumentException("More than one non-private invocation handler method found");
            }

            found = declaredMethod;
         }
      }

      return found;
   }

   public static <T> T invoke(
      Class<?> theClass, Object targetInstance, String methodName, Class<?>[] paramTypes, Object... methodArgs)
   {
      Method method = findSpecifiedMethod(theClass, methodName, paramTypes);
      T result = (T) invoke(targetInstance, method, methodArgs);
      return result;
   }

   public static <T> T invoke(Object targetInstance, Method method, Object... methodArgs)
   {
      ensureThatMemberIsAccessible(method);

      try {
         return (T) method.invoke(targetInstance, methodArgs);
      }
      catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      }
      catch (IllegalArgumentException e) {
         StackTrace.filterStackTrace(e);
         throw new IllegalArgumentException("Failure to invoke method: " + method, e);
      }
      catch (InvocationTargetException e) {
         Throwable cause = e.getCause();

         if (cause instanceof Error) {
            throw (Error) cause;
         }
         else if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
         }
         else {
            throwCheckedException((Exception) cause);
            return null;
         }
      }
   }

   private static void ensureThatMemberIsAccessible(AccessibleObject classMember)
   {
      if (!classMember.isAccessible()) {
         classMember.setAccessible(true);
      }
   }

   private static Method findSpecifiedMethod(Class<?> theClass, String methodName, Class<?>[] paramTypes)
   {
      for (Method declaredMethod : theClass.getDeclaredMethods()) {
         if (declaredMethod.getName().equals(methodName)) {
            Class<?>[] declaredParameterTypes = declaredMethod.getParameterTypes();
            int firstRealParameter = indexOfFirstRealParameter(declaredParameterTypes, paramTypes);

            if (
               firstRealParameter >= 0 &&
               matchesParameterTypes(declaredMethod.getParameterTypes(), paramTypes, firstRealParameter)
            ) {
               return declaredMethod;
            }
         }
      }

      Class<?> superClass = theClass.getSuperclass();

      if (superClass != null && superClass != Object.class) {
         return findSpecifiedMethod(superClass, methodName, paramTypes);
      }

      String paramTypesDesc = getParameterTypesDescription(paramTypes);

      throw new IllegalArgumentException("Specified method not found: " + methodName + paramTypesDesc);
   }

   private static int indexOfFirstRealParameter(Class<?>[] mockParameterTypes, Class<?>[] realParameterTypes)
   {
      int extraParameters = mockParameterTypes.length - realParameterTypes.length;

      if (extraParameters == 1) {
         //noinspection UnnecessaryFullyQualifiedName
         return mockParameterTypes[0] == mockit.Invocation.class ? 1 : -1;
      }
      else if (extraParameters != 0) {
         return -1;
      }

      return 0;
   }

   private static boolean matchesParameterTypes(Class<?>[] declaredTypes, Class<?>[] specifiedTypes, int firstParameter)
   {
      for (int i = firstParameter; i < declaredTypes.length; i++) {
         Class<?> declaredType = declaredTypes[i];
         Class<?> specifiedType = specifiedTypes[i - firstParameter];

         if (isSameTypeIgnoringAutoBoxing(declaredType, specifiedType)) {
            // OK, move to next parameter.
         }
         else {
            return false;
         }
      }

      return true;
   }

   private static boolean isSameTypeIgnoringAutoBoxing(Class<?> firstType, Class<?> secondType)
   {
      return
         firstType == secondType ||
         firstType.isPrimitive() && isWrapperOfPrimitiveType(firstType, secondType) ||
         secondType.isPrimitive() && isWrapperOfPrimitiveType(secondType, firstType);
   }

   private static boolean isWrapperOfPrimitiveType(Class<?> primitiveType, Class<?> otherType)
   {
      return primitiveType == WRAPPER_TO_PRIMITIVE.get(otherType);
   }

   public static boolean isWrapperOfPrimitiveType(Class<?> type)
   {
      return WRAPPER_TO_PRIMITIVE.containsKey(type);
   }

   public static Method findPublicVoidMethod(Class<?> aClass, String methodName)
   {
      for (Method method : aClass.getDeclaredMethods()) {
         if (
            isPublic(method.getModifiers()) && method.getReturnType() == void.class &&
            methodName.equals(method.getName())
         ) {
            return method;
         }
      }

      return null;
   }

   private static Object[] getDefaultParameterValues(Constructor<?> constructor)
   {
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      int numParameters = parameterTypes.length;
      Object[] defaultArgs = new Object[numParameters];

      for (int i = 0; i < numParameters; i++) {
         Class<?> paramType = parameterTypes[i];
         defaultArgs[i] = DefaultValues.computeForType(paramType);
      }

      return defaultArgs;
   }

   public static Object invoke(Constructor<?> constructor, Object... initArgs)
   {
      ensureThatMemberIsAccessible(constructor);

      Object[] args = initArgs != null? initArgs : getDefaultParameterValues(constructor);

      try {
         return constructor.newInstance(args);
      }
      catch (InstantiationException e) {
         throw new RuntimeException(e);
      }
      catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      }
      catch (InvocationTargetException e) {
         Throwable cause = e.getCause();

         if (cause instanceof Error) {
            throw (Error) cause;
         }
         else if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
         }
         else {
            throwCheckedException((Exception) cause);
            return null;
         }
      }
   }

   public static Object getField(Class<?> theClass, String fieldName, Object targetObject)
   {
      Field field = getDeclaredField(theClass, fieldName);
      return getFieldValue(field, targetObject);
   }

   public static Field getDeclaredField(Class<?> theClass, String fieldName)
   {
      try {
         return theClass.getDeclaredField(fieldName);
      }
      catch (NoSuchFieldException ignore) {
         Class<?> superClass = theClass.getSuperclass();

         if (superClass != null) {
            return getDeclaredField(superClass, fieldName);
         }

         //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
         throw new IllegalArgumentException("No instance field of name \"" + fieldName + "\" found in " + theClass);
      }
   }

   public static <T> T getField(Class<?> theClass, Class<T> fieldType, Object targetObject)
   {
      Field field = getDeclaredField(theClass, fieldType, targetObject != null, false);
      return (T) getFieldValue(field, targetObject);
   }

   public static <T> T getField(Class<?> theClass, Type fieldType, Object targetObject)
   {
      Field field = getDeclaredField(theClass, fieldType, targetObject != null, false);
      return (T) getFieldValue(field, targetObject);
   }

   private static Field getDeclaredField(
      Class<?> theClass, Type desiredType, boolean instanceField, boolean forAssignment)
   {
      Field found = getDeclaredFieldInSingleClass(theClass, desiredType, instanceField, forAssignment);

      if (found == null) {
         Class<?> superClass = theClass.getSuperclass();

         if (superClass != null && superClass != Object.class) {
            return getDeclaredField(superClass, desiredType, instanceField, forAssignment);
         }
         
         throw new IllegalArgumentException(
            (instanceField ? "Instance" : "Static") + " field of " + desiredType + " not found in " + theClass);
      }

      return found;
   }

   private static Field getDeclaredFieldInSingleClass(
      Class<?> theClass, Type desiredType, boolean instanceField, boolean forAssignment)
   {
      Field found = null;

      for (Field field : theClass.getDeclaredFields()) {
         if (!field.isSynthetic()) {
            Type fieldType = field.getGenericType();

            if (
               instanceField != isStatic(field.getModifiers()) &&
               isCompatibleFieldType(fieldType, desiredType, forAssignment)
            ) {
               if (found != null) {
                  String message =
                     errorMessageForMoreThanOneFieldFound(desiredType, instanceField, forAssignment, found, field);

                  throw new IllegalArgumentException(message);
               }

               found = field;
            }
         }
      }

      return found;
   }

   private static String errorMessageForMoreThanOneFieldFound(
      Type desiredFieldType, boolean instanceField, boolean forAssignment, Field firstField, Field secondField)
   {
      StringBuilder message = new StringBuilder("More than one ");
      message.append(instanceField ? "instance" : "static").append(" field ");

      message.append(forAssignment ? "to which a value of " : "from which a value of ");
      message.append(desiredFieldType);
      message.append(forAssignment ? " can be assigned" : " can be read");

      message.append(" exists in ").append(secondField.getDeclaringClass()).append(": ");
      message.append(firstField.getName()).append(", ").append(secondField.getName());

      return message.toString();
   }

   private static boolean isCompatibleFieldType(Type fieldType, Type desiredType, boolean forAssignment)
   {
      Class<?> fieldClass = getClassType(fieldType);
      Class<?> desiredClass = getClassType(desiredType);

      if (isSameTypeIgnoringAutoBoxing(desiredClass, fieldClass)) {
         return true;
      }
      else if (forAssignment) {
         return fieldClass.isAssignableFrom(desiredClass);
      }

      return desiredClass.isAssignableFrom(fieldClass) || fieldClass.isAssignableFrom(desiredClass);
   }

   public static Class<?> getClassType(Type declaredType)
   {
      if (declaredType instanceof ParameterizedType) {
         return (Class<?>) ((ParameterizedType) declaredType).getRawType();
      }

      return (Class<?>) declaredType;
   }

   public static <E> E newEmptyProxy(ClassLoader loader, Class<E> interfaceToBeProxied)
   {
      Class<?>[] interfaces = loader == null ?
         new Class<?>[] {interfaceToBeProxied} : new Class<?>[] {interfaceToBeProxied, EmptyProxy.class};

      //noinspection unchecked
      return (E) Proxy.newProxyInstance(loader, interfaces, MockInvocationHandler.INSTANCE);
   }

   public static <E> E newEmptyProxy(ClassLoader loader, Type... interfacesToBeProxied)
   {
      List<Class<?>> interfaces = new ArrayList<Class<?>>();

      for (Type type : interfacesToBeProxied) {
         addInterface(interfaces, type);
      }

      if (loader == null) {
         //noinspection AssignmentToMethodParameter
         loader = interfaces.get(0).getClassLoader();
      }

      if (loader == EmptyProxy.class.getClassLoader()) {
         interfaces.add(EmptyProxy.class);
      }

      Class<?>[] interfacesArray = interfaces.toArray(new Class<?>[interfaces.size()]);

      //noinspection unchecked
      return (E) Proxy.newProxyInstance(loader, interfacesArray, MockInvocationHandler.INSTANCE);
   }

   private static void addInterface(List<Class<?>> interfaces, Type type)
   {
      if (type instanceof Class<?>) {
         interfaces.add((Class<?>) type);
      }
      else if (type instanceof ParameterizedType) {
         ParameterizedType paramType = (ParameterizedType) type;
         interfaces.add((Class<?>) paramType.getRawType());
      }
      else if (type instanceof TypeVariable) {
         TypeVariable<?> typeVar = (TypeVariable<?>) type;
         addBoundInterfaces(interfaces, typeVar.getBounds());
      }
   }

   private static void addBoundInterfaces(List<Class<?>> interfaces, Type[] bounds)
   {
      for (Type bound : bounds) {
         addInterface(interfaces, bound);
      }
   }

   public static Object getFieldValue(Field field, Object targetObject)
   {
      ensureThatMemberIsAccessible(field);

      try {
         return field.get(targetObject);
      }
      catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      }
   }

   public static Field setField(Class<?> theClass, Object targetObject, String fieldName, Object fieldValue)
   {
      Field field =
         fieldName == null ?
            getDeclaredField(theClass, fieldValue.getClass(), targetObject != null, true) :
            getDeclaredField(theClass, fieldName);
      
      setFieldValue(field, targetObject, fieldValue);
      return field;
   }

   public static void setFieldValue(Field field, Object targetObject, Object value)
   {
      try {
         if (isStatic(field.getModifiers()) && isFinal(field.getModifiers())) {
            setStaticFinalField(field, value);
         }
         else {
            ensureThatMemberIsAccessible(field);
            field.set(targetObject, value);
         }
      }
      catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      }
   }

   private static void setStaticFinalField(Field field, Object value) throws IllegalAccessException
   {
      Field modifiersField;

      try {
         modifiersField = Field.class.getDeclaredField("modifiers");
      }
      catch (NoSuchFieldException e) {
         throw new RuntimeException(e);
      }

      modifiersField.setAccessible(true);
      int nonFinalModifiers = modifiersField.getInt(field) - FINAL;
      modifiersField.setInt(field, nonFinalModifiers);

      //noinspection UnnecessaryFullyQualifiedName,UseOfSunClasses
      sun.reflect.FieldAccessor accessor =
         sun.reflect.ReflectionFactory.getReflectionFactory().newFieldAccessor(field, false);
      accessor.set(null, value);
   }

   public static Class<?>[] getParameterTypes(String mockDesc)
   {
      mockit.external.asm4.Type[] paramTypes = mockit.external.asm4.Type.getArgumentTypes(mockDesc);

      if (paramTypes.length == 0) {
         return NO_PARAMETERS;
      }

      Class<?>[] paramClasses = new Class<?>[paramTypes.length];

      for (int i = 0; i < paramTypes.length; i++) {
         paramClasses[i] = getClassForType(paramTypes[i]);
      }

      return paramClasses;
   }

   public static Class<?> getReturnType(String mockDesc)
   {
      mockit.external.asm4.Type returnType = mockit.external.asm4.Type.getReturnType(mockDesc);
      return getClassForType(returnType);
   }

   public static Class<?> getClassForType(mockit.external.asm4.Type type)
   {
      int elementSort = type.getSort();

      if (elementSort < PRIMITIVE_TYPES.length) {
         return PRIMITIVE_TYPES[elementSort];
      }

      String className =
         elementSort == mockit.external.asm4.Type.ARRAY ? type.getDescriptor().replace('/', '.') : type.getClassName();

      return loadClass(className);
   }

   public static void throwCheckedException(Exception exceptionToThrow)
   {
      synchronized (ThrowOfCheckedException.class) {
         ThrowOfCheckedException.exceptionToThrow = exceptionToThrow;
         newInstanceUsingDefaultConstructor(ThrowOfCheckedException.class);
      }
   }

   public static <T> T newInstanceUsingDefaultConstructor(Class<T> aClass)
   {
      try {
         //noinspection ClassNewInstance
         return aClass.newInstance();
      }
      catch (InstantiationException ie) {
         throw new RuntimeException(ie);
      }
      catch (IllegalAccessException ignore) {
         return newInstance(aClass);
      }
   }

   private static final class ThrowOfCheckedException
   {
      static Exception exceptionToThrow;

      ThrowOfCheckedException() throws Exception { throw exceptionToThrow; }
   }

   /**
    * This method was created to work around an issue in the standard {@link Class#isAnonymousClass()} method, which
    * causes a sibling nested class to be loaded when called on a nested class. If that sibling nested class is not in
    * the classpath, a {@code ClassNotFoundException} would result.
    * <p/>
    * This method checks only the given class name, never causing any other classes to be loaded.
    */
   public static boolean isAnonymousClass(Class<?> aClass)
   {
      return isAnonymousClass(aClass.getName());
   }

   public static boolean isAnonymousClass(String className)
   {
      int p = className.lastIndexOf('$');
      return isAllNumeric(className, p);
   }

   public static boolean isAllNumeric(String className, int positionJustBefore)
   {
      if (positionJustBefore <= 0) {
         return false;
      }

      int nextPos = positionJustBefore + 1;
      int n = className.length();

      while (nextPos < n) {
         char c = className.charAt(nextPos);
         if (c < '0' || c > '9') return false;
         nextPos++;
      }

      return true;
   }

   public static String objectIdentity(Object obj)
   {
      return obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
   }

   public static Object evaluateObjectOverride(Object obj, String methodNameAndDesc, Object[] args)
   {
      if ("equals(Ljava/lang/Object;)Z".equals(methodNameAndDesc)) {
         return obj == args[0];
      }
      else if ("hashCode()I".equals(methodNameAndDesc)) {
         return System.identityHashCode(obj);
      }
      else if ("toString()Ljava/lang/String;".equals(methodNameAndDesc)) {
         return objectIdentity(obj);
      }
      else if (
         obj instanceof Comparable<?> && args.length == 1 &&
         methodNameAndDesc.startsWith("compareTo(L") && methodNameAndDesc.endsWith(";)I")
      ) {
         Object arg = args[0];

         if (obj == arg) {
            return 0;
         }

         return System.identityHashCode(obj) > System.identityHashCode(arg) ? 1 : -1;
      }

      return null;
   }

   public static  <A extends Annotation> A getAnnotation(Annotation[] annotations, Class<A> annotation)
   {
      for (Annotation paramAnnotation : annotations) {
         if (paramAnnotation.annotationType() == annotation) {
            //noinspection unchecked
            return (A) paramAnnotation;
         }
      }

      return null;
   }

   public static boolean containsReference(List<?> references, Object toBeFound)
   {
      for (Object reference : references) {
         if (reference == toBeFound) {
            return true;
         }
      }

      return false;
   }
}
