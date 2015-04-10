package org.test4j.junit.filter.finder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.test4j.junit.filter.acceptor.TestAcceptor;
import org.test4j.junit.filter.iterator.DirFileIterator;
import org.test4j.junit.filter.iterator.JarFileIterator;
import org.test4j.junit.filter.iterator.NonFileIterator;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.ResourceHelper;

/**
 * Utility class to find classes within the class path, both inside and outside
 * of jar files. Inner and anonymous classes are not being considered in the
 * first place. It's originally evolved out of ClassPathTestCollector in JUnit
 * 3.8.1
 */
public class ClasspathTestClazFinder implements TestClazFinder {

    private static final int   CLASS_SUFFIX_LENGTH = ".class".length();

    private final TestAcceptor tester;

    public ClasspathTestClazFinder(TestAcceptor tester) {
        this.tester = tester;
    }

    public List<Class<?>> find() {
        String classPath = System.getProperty("java.class.path");
        List<String> paths = this.splitClassPath(classPath);
        return findClassesInRoots(paths);
    }

    private List<String> splitClassPath(String classPath) {
        final String separator = System.getProperty("path.separator");
        return Arrays.asList(classPath.split(separator));
    }

    private List<Class<?>> findClassesInRoots(List<String> roots) {
        List<Class<?>> classes = new ArrayList<Class<?>>(100);
        for (String root : roots) {
            this.gatherClassesInRoot(new File(root), classes);
        }
        return classes;
    }

    private void gatherClassesInRoot(File classpathRoot, List<Class<?>> classes) {
        Iterable<String> relativeFilenames = new NonFileIterator<String>();
        if (ResourceHelper.isJarFile(classpathRoot)) {
            if (tester.searchInJars()) {
                try {
                    relativeFilenames = new JarFileIterator(classpathRoot);
                } catch (IOException e) {
                    MessageHelper.error(
                            "search test in jar[" + String.valueOf(classpathRoot) + "] error:" + e.getMessage(), e);
                }
            }
        } else {
            relativeFilenames = new DirFileIterator(classpathRoot);
        }
        for (String fileName : relativeFilenames) {
            this.addTestClazzIfAccepted(classes, fileName);
        }
    }

    /**
     * 往列表中增加测试类
     * 
     * @param classes
     * @param fileName
     */
    private void addTestClazzIfAccepted(List<Class<?>> classes, String fileName) {
        if (!ClazzHelper.isClassFile(fileName)) {
            return;
        }
        String clazzPath = fileName.substring(0, fileName.length() - CLASS_SUFFIX_LENGTH);
        String clazzName = ClazzHelper.replaceFileSeparators(clazzPath);
        if (!tester.isAcceptedByPatterns(clazzName)) {
            return;
        }
        if (!tester.acceptInnerClass() && ClazzHelper.isInnerClass(clazzName)) {
            return;
        }
        try {
            Class<?> clazz = Class.forName(clazzName);
            boolean isAcceptedByBaseType = tester.isCorrectClazType(clazz);
            if (!isAcceptedByBaseType) {
                return;
            }
            if (tester.isCorrectTestType(clazz)) {
                classes.add(clazz);
            }
        } catch (ClassNotFoundException cnfe) {
            // ignore not instantiable classes
        } catch (NoClassDefFoundError ncdfe) {
            // ignore not instantiable classes
        } catch (ExceptionInInitializerError ciie) {
            // ignore not instantiable classes
        } catch (UnsatisfiedLinkError ule) {
            // ignore not instantiable classes
        }
    }

    public TestAcceptor getTester() {
        return tester;
    }
}
