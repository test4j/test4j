package org.test4j.module.spec.internal;

import org.test4j.function.ReturnExecutor;
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
    public <E extends Throwable> IThen want(String description, Consumer<E> consumer) throws RuntimeException {
        SExecutor lambda = () -> {
            E expected = (E) SpecContext.getExpectedException();
            if (expected == null) {
                throw new AssertionError("Expecting an exception, but not!");
            }
            try {
                consumer.accept(expected);
            } catch (Throwable e) {
                throw new AssertionError("expected an Exception error: " + e.getMessage(), expected);
            }
        };
        try {
            this.scenario.doStep(StepType.Then, description, lambda, null);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 异常验证错误：" + e.getMessage(), e);
        }
    }

    @Override
    public <T> IThen wantResult(String description, Consumer<T> consumer) throws RuntimeException {
        ReturnExecutor lambda = () -> {
            try {
                consumer.accept((T) SpecContext.getWhenResult());
                return null;
            } catch (Throwable e) {
                throw new AssertionError("assert result error: " + e.getMessage(), e);
            }
        };
        try {
            this.scenario.doStep(StepType.Then, description, lambda, null);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 执行结果验证错误：" + e.getMessage(), e);
        }
    }
}
