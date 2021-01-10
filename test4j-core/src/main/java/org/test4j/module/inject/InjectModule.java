package org.test4j.module.inject;

import org.test4j.module.core.Module;
import org.test4j.module.core.internal.ModuleListener;

import java.lang.reflect.Method;

import static org.test4j.module.inject.imposteriser.InjectHelper.injectIntoTestedObject;

public class InjectModule implements Module {

    @Override
    public void init() {
    }

    @Override
    public void afterInit() {
    }

    /**
     * test4j扩展的注入<br>
     *
     * @param testedObject
     */
    private void injectInto(Object testedObject) {
        injectIntoTestedObject(testedObject);
    }

    @Override
    public ModuleListener getTestListener() {
        return new InjectModuleListener();
    }

    protected class InjectModuleListener extends ModuleListener {
        @Override
        public void beforeExecute(Object testObject, Method testMethod) {
            injectInto(testObject);
        }

        @Override
        protected String getName() {
            return "InjectTestListener";
        }
    }
}