package org.jtester.spec.scenario.xmlparser;

import java.util.ArrayList;
import java.util.List;

import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.IScenarioStep;

public class TxtStoryFeature {
    private String              description;

    private List<IScenarioStep> templates;

    private List<IScenario>     scenarios;

    public TxtStoryFeature() {
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
}
