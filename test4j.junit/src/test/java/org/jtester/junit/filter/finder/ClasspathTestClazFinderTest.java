package org.jtester.junit.filter.finder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jtester.junit.filter.acceptor.TestAcceptor;
import org.jtester.junit.filter.acceptor.TestAcceptor.AllTestAcceptor;
import org.jtester.junit.filter.finder.ClasspathTestClazFinder;
import org.junit.Assert;
import org.junit.Test;

public class ClasspathTestClazFinderTest {

	class AcceptAllClassesInTestDirTester extends AllTestAcceptor {
		@Override
		public boolean isAcceptedByPatterns(String className) {
			return className.startsWith("forfilter.tests.");
		}

		@Override
		public boolean searchInJars() {
			return false;
		}
	}

	private static final String DEFAULT_CLASSPATH_PROPERTY = "java.class.path";

	@Test
	public void allClasses() {
		TestAcceptor tester = new AcceptAllClassesInTestDirTester();
		Collection<Class<?>> classes = new ClasspathTestClazFinder(tester, DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(8, classes.size());
		classes.contains(forfilter.tests.ju38.JU38AbstractTest.class);
		classes.contains(forfilter.tests.ju38.JU38ConcreteTest.class);
		classes.contains(forfilter.tests.ju38.JU38TestWithoutBase.class);
		classes.contains(forfilter.tests.p1.P1Test.class);
		classes.contains(forfilter.tests.p1.P1NoTest.class);
		classes.contains(forfilter.tests.p1.P1NoTest.InnerTest.class);
		classes.contains(forfilter.tests.p2.AbstractP2Test.class);
		classes.contains(forfilter.tests.p2.ConcreteP2Test.class);
	}

	@Test
	public void allClassNames() {
		final Set<String> allClasses = new HashSet<String>();
		allClasses.add("forfilter.tests.ju38.JU38AbstractTest");
		allClasses.add("forfilter.tests.ju38.JU38ConcreteTest");
		allClasses.add("forfilter.tests.ju38.JU38TestWithoutBase");
		allClasses.add("forfilter.tests.p1.P1Test");
		allClasses.add("forfilter.tests.p1.P1NoTest");
		allClasses.add("forfilter.tests.p1.P1NoTest$InnerTest");
		allClasses.add("forfilter.tests.p2.AbstractP2Test");
		allClasses.add("forfilter.tests.p2.ConcreteP2Test");
		TestAcceptor tester = new AcceptAllClassesInTestDirTester() {
			@Override
			public boolean isAcceptedByPatterns(String className) {
				if (className.startsWith("forfilter.tests.")) {
					Assert.assertTrue(allClasses.contains(className));
				}
				return super.isAcceptedByPatterns(className);
			}
		};
		Collection<Class<?>> classes = new ClasspathTestClazFinder(tester, DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(allClasses.size(), classes.size());
	}

	@Test
	public void allClassesExceptInner() {
		TestAcceptor tester = new AcceptAllClassesInTestDirTester() {
			@Override
			public boolean acceptInnerClass() {
				return false;
			}
		};
		Collection<Class<?>> classes = new ClasspathTestClazFinder(tester, DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(7, classes.size());
	}

	@Test
	public void selectClassByAcceptClass() {
		TestAcceptor tester = new AcceptAllClassesInTestDirTester() {
			@Override
			public boolean isCorrectTestType(Class<?> clazz) {
				return clazz.getName().endsWith("NoTest");
			}
		};
		Collection<Class<?>> classes = new ClasspathTestClazFinder(tester, DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(1, classes.size());
	}

	@Test
	public void dontSearchJarsIfSpecified() {
		TestAcceptor tester = new AllTestAcceptor() {
			@Override
			public boolean searchInJars() {
				return false;
			}

			@Override
			public boolean isAcceptedByPatterns(String className) {
				return className.startsWith("injar.");
			}
		};
		Collection<Class<?>> classes = new ClasspathTestClazFinder(tester, DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(0, classes.size());
	}

	@Test
	public void useFallbackValueIfClasspathPropertyIsSpecifiedButNotSetInSystem() {
		System.clearProperty("my.class.path");
		TestAcceptor tester = new AcceptAllClassesInTestDirTester();
		Collection<Class<?>> classes = new ClasspathTestClazFinder(tester, "my.class.path").find();
		Assert.assertEquals(8, classes.size());
	}
}
