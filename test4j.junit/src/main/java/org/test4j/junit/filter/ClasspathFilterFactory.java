package org.test4j.junit.filter;

import org.test4j.junit.annotations.AnnotationDefaultValue;
import org.test4j.junit.annotations.ClasspathProperty;
import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.filter.acceptor.TestAcceptor;
import org.test4j.junit.filter.acceptor.TestInClasspathAcceptor;
import org.test4j.junit.filter.finder.ClasspathTestClazFinder;
import org.test4j.junit.filter.finder.FilterCondiction;
import org.test4j.junit.filter.finder.TestClazFinder;

public class ClasspathFilterFactory implements FilterFactory {

	public TestClazFinder create(String clazzpathProp, FilterCondiction filterCondiction) {
		TestAcceptor tester = new TestInClasspathAcceptor(filterCondiction);
		return new ClasspathTestClazFinder(tester, clazzpathProp);
	}

	public TestClazFinder createFinder(Class<?> suiteClazz) {
		String clazzpathProp = AnnotationDefaultValue.DEFAULT_CLASSPATH_PROPERTY;
		FilterCondiction filterCondiction = new FilterCondiction();

		ClazFinder testFilterAnnotation = suiteClazz.getAnnotation(ClazFinder.class);
		if (testFilterAnnotation != null) {
			filterCondiction.initFilters(testFilterAnnotation);
		}
		ClasspathProperty propAnnotation = suiteClazz.getAnnotation(ClasspathProperty.class);
		if (propAnnotation != null) {
			clazzpathProp = propAnnotation.value();
		}
		return this.create(clazzpathProp, filterCondiction);
	}
}
