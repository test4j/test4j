package org.jtester.junit.filter.finder;

import java.util.Collection;

import org.jtester.junit.annotations.Group;
import org.jtester.junit.filter.acceptor.TestAcceptor;
import org.jtester.junit.filter.acceptor.TestAcceptor.AllTestAcceptor;
import org.junit.Assert;
import org.junit.Test;

@Group("common")
public class ClasspathTestClazFinderTest {
	private static final String DEFAULT_CLASSPATH_PROPERTY = "java.class.path";

	@Test
	public void allClassesIncludingJarFiles() {
		TestAcceptor tester = new AllTestAcceptor() {
			@Override
			public boolean searchInJars() {
				return true;
			}

			@Override
			public boolean isAcceptedByPatterns(String className) {
				return className.startsWith("injar.");
			}
		};
		Collection<Class<?>> classes = new ClasspathTestClazFinder(tester, DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(4, classes.size());
	}

	@Test
	public void nonDefaultClasspathProperty() {
		System.setProperty("my.class.path", "./lib/mytests.jar");
		TestAcceptor tester = new AllTestAcceptor();
		Collection<Class<?>> classes = new ClasspathTestClazFinder(tester, "my.class.path").find();
		Assert.assertEquals(4, classes.size());
	}
}
