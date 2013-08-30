package org.jtester.junit.suite;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jtester.junit.annotations.BeforeSuite;
import org.jtester.junit.filter.ClasspathFilterFactory;
import org.jtester.junit.filter.FilterFactory;
import org.jtester.junit.filter.finder.TestClazFinder;
import org.jtester.tools.commons.MethodHelper;
import org.jtester.tools.reflector.MethodAccessor;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class ClasspathSuite extends Suite {

	private final Class<?> suiteClass;

	public ClasspathSuite(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError {
		super(builder, suiteClass, getSortedTestclasses(suiteClass, new ClasspathFilterFactory()));
		this.suiteClass = suiteClass;
	}

	public ClasspathSuite(Class<?> suiteClass, RunnerBuilder builder, FilterFactory filterFactory)
			throws InitializationError {
		super(builder, suiteClass, getSortedTestclasses(suiteClass, filterFactory));
		this.suiteClass = suiteClass;
	}

	private static Class<?>[] getSortedTestclasses(Class<?> suiteClass, FilterFactory filterFactory) {
		TestClazFinder finder = filterFactory.createFinder(suiteClass);

		List<Class<?>> testclasses = finder.find();
		Collections.sort(testclasses, new Comparator<Class<?>>() {
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return testclasses.toArray(new Class[testclasses.size()]);
	}

	@Override
	public void run(RunNotifier notifier) {
		try {
			this.runBeforeMethods();
		} catch (Exception e) {
			notifier.fireTestFailure(new Failure(getDescription(), e));
			return;
		}
		super.run(notifier);
	}

	/**
	 * 执行所有@BeforeSuite方法
	 * 
	 * @throws Exception
	 */
	private void runBeforeMethods() throws Exception {
		for (Method each : suiteClass.getMethods()) {
			boolean isBeforeSuiteMethod = each.isAnnotationPresent(BeforeSuite.class);
			if (!isBeforeSuiteMethod) {
				continue;
			}
			boolean isPublicStaticVoid = MethodHelper.isPublicStaticVoid(each);
			if (isPublicStaticVoid) {
				try {
					new MethodAccessor<Void>(each).invokeStatic(new String[0]);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
}
