package org.jtester.module.jmockit.utility;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import mockit.Mocked;
import mockit.NonStrict;

import org.jtester.module.core.utility.MessageHelper;

public class JMockitModuleHelper {
    /**
     * 判断 @SpringBeanByName @SpringBeanByType 定义的字段是否和 @NonStrict @Mocked 定义在一起<br>
     * 这种定义在逻辑意义上是无效的<br>
     * 如果碰到这种情况，抛出运行时异常
     * 
     * @param field
     */
    public static void doesSpringBeanFieldIllegal(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (NonStrict.class.isInstance(annotation)) {
                throw new RuntimeException(
                        "@SpringBeanByName/@SpringBeanByType can't define with @NonStrict together. you may be hope to use @SpringBeanFrom @NonStrict.");
            }
            if (Mocked.class.isInstance(annotation)) {
                throw new RuntimeException(
                        "@SpringBeanByName/@SpringBeanByType can't define with @Mocked together. you may be hope to use @SpringBeanFrom @Mocked.");
            }
        }
    }

    private static final Pattern JAR_REGEX      = Pattern.compile(".*jtester\\.nodep[-.\\d]*.jar");
    private static final String  Nodep_Jar_Path = "jtester.nodep.jar";

    private static String        hitsMessage    = null;

    /**
     * 返回 -javaagent:.../jtester-nodep.xxx.jar 提示
     * 
     * @return
     */
    public static String getJMockitJavaagentHit() {
        if (hitsMessage == null) {
            String jarPath = getJMockitJarPath();
            StringBuffer buff = new StringBuffer();
            buff.append("If JMockit isn't initialized. Please check that your JVM is started with command option:");
            buff.append("-javaagent:" + jarPath);
            hitsMessage = buff.toString();
            MessageHelper
                    .warn("If JMockit isn't initialized. Please check that your JVM is started with command option:");
            System.err.println("\t -javaagent:" + jarPath);
        }
        return hitsMessage;
    }

    private static String getJMockitJarPath() {
        String javaClazzPaths = System.getProperty("java.class.path");
        if (javaClazzPaths == null) {
            return Nodep_Jar_Path;
        }
        String[] classPath = javaClazzPaths.split(File.pathSeparator);
        if (classPath == null) {
            return Nodep_Jar_Path;
        }
        for (String cpEntry : classPath) {
            if (JAR_REGEX.matcher(cpEntry).matches()) {
                return cpEntry;
            }
        }
        return Nodep_Jar_Path;
    }
}
