package org.test4j.junit.suitetest.suite;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.test4j.junit.annotations.BeforeSuite;
import org.test4j.junit.annotations.ClazFinder;
import org.test4j.junit.filter.ClasspathFilterFactory;
import org.test4j.junit.filter.FilterFactory;
import org.test4j.junit.filter.finder.TestClazFinder;
import org.test4j.tools.commons.MethodHelper;
import org.test4j.tools.reflector.FieldAccessor;
import org.test4j.tools.reflector.MethodAccessor;

public class ClasspathSuite extends Suite {

    private final Class<?> suiteClass;

    public ClasspathSuite(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError {
        this(suiteClass, builder, new ClasspathFilterFactory());
    }

    public ClasspathSuite(Class<?> suiteClass, RunnerBuilder builder, FilterFactory filterFactory)
            throws InitializationError {
        super(builder, suiteClass, getSortedTestclasses(suiteClass, builder, filterFactory));
        this.suiteClass = suiteClass;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Class<?>[] getSortedTestclasses(Class<?> suiteClass, RunnerBuilder builder,
                                                   FilterFactory filterFactory) {
        Set<Class> parents = (Set<Class>) FieldAccessor.getFieldValue(builder, "parents");
        ClazFinder clazFinder = filterFactory.findClazFinder(suiteClass, parents);

        TestClazFinder finder = filterFactory.createFinder(clazFinder);
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
