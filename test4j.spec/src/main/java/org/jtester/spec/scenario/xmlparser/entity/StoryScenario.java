package org.jtester.spec.scenario.xmlparser.entity;

import java.util.ArrayList;
import java.util.List;

import org.jtester.spec.scenario.xmlparser.StoryFeature;
import org.jtester.spec.scenario.xmlparser.StoryNodeEntity;

/**
 * story xml场景节点
 * 
 * @author darui.wudr 2012-7-12 下午2:05:03
 */
@SuppressWarnings("serial")
public class StoryScenario implements StoryNodeEntity {
	private String description;

	private String displayDescription;

	private String name;

	private boolean isSkip;

	private List<ScenarioMethod> methods;

	public StoryScenario(String scenarioName) {
		this.name = scenarioName;
		this.setDescription("");
		this.isSkip = false;
		this.methods = new ArrayList<ScenarioMethod>();
	}

	public StoryScenario(String scenarioName, boolean skip) {
		this(scenarioName);
		this.isSkip = skip;
	}

	public StoryScenario(String scenarioName, String skip) {
		this(scenarioName);
		this.isSkip = "true".equalsIgnoreCase(skip);
	}

	public String getDescription() {
		return this.description;
	}

	public String getDisplayDescription() {
		return displayDescription;
	}

	public StoryScenario setDescription(String description) {
		this.description = description == null ? "" : description;
		this.displayDescription = StoryFeature.convetTextToHTML(this.description);
		return this;
	}

	public String getScenarioName() {
		return this.name;
	}

	public boolean isSkip() {
		return this.isSkip;
	}

	public StoryScenario setSkip(String skip) {
		this.isSkip = "true".equalsIgnoreCase(skip);
		return this;
	}

	public String getEntityName() {
		return this.getScenarioName();
	}

	public List<ScenarioMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<ScenarioMethod> methods) {
		this.methods = methods;
	}

	/**
	 * 增加场景步骤
	 * 
	 * @param method
	 */
	public void addMethod(ScenarioMethod method) {
		this.methods.add(method);
	}

	private int scenarioIndex;

	public void setPathID(int scenarioIndex) {
		this.scenarioIndex = scenarioIndex;
	}

	public String getPathID() {
		return String.format(PathID_Story_Scenario, scenarioIndex);
	}
}
