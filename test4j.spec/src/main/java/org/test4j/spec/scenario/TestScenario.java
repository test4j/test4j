package org.test4j.spec.scenario;

import java.util.ArrayList;
import java.util.List;

import org.test4j.spec.inner.IScenario;
import org.test4j.spec.inner.IScenarioStep;

/**
 * 可执行的场景定义
 * 
 * @author davey.wu 2014年6月18日 下午7:05:33
 */
public class TestScenario implements IScenario {
    private List<IScenarioStep> steps;

    private int                 index;

    private IScenario           currScenario;

    public TestScenario(IScenario before, IScenario curr, IScenario after) {
        if (curr == null) {
            throw new RuntimeException("the executed scenario can't be null!");
        }
        currScenario = curr;
        this.setIndex(curr.getIndex());
        this.steps = new ArrayList<IScenarioStep>();
        if (before != null) {
            this.steps.addAll(before.getSteps());
        }
        this.steps.addAll(curr.getSteps());
        if (after != null) {
            this.steps.addAll(after.getSteps());
        }
    }

    @Override
    public List<IScenarioStep> getSteps() {
        return steps;
    }

    @Override
    public void validate() throws Throwable {
        currScenario.validate();
    }

    @Override
    public String getName() {
        return this.currScenario.getName();
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }
}
