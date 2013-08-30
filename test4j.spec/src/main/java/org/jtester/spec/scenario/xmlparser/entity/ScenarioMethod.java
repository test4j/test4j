package org.jtester.spec.scenario.xmlparser.entity;

import java.util.LinkedHashMap;

import org.jtester.spec.inner.StepType;

/**
 * 场景方法
 * 
 * @author darui.wudr 2012-7-12 下午2:47:00
 */
@SuppressWarnings("serial")
public class ScenarioMethod extends TemplateMethod {

	private boolean isSkip;

	public ScenarioMethod(String methodName, StepType type, boolean skip) {
		super(methodName, type);
		this.isSkip = skip;
	}

	public ScenarioMethod(String name, String type, String skip) {
		super(name, type);
		this.isSkip = "true".equalsIgnoreCase(skip);
	}

	public ScenarioMethod(String methodName, StepType type) {
		super(methodName, type);
		this.isSkip = false;
	}

	public boolean isSkip() {
		return this.isSkip;
	}

	@Override
	public ScenarioMethod setParas(LinkedHashMap<String, String> paras) {
		super.setParas(paras);
		return this;
	}

	private int scenarioIndex;

	private int methodIndex;

	/**
	 * 设置场景和方法在document中的序号
	 * 
	 * @param scenarioIndex
	 * @param methodIndex
	 */
	public ScenarioMethod setPathID(int scenarioIndex, int methodIndex) {
		this.scenarioIndex = scenarioIndex;
		this.methodIndex = methodIndex;
		return this;
	}

	public String getPathID() {
		return String.format(PathID_Story_Scenario_Method, scenarioIndex, methodIndex);
	}

	@Override
	public ScenarioMethod setInitialText(String initialText) {
		super.setInitialText(initialText);
		return this;
	}

	@Override
	public ScenarioMethod setTemplateText(String templateText) {
		super.setTemplateText(templateText);
		return this;
	}

	@Override
	public ScenarioMethod setDisplayText(String displayText) {
		super.setDisplayText(displayText);
		return this;
	}
}
