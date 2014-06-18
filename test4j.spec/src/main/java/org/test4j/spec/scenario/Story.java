package org.test4j.spec.scenario;

import java.util.ArrayList;
import java.util.List;

import org.test4j.spec.inner.IScenario;
import org.test4j.spec.inner.IScenarioStep;

public class Story {
    private String              description;

    private List<IScenarioStep> templates;
    /**
     * 场景前执行步骤
     */
    private IScenario           beforeScenario;
    /**
     * 场景后执行步骤
     */
    private IScenario           afterScenario;
    /**
     * 场景列表
     */
    private List<IScenario>     scenarios;

    public Story() {
        this.description = "";
        this.templates = new ArrayList<IScenarioStep>();
        this.scenarios = new ArrayList<IScenario>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<IScenarioStep> getTemplates() {
        return templates;
    }

    public void setTemplates(List<IScenarioStep> templates) {
        this.templates = templates;
    }

    public List<IScenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<IScenario> scenarios) {
        this.scenarios = scenarios;
    }

    public IScenario getBeforeScenario() {
        return beforeScenario;
    }

    public void setBeforeScenario(IScenario beforeScenario) {
        this.beforeScenario = beforeScenario;
    }

    public IScenario getAfterScenario() {
        return afterScenario;
    }

    public void setAfterScenario(IScenario afterScenario) {
        this.afterScenario = afterScenario;
    }
}
