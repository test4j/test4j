package org.test4j.module.spec.internal;

import org.test4j.function.SExecutor;

import java.util.function.Consumer;

public class ScenarioThen implements IThen {
    private final ScenarioResult scenario;

    ScenarioThen(ScenarioResult scenario) {
        this.scenario = scenario;
    }

    @Override
    public IThen then(String description, SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.Then, description, lambda, null);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - " + description + ", 验证错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen then(SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.Then, lambda, null);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 验证错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen want(Consumer<Throwable> consumer) throws RuntimeException {
        SExecutor lambda = () -> {
            Throwable expected = SpecContext.getExpectedException();
            if (expected == null) {
                throw new AssertionError("Expecting an exception, but not!");
            }
            try {
                consumer.accept(expected);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new AssertionError("expected an Exception error: " + e.getMessage(), expected);
            }
        };
        try {
            this.scenario.doStep(StepType.Then, "异常验证", lambda, null);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 异常验证错误：" + e.getMessage(), e);
        }
    }
}
