package org.test4j.module.spec.internal;

import org.test4j.function.ReturnExecutor;
import org.test4j.function.SExecutor;

/**
 * ScenarioGiven
 *
 * @author wudarui
 */
public class ScenarioGiven implements IGiven {
    private final ScenarioResult scenario;

    public ScenarioGiven(ScenarioResult scenario) {
        this.scenario = scenario;
    }

    @Override
    public IGiven given(String description, SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.Given, description, lambda, null);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - " + description + ", 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IGiven given(SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.Given, lambda, null);
            return this;
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen when(String description, ReturnExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, description, lambda, null);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - " + description + ", 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen when(ReturnExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, lambda, null);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen when(String description, ReturnExecutor lambda, Class<? extends Throwable> eKlass) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, description, lambda, eKlass);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - " + description + ",执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen when(ReturnExecutor lambda, Class<? extends Throwable> eKlass) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, lambda, eKlass);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 执行错误：" + e.getMessage(), e);
        }
    }
}