package org.test4j.module.spec;

import org.test4j.module.core.Module;
import org.test4j.module.core.internal.ModuleListener;
import org.test4j.module.spec.internal.MixProxy;
import org.test4j.module.spec.internal.ScenarioResult;
import org.test4j.module.spec.internal.StoryPrinter;
import org.test4j.tools.commons.ResourceHelper;

import java.lang.reflect.Method;

public class SpecModule implements Module {
    private static ThreadLocal<ScenarioResult> Curr_Result = new ThreadLocal<>();

    public static ScenarioResult currScenario() {
        return Curr_Result.get();
    }

    @Override
    public void init() {
        ResourceHelper.deleteFileOrDir(StoryPrinter.getStoryPath());
    }

    @Override
    public void afterInit() {
    }

    @Override
    public ModuleListener getTestListener() {
        return new SpecListener();
    }

    protected class SpecListener extends ModuleListener {

        @Override
        protected String getName() {
            return "SpecListener";
        }


        @Override
        public void beforeExecute(Object testedObject, Method testMethod) {
            if (testedObject instanceof IStory) {
                MixProxy.createMixes(testedObject);
                MixProxy.mix(testedObject);
                Curr_Result.set(new ScenarioResult("NoName"));
            }
        }

        @Override
        public void afterExecute(Object testObject, Method testMethod, Throwable testThrowable) {
            if (testObject instanceof IStory) {
                String scenarioPath = testObject.getClass().getName() + "__" + testMethod.getName();
                String scenarioName = currScenario().getScenarioName();
                String scenarioResult = currScenario().scenarioResult();
                StoryPrinter.print(scenarioPath, scenarioName, scenarioResult, testThrowable);
            }
            Curr_Result.remove();
        }

        @Override
        public void afterAll(Class testClass) {
            StoryPrinter.printScenarioIndex(testClass.getSimpleName());
        }
    }
}