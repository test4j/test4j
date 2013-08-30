package org.jtester.testng.utility;

import java.util.ArrayList;
import java.util.List;

import mockit.Instantiation;
import mockit.Mock;
import mockit.MockClass;

import org.testng.ITestNGMethod;
import org.testng.internal.TestNGMethodFinder;

@SuppressWarnings("rawtypes")
@MockClass(realClass = TestNGMethodFinder.class, instantiation = Instantiation.PerMockSetup)
public final class MockTestNGMethodFinder {
	private static final String JTester_Before_Method = "aBeforeMethod";
	private static final String JTester_After_Method = "zAfterMethod";
	private static final String JTester_Before_Class = "aBeforeClass";
	private static final String JTester_After_Class = "zAfterClass";

	public TestNGMethodFinder it;

	@Mock(reentrant = true)
	public ITestNGMethod[] getBeforeClassMethods(Class cls) {
		ITestNGMethod[] methods = it.getBeforeClassMethods(cls);
		List<ITestNGMethod> list = new ArrayList<ITestNGMethod>();
		ITestNGMethod aBeforeClass = null;
		for (ITestNGMethod method : methods) {
			String name = method.getMethodName();
			if (JTester_Before_Class.equals(name)) {
				aBeforeClass = method;
			} else {
				list.add(method);
			}
		}
		if (aBeforeClass != null) {
			list.add(0, aBeforeClass);
		}
		return list.toArray(new ITestNGMethod[0]);
	}

	@Mock(reentrant = true)
	public ITestNGMethod[] getAfterClassMethods(Class cls) {
		ITestNGMethod[] methods = it.getAfterClassMethods(cls);

		List<ITestNGMethod> list = new ArrayList<ITestNGMethod>();
		ITestNGMethod zAfterClass = null;
		for (ITestNGMethod method : methods) {
			String name = method.getMethodName();
			if (JTester_After_Class.equals(name)) {
				zAfterClass = method;
			} else {
				list.add(method);
			}
		}
		if (zAfterClass != null) {
			list.add(zAfterClass);
		}
		return list.toArray(new ITestNGMethod[0]);
	}

	@Mock(reentrant = true)
	public ITestNGMethod[] getBeforeTestMethods(Class cls) {
		ITestNGMethod[] methods = it.getBeforeTestMethods(cls);

		List<ITestNGMethod> list = new ArrayList<ITestNGMethod>();
		ITestNGMethod aBeforeMethod = null;
		for (ITestNGMethod method : methods) {
			String name = method.getMethodName();
			if (JTester_Before_Method.equals(name)) {
				aBeforeMethod = method;
			} else {
				list.add(method);
			}
		}
		if (aBeforeMethod != null) {
			list.add(0, aBeforeMethod);
		}
		return list.toArray(new ITestNGMethod[0]);
	}

	@Mock(reentrant = true)
	public ITestNGMethod[] getAfterTestMethods(Class cls) {
		ITestNGMethod[] methods = it.getAfterTestMethods(cls);

		List<ITestNGMethod> list = new ArrayList<ITestNGMethod>();
		ITestNGMethod zAfterMethod = null;
		for (ITestNGMethod method : methods) {
			String name = method.getMethodName();
			if (JTester_After_Method.equals(name)) {
				zAfterMethod = method;
			} else {
				list.add(method);
			}
		}
		if (zAfterMethod != null) {
			list.add(zAfterMethod);
		}
		return list.toArray(new ITestNGMethod[0]);
	}
}