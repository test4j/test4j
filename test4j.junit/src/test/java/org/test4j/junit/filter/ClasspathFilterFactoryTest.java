package org.test4j.junit.filter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.test4j.junit.filter.acceptor.TestInClasspathAcceptor;
import org.test4j.junit.filter.finder.ClasspathTestClazFinder;
import org.test4j.junit.filter.finder.FilterCondiction;
import org.test4j.module.ICore;

public class ClasspathFilterFactoryTest implements ICore {

    @Test
    public void creationParametersAreHandedToTester() {
        FilterCondiction filter = new FilterCondiction(true, new String[] { "pos", "!neg" },
                new SuiteType[] { SuiteType.JUNT4_TEST_CLASSES }, new Class<?>[] { Object.class, getClass() },
                new Class<?>[] { String.class });
        ClasspathTestClazFinder finder = new ClasspathTestClazFinder(new TestInClasspathAcceptor(filter));
        TestInClasspathAcceptor tester = (TestInClasspathAcceptor) finder.getTester();
        assertTrue(tester.searchInJars());
        assertArrayEquals(new String[] { "pos" }, filter.getPositiveFilters().toArray());
        assertArrayEquals(new String[] { "neg" }, filter.getNegationFilters().toArray());
        want.list(filter.getSuiteTypes()).reflectionEq(new SuiteType[] { SuiteType.JUNT4_TEST_CLASSES });
        assertArrayEquals(new Class<?>[] { Object.class, getClass() }, filter.getIncludedBaseTypes());
        assertArrayEquals(new Class<?>[] { String.class }, filter.getExcludedBaseTypes());
    }
}
