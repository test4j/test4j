package org.test4j.module.spec.internal;

import org.test4j.function.SExecutor;

public class ScenarioThen implements IThen {
    private final ScenarioResult scenario;

    ScenarioThen(ScenarioResult scenario) {
        this.scenario = scenario;
    }

    @Override
    public IThen then(String description, SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.Then, description, lambda);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - " + description + ", 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen then(SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.Then, lambda);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 执行错误：" + e.getMessage(), e);
        }
    }
}
