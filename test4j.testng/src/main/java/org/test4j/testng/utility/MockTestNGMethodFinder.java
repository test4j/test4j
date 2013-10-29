package org.test4j.testng.utility;

import java.util.ArrayList;
import java.util.List;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;

import org.testng.ITestNGMethod;
import org.testng.internal.TestNGMethodFinder;

@SuppressWarnings("rawtypes")
public final class MockTestNGMethodFinder extends MockUp<TestNGMethodFinder> {
    private static final String Test4J_Before_Method = "aBeforeMethod";
    private static final String Test4J_After_Method  = "zAfterMethod";
    private static final String Test4J_Before_Class  = "aBeforeClass";
    private static final String Test4J_After_Class   = "zAfterClass";

    //    public TestNGMethodFinder   it;

    @Mock
    public ITestNGMethod[] getBeforeClassMethods(Invocation invocation, Class cls) {
        ITestNGMethod[] methods = invocation.proceed(cls);
        //ITestNGMethod[] methods = it.getBeforeClassMethods(cls);
        List<ITestNGMethod> list = new ArrayList<ITestNGMethod>();
        ITestNGMethod aBeforeClass = null;
        for (ITestNGMethod method : methods) {
            String name = method.getMethodName();
            if (Test4J_Before_Class.equals(name)) {
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

    @Mock
    public ITestNGMethod[] getAfterClassMethods(Invocation invocation, Class cls) {
        ITestNGMethod[] methods = invocation.proceed(cls);
        //ITestNGMethod[] methods = it.getAfterClassMethods(cls);

        List<ITestNGMethod> list = new ArrayList<ITestNGMethod>();
        ITestNGMethod zAfterClass = null;
        for (ITestNGMethod method : methods) {
            String name = method.getMethodName();
            if (Test4J_After_Class.equals(name)) {
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

    @Mock
    public ITestNGMethod[] getBeforeTestMethods(Invocation invocation, Class cls) {
        ITestNGMethod[] methods = invocation.proceed(cls);
        //ITestNGMethod[] methods = it.getBeforeTestMethods(cls);

        List<ITestNGMethod> list = new ArrayList<ITestNGMethod>();
        ITestNGMethod aBeforeMethod = null;
        for (ITestNGMethod method : methods) {
            String name = method.getMethodName();
            if (Test4J_Before_Method.equals(name)) {
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

    @Mock
    public ITestNGMethod[] getAfterTestMethods(Invocation invocation, Class cls) {
        ITestNGMethod[] methods = invocation.proceed(cls);
        //ITestNGMethod[] methods = it.getAfterTestMethods(cls);

        List<ITestNGMethod> list = new ArrayList<ITestNGMethod>();
        ITestNGMethod zAfterMethod = null;
        for (ITestNGMethod method : methods) {
            String name = method.getMethodName();
            if (Test4J_After_Method.equals(name)) {
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
