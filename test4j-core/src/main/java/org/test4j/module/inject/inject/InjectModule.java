package org.test4j.module.inject.inject;

import org.test4j.ITest4JListener;

import java.lang.reflect.Method;

import static org.test4j.module.inject.inject.imposteriser.InjectHelper.injectIntoTestedObject;

public class InjectModule implements ITest4JListener {
    @Override
    public void beforeAll(Class testClass) {
    }

    @Override
    public void afterAll() {
    }

    @Override
    public void beforeMethod(Object target) {
    }

    @Override
    public void afterMethod() {
    }

    @Override
    public void beforeExecute(Object testObject, Method testMethod) {
        injectIntoTestedObject(testObject);
    }

    @Override
    public void afterExecute(Object target, Method method, Throwable e) {
    }
}
