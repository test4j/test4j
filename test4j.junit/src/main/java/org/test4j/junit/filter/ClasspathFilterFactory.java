package org.test4j.junit.filter;

import java.util.Set;

import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.filter.acceptor.TestAcceptor;
import org.test4j.junit.filter.acceptor.TestInClasspathAcceptor;
import org.test4j.junit.filter.finder.ClasspathTestClazFinder;
import org.test4j.junit.filter.finder.FilterCondiction;
import org.test4j.junit.filter.finder.TestClazFinder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClasspathFilterFactory implements FilterFactory {

    public ClazFinder findClazFinder(Class suiteClazz, Set<Class> parents) {
        ClazFinder finder = (ClazFinder) suiteClazz.getAnnotation(ClazFinder.class);
        if (finder != null) {
            return finder;
        }
        if (parents == null || parents.size() == 0) {
            return null;
        }
        for (Class parent : parents) {
            finder = (ClazFinder) parent.getAnnotation(ClazFinder.class);
            if (finder != null) {
                return finder;
            }
        }
        return null;
    }

    public TestClazFinder createFinder(ClazFinder clazFinder) {
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
