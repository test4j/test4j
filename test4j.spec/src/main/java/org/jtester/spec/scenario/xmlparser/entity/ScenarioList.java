package org.jtester.spec.scenario.xmlparser.entity;

import java.util.ArrayList;
import java.util.List;

import org.jtester.spec.scenario.xmlparser.StoryNodeEntity;

/**
 * 用例场景列表
 * 
 * @author darui.wudr 2012-7-16 下午1:55:35
 */
@SuppressWarnings("serial")
public class ScenarioList implements StoryNodeEntity {
	private List<StoryScenario> scenarios;

	public ScenarioList() {
		this.scenarios = new ArrayList<StoryScenario>();
	}

	public String getEntityName() {
		return "场景列表";
	}

	public List<StoryScenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<StoryScenario> scenarios) {
		this.scenarios = scenarios;
	}

	/**
	 * 增加用例场景
	 * 
	 * @param scenario
	 */
	public void addScenario(StoryScenario scenario) {
		this.scenarios.add(scenario);
	}

	public String getPathID() {
		return PathID_Story_ScenarioList;
	}
}
