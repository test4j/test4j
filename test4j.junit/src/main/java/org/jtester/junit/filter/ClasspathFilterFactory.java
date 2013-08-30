package org.jtester.junit.filter;

import org.jtester.junit.annotations.AnnotationDefaultValue;
import org.jtester.junit.annotations.ClasspathProperty;
import org.jtester.junit.annotations.ClazFinder;
import org.jtester.junit.filter.acceptor.TestAcceptor;
import org.jtester.junit.filter.acceptor.TestInClasspathAcceptor;
import org.jtester.junit.filter.finder.ClasspathTestClazFinder;
import org.jtester.junit.filter.finder.FilterCondiction;
import org.jtester.junit.filter.finder.TestClazFinder;

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
