package org.test4j.module.spec.internal;

import org.test4j.function.SExecutor;
import org.test4j.module.core.internal.Test4JContext;
import org.test4j.tools.datagen.TableDataAround;

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
    public IThen when(String description, SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, description, lambda, null);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - " + description + ", 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen when(SExecutor lambda) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, lambda, null);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen when(String description, SExecutor lambda, Class<? extends Throwable> eKlass) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, description, lambda, eKlass);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - " + description + ",执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IThen when(SExecutor lambda, Class<? extends Throwable> eKlass) throws RuntimeException {
        try {
            this.scenario.doStep(StepType.When, lambda, eKlass);
            return new ScenarioThen(this.scenario);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 执行错误：" + e.getMessage(), e);
        }
    }

    @Override
    public IAround aroundDb() throws RuntimeException {
        try {
            String file = TableDataAround.findFile(Test4JContext.currTestedClazz(), Test4JContext.currTestedMethod().getName());
            return this.aroundDb(file);
        } catch (Throwable e) {
            throw new RuntimeException("步骤 - 数据库初始化数据和检查数据准备失败：" + e.getMessage(), e);
        }
    }

    @Override
    public IAround aroundDb(String file) throws RuntimeException {
        TableDataAround.around(file);
        return this;
    }
}
