package org.test4j.module.core;

import org.test4j.Context;
import org.test4j.ITest4JListener;
import org.test4j.module.core.internal.ModuleListener;
import org.test4j.module.core.internal.ModulesManager;
import org.test4j.module.spec.internal.SpecContext;

import java.lang.reflect.Method;

public class CoreModuleListener extends ModuleListener implements ITest4JListener {
    @Override
    public void beforeAll(Class testClazz) {
        CoreModule.initModules();
        ModulesManager.getTestListeners()
            .forEach(listener -> listener.beforeAll(testClazz));
    }

    @Override
    public void beforeExecute(Object testObject, Method testMethod) {
        ModulesManager.getTestListeners().forEach(listener -> listener.beforeExecute(testObject, testMethod));
    }

    @Override
    public void afterExecute(Object testObject, Method testMethod, Throwable throwable) {
        ModulesManager.getTestListeners_Reverse()
            .forEach(listener -> listener.afterExecute(testObject, testMethod, throwable));
    }

    @Override
    public void afterAll() {
        Class testClass = Context.currTestClass();
        ModulesManager.getTestListeners_Reverse()
            .forEach(listener -> listener.afterAll(testClass));
        SpecContext.clean();
    }

    public void beforeMethod(Object testObject) {
    }

    public void afterMethod() {
    }

    @Override
    protected String getName() {
        return "CoreModuleListener";
    }
}