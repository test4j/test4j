package org.jtester.junit.filter.acceptor;

import java.lang.reflect.*;
import java.util.*;

import junit.framework.TestCase;

import org.jtester.junit.filter.SuiteType;
import org.jtester.junit.filter.finder.FilterCondiction;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.ClazzHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * ClassTester implementation to retrieve JUnit38 & 4.x test classes in the
 * classpath. You can specify if you want to include jar files in the search and
 * you can give a set of regex expression to specify the class names to include.
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TestInClasspathAcceptor implements TestAcceptor {
	private final FilterCondiction testerFilter;

	public TestInClasspathAcceptor(FilterCondiction testerFilter) {
		this.testerFilter = testerFilter;
	}

	/**
	 * 测试类是否是指定的测试类型
	 */
	public boolean isCorrectTestType(Class<?> clazz) {
		List<SuiteType> types = testerFilter.getSuiteTypes();
		if (types.contains(SuiteType.JUNT4_TEST_CLASSES)) {
			if (isJunit4TestClaz(clazz)) {
				return true;
			}
		}
		if (types.contains(SuiteType.JUNIT38_TEST_CLASSES)) {
			if (isJUnit38TestClaz(clazz)) {
				return true;
			}
		}
		if (types.contains(SuiteType.SUITE_TEST_CLASSES)) {
			return isJunit4SuiteClaz(clazz);
		}
		return false;
	}

	/**
	 * 是否是junit38类型测试
	 * 
	 * @param clazz
	 * @return
	 */
	private boolean isJUnit38TestClaz(Class<?> clazz) {
		return TestCase.class.isAssignableFrom(clazz);
	}

	/**
	 * 是否是JUnit4类型测试
	 * 
	 * @param clazz
	 * @return
	 */
	private boolean isJunit4TestClaz(Class<?> clazz) {
		try {
			for (Method method : clazz.getMethods()) {
				if (method.getAnnotation(Test.class) != null) {
					return true;
				}
			}
		} catch (NoClassDefFoundError ignore) {
		}
		return false;
	}

	/**
	 * 是否是junit4 suite类型测试
	 * 
	 * @param clazz
	 * @return
	 */
	private boolean isJunit4SuiteClaz(Class<?> clazz) {
		RunWith runwith = AnnotationHelper.getClassLevelAnnotation(RunWith.class, clazz);
		if (runwith == null) {
			return false;
		} else {
			return Suite.class.isAssignableFrom(runwith.value());
		}
	}

	public boolean isCorrectClazType(Class<?> clazz) {
		if (clazz == null || ClazzHelper.isAbstract(clazz) || clazz.isAnonymousClass() || clazz.isInterface()
				|| clazz.isLocalClass() || clazz.isEnum() || clazz.isMemberClass() || clazz.isAnnotation()) {
			return false;
		}
		boolean isAcceptedByExcluded = this.acceptedByBaseTypes(clazz, testerFilter.getExcludedBaseTypes(), false);
		if (isAcceptedByExcluded) {
			return false;
		}
		boolean isAcceptedByIncluded = this.acceptedByBaseTypes(clazz, testerFilter.getIncludedBaseTypes(), true);
		return isAcceptedByIncluded;
	}

	private boolean acceptedByBaseTypes(Class<?> clazz, Class<?>[] bases, boolean _default) {
		if (bases == null || bases.length == 0) {
			return _default;
		}
		for (Class base : bases) {
			if (base.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 测试类名称是否通过表达式过滤规则
	 */
	public boolean isAcceptedByPatterns(String className) {
		boolean isNegationAccepted = this.acceptedByPatterns(className, testerFilter.getNegationFilters(), false);
		if (isNegationAccepted) {
			return false;
		}
		boolean isPositionAccepted = this.acceptedByPatterns(className, testerFilter.getPositiveFilters(), true);
		return isPositionAccepted;
	}

	/**
	 * @param className
	 * @param patterns
	 * @param _default
	 *            规则为空时的返回值
	 * @return
	 */
	private boolean acceptedByPatterns(String className, List<String> patterns, boolean _default) {
		if (patterns == null || patterns.isEmpty()) {
			return _default;
		}
		for (String pattern : patterns) {
			if (className.matches(pattern)) {
				return true;
			}
		}
		return false;
	}

	public boolean acceptInnerClass() {
		return true;
	}

	public boolean searchInJars() {
		return testerFilter.isSearchInJars();
	}
}
