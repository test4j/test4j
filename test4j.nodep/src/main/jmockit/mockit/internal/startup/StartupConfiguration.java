/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.startup;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

final class StartupConfiguration
{
   private static final Iterable<String> NO_VALUES = Collections.emptyList();

   private final Properties config;
   final Iterable<String> externalTools;
   final Iterable<String> classesToBeStubbedOut;
   final Iterable<String> mockClasses;

   StartupConfiguration() throws IOException
   {
      config = new Properties();

      loadJMockitPropertiesFilesFromClasspath();
      loadJMockitPropertiesIntoSystemProperties();

      externalTools = getMultiValuedProperty("jmockit-tools");
      classesToBeStubbedOut = getMultiValuedProperty("jmockit-stubs");
      mockClasses = getMultiValuedProperty("jmockit-mocks");
   }

   private void loadJMockitPropertiesFilesFromClasspath() throws IOException
   {
      Enumeration<URL> allFiles = Thread.currentThread().getContextClassLoader().getResources("jmockit.properties");
      int numFiles = 0;

      while (allFiles.hasMoreElements()) {
         URL url = allFiles.nextElement();
         InputStream propertiesFile = url.openStream();

         if (numFiles == 0) {
            try { config.load(propertiesFile); } finally { propertiesFile.close(); }
         }
         else {
            Properties properties = new Properties();
            try { properties.load(propertiesFile); } finally { propertiesFile.close(); }
            addPropertyValues(properties);
         }

         numFiles++;
      }
   }

   private void addPropertyValues(Properties propertiesToAdd)
   {
      for (Entry<?, ?> propertyToAdd : propertiesToAdd.entrySet()) {
         Object key = propertyToAdd.getKey();
         String valueToAdd = (String) propertyToAdd.getValue();
         String existingValue = (String) config.get(key);
         String newValue;

         if (existingValue == null || existingValue.length() == 0) {
            newValue = valueToAdd;
         }
         else {
            newValue = existingValue + ' ' + valueToAdd;
         }

         config.put(key, newValue);
      }
   }

   private void loadJMockitPropertiesIntoSystemProperties()
   {
      Properties systemProperties = System.getProperties();

      for (Entry<?, ?> prop : config.entrySet()) {
         String key = (String) prop.getKey();
         String name = key.startsWith("jmockit-") ? key : "jmockit-" + key;

         if (!systemProperties.containsKey(name)) {
            systemProperties.put(name, prop.getValue());
         }
      }
   }

   private Iterable<String> getMultiValuedProperty(String key)
   {
      String commaOrSpaceSeparatedValues = System.getProperty(key);
      
      if (commaOrSpaceSeparatedValues == null) {
         return NO_VALUES;
      }

      List<String> allValues = Arrays.asList(commaOrSpaceSeparatedValues.split("\\s*,\\s*|\\s+"));
      Set<String> uniqueValues = new HashSet<String>(allValues);
      uniqueValues.remove("");

      return uniqueValues;
   }
}
