package org.test4j.module.inject;

import org.test4j.module.core.Module;
import org.test4j.module.core.internal.TestListener;

import java.lang.reflect.Method;

import static org.test4j.module.inject.InjectHelper.injectIntoTestedObject;

@SuppressWarnings({"rawtypes", "unchecked"})
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
    public TestListener getTestListener() {
        return new InjectTestListener();
    }

    protected class InjectTestListener extends TestListener {
        @Override
        public void beforeMethod(Object testObject, Method testMethod) {
            injectInto(testObject);
        }

        @Override
        protected String getName() {
            return "InjectTestListener";
        }
    }
}
