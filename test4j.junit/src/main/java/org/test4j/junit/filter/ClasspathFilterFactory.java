package org.test4j.junit.filter;

import org.test4j.junit.annotations.TestPath;
import org.test4j.junit.filter.acceptor.TestAcceptor;
import org.test4j.junit.filter.acceptor.TestInClasspathAcceptor;
import org.test4j.junit.filter.finder.ClasspathTestClazFinder;
import org.test4j.junit.filter.finder.FilterCondiction;
import org.test4j.junit.filter.finder.TestClazFinder;

public class ClasspathFilterFactory implements FilterFactory {

    public TestClazFinder createFinder(TestPath clazFinder) {
        FilterCondiction filterCondiction = new FilterCondiction();
        if (clazFinder != null) {
            filterCondiction.initFilters(clazFinder);
        }
        return this.create(filterCondiction);
    }

    public TestClazFinder create(FilterCondiction filterCondiction) {
        TestAcceptor tester = new TestInClasspathAcceptor(filterCondiction);
        return new ClasspathTestClazFinder(tester);
    }
}
