/*
 * Copyright (c) 2006-2013 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.state;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;

import mockit.internal.*;
import mockit.internal.expectations.mocking.*;
import mockit.internal.util.*;

/**
 * Holds data about redefined real classes and their corresponding mock classes (if any), and provides methods to
 * add/remove such state both from this instance and from other state holders with associated data.
 */
public final class MockFixture
{
   /**
    * Similar to {@code redefinedClasses}, but for classes modified by a {@code ClassFileTransformer} such as the
    * {@code CaptureTransformer}, and containing the pre-transform bytecode instead of the modified one.
    */
   private final Map<String, byte[]> transformedClasses = new HashMap<String, byte[]>(2);

   /**
    * Real classes currently redefined in the running JVM and their current (modified) bytecodes.
    * <p/>
    * The keys in the map allow each redefined real class to be later restored to a previous definition.
    * <p/>
    * The modified bytecode arrays in the map allow a new redefinition to be made on top of the current redefinition
    * (in the case of the Mockups API), or to restore the class to a previous definition (provided the map is copied
    * between redefinitions of the same class).
    */
   private final Map<Class<?>, byte[]> redefinedClasses = new IdentityHashMap<Class<?>, byte[]>(8);

   /**
    * Subset of all currently redefined classes which contain one or more native methods.
    * <p/>
    * This is needed because in order to restore such methods it is necessary (for some classes) to re-register them
    * with the JVM.
    *
    * @see #reregisterNativeMethodsForRestoredClass(Class)
    */
   private final Set<String> redefinedClassesWithNativeMethods = new HashSet<String>();

   /**
    * Maps redefined real classes to the internal name of the corresponding mock classes, when it's the case.
    * <p/>
    * This allows any global state associated to a mock class to be discarded when the corresponding real class is
    * later restored to its original definition.
    */
   private final Map<Class<?>, String> realClassesToMockClasses = new IdentityHashMap<Class<?>, String>(8);

   private final List<Class<?>> mockedClasses = new ArrayList<Class<?>>();
   private final Map<Class<?>, InstanceFactory> mockedTypesAndInstances =
      new IdentityHashMap<Class<?>, InstanceFactory>();

   // Methods to add/remove transformed/redefined classes /////////////////////////////////////////////////////////////

   public void addTransformedClass(String className, byte[] pretransformClassfile)
   {
      transformedClasses.put(className, pretransformClassfile);
   }

   public void addRedefinedClass(String mockClassInternalName, Class<?> redefinedClass, byte[] modifiedClassfile)
   {
      if (mockClassInternalName != null) {
         String previousNames = realClassesToMockClasses.put(redefinedClass, mockClassInternalName);

         if (previousNames != null) {
            realClassesToMockClasses.put(redefinedClass, previousNames + ' ' + mockClassInternalName);
         }
      }

      addRedefinedClass(redefinedClass, modifiedClassfile);
   }

   public void addRedefinedClass(Class<?> redefinedClass, byte[] modifiedClassfile)
   {
      redefinedClasses.put(redefinedClass, modifiedClassfile);
   }

   public void registerMockedClass(Class<?> mockedType)
   {
      if (!mockedClasses.contains(mockedType) && !GeneratedClasses.isGeneratedImplementationClass(mockedType)) {
         mockedClasses.add(mockedType);
      }
   }

   public boolean isInstanceOfMockedClass(Object mockedInstance)
   {
      Class<?> mockedClass = mockedInstance.getClass();
      int n = mockedClasses.size();

      for (int i = 0; i < n; i++) {
         Class<?> mockedType = mockedClasses.get(i);

         if (mockedType == mockedClass || mockedType.isAssignableFrom(mockedClass)) {
            return true;
         }
      }

      return false;
   }

   public void registerInstanceFactoryForMockedType(Class<?> mockedType, InstanceFactory mockedInstanceFactory)
   {
      registerMockedClass(mockedType);
      mockedTypesAndInstances.put(mockedType, mockedInstanceFactory);
   }

   public InstanceFactory findInstanceFactory(Class<?> mockedType)
   {
      if (mockedType.isInterface() || Modifier.isAbstract(mockedType.getModifiers())) {
         for (Entry<Class<?>, InstanceFactory> entry : mockedTypesAndInstances.entrySet()) {
            Class<?> baseType = GeneratedClasses.getMockedClassOrInterfaceType(entry.getKey());

            if (baseType == mockedType) {
               return entry.getValue();
            }
         }

         return null;
      }

      return mockedTypesAndInstances.get(mockedType);
   }

   public void restoreAndRemoveRedefinedClasses(Set<Class<?>> desiredClasses)
   {
      Set<Class<?>> classesToRestore = desiredClasses == null ? redefinedClasses.keySet() : desiredClasses;
      RedefinitionEngine redefinitionEngine = new RedefinitionEngine();

      for (Class<?> redefinedClass : classesToRestore) {
         redefinitionEngine.restoreOriginalDefinition(redefinedClass);
         restoreDefinition(redefinedClass);
         discardStateForCorrespondingMockClassIfAny(redefinedClass);
      }

      if (desiredClasses == null) {
         redefinedClasses.clear();
      }
      else {
         redefinedClasses.keySet().removeAll(desiredClasses);
      }
   }

   private void restoreDefinition(Class<?> redefinedClass)
   {
      if (redefinedClassesWithNativeMethods.contains(redefinedClass.getName())) {
         reregisterNativeMethodsForRestoredClass(redefinedClass);
      }

      mockedTypesAndInstances.remove(redefinedClass);
      mockedClasses.remove(redefinedClass);
   }

   private void discardStateForCorrespondingMockClassIfAny(Class<?> redefinedClass)
   {
      String mockClassesInternalNames = realClassesToMockClasses.remove(redefinedClass);
      TestRun.getMockClasses().getMockStates().removeClassState(redefinedClass, mockClassesInternalNames);
   }

   void restoreTransformedClasses(Set<String> previousTransformedClasses)
   {
      if (!transformedClasses.isEmpty()) {
         Set<String> classesToRestore;

         if (previousTransformedClasses.isEmpty()) {
            classesToRestore = transformedClasses.keySet();
         }
         else {
            classesToRestore = getTransformedClasses();
            classesToRestore.removeAll(previousTransformedClasses);
         }

         if (!classesToRestore.isEmpty()) {
            restoreAndRemoveTransformedClasses(classesToRestore);
         }
      }
   }

   private void restoreAndRemoveTransformedClasses(Set<String> classesToRestore)
   {
      RedefinitionEngine redefinitionEngine = new RedefinitionEngine();

      for (String transformedClassName : classesToRestore) {
         byte[] definitionToRestore = transformedClasses.get(transformedClassName);
         redefinitionEngine.restoreToDefinition(transformedClassName, definitionToRestore);
      }

      transformedClasses.keySet().removeAll(classesToRestore);
   }

   void restoreRedefinedClasses(Map<?, byte[]> previousDefinitions)
   {
      RedefinitionEngine redefinitionEngine = new RedefinitionEngine();
      Iterator<Entry<Class<?>, byte[]>> itr = redefinedClasses.entrySet().iterator();

      while (itr.hasNext()) {
         Entry<Class<?>, byte[]> entry = itr.next();
         Class<?> redefinedClass = entry.getKey();
         byte[] currentDefinition = entry.getValue();
         byte[] previousDefinition = previousDefinitions.get(redefinedClass);

         if (currentDefinition != previousDefinition) {
            redefinitionEngine.restoreDefinition(redefinedClass, previousDefinition);

            if (previousDefinition == null) {
               restoreDefinition(redefinedClass);
               discardStateForCorrespondingMockClassIfAny(redefinedClass);
               itr.remove();
            }
            else {
               entry.setValue(previousDefinition);
            }
         }
      }
   }

   // Methods that deal with redefined native methods /////////////////////////////////////////////////////////////////

   public void addRedefinedClassWithNativeMethods(String redefinedClassInternalName)
   {
      redefinedClassesWithNativeMethods.add(redefinedClassInternalName.replace('/', '.'));
   }

   private void reregisterNativeMethodsForRestoredClass(Class<?> realClass)
   {
      Method registerNatives = null;

      try {
         registerNatives = realClass.getDeclaredMethod("registerNatives");
      }
      catch (NoSuchMethodException ignore) {
         try {
            registerNatives = realClass.getDeclaredMethod("initIDs");
         }
         catch (NoSuchMethodException alsoIgnore) {
            // OK
         }
      }

      if (registerNatives != null) {
         try {
            registerNatives.setAccessible(true);
            registerNatives.invoke(null);
         }
         catch (IllegalAccessException ignore) {
            // Won't happen.
         }
         catch (InvocationTargetException ignore) {
            // Shouldn't happen either.
         }
      }

      // OK, although another solution will be required for this particular class if it requires
      // natives to be explicitly registered again (not all do, such as java.lang.Float).
   }

   // Getter methods for the maps of transformed/redefined classes ////////////////////////////////////////////////////

   public Set<String> getTransformedClasses() { return new HashSet<String>(transformedClasses.keySet()); }
   public Map<Class<?>, byte[]> getRedefinedClasses() { return new HashMap<Class<?>, byte[]>(redefinedClasses); }

   public byte[] getRedefinedClassfile(Class<?> redefinedClass) { return redefinedClasses.get(redefinedClass); }

   public boolean containsRedefinedClass(Class<?> redefinedClass)
   {
      return redefinedClasses.containsKey(redefinedClass);
   }
}
