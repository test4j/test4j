package org.test4j.module.spec.internal;

import org.test4j.function.SExecutor;

public class ScenarioGiven implements IGiven {
    private final ScenarioResult scenario;

    public ScenarioGiven(ScenarioResult scenario) {
        this.scenario = scenario;
    }

    @Override
    public IGiven given(String description, SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.Given, description, lambda);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - " + description + ", 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IGiven given(SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.Given, lambda);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen when(String description, SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, description, lambda);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - " + description + ", 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen when(SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, lambda);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 执行错误：" + e.getMessage(), e);
        }
    }
}
