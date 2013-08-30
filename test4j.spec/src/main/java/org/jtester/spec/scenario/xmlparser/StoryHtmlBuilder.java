package org.jtester.spec.scenario.xmlparser;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.jtester.spec.util.XmlHelper;
import org.jtester.tools.commons.ResourceHelper;

/**
 * 将xml格式的用例文件转换为html格式
 * 
 * @author darui.wudr 2012-7-13 上午9:50:51
 */
@SuppressWarnings({ "unchecked", "serial" })
public class StoryHtmlBuilder implements Serializable {
	private Document document;

	public StoryHtmlBuilder(Document document) {
		this.document = document;
	}

	public StoryHtmlBuilder(String xml) {
		this.document = XmlHelper.buildFromString(xml);
	}

	public String toHtml() {
		StringBuilder html = new StringBuilder();
		this.buildStoryDescription(html);
		this.buildTemplateList(html);
		this.buildScenarioList(html);
		return html.toString();
	}

	/**
	 * 构建用例的场景列表html
	 * 
	 * @param html
	 */
	void buildScenarioList(StringBuilder html) {
		List<Element> scenarios = this.document.selectNodes(StoryQName.xpathScenario);
		String scenariosHTML = this.buildScenarioList(scenarios);
		html.append(String.format(STORY_SCENARIO_LIST, scenariosHTML));
	}

	/**
	 * 构建场景列表的html
	 * 
	 * @param scenarioHtml
	 * @param scenario
	 */
	String buildScenarioList(List<Element> scenarios) {
		if (scenarios == null) {
			return "";
		}
		StringBuilder html = new StringBuilder();
		int index = 1;
		for (Element scenario : scenarios) {
			List<Element> methods = scenario.selectNodes(StoryQName.nodeScenarioMethod);
			String methodsHTML = this.buildScenarioMethods(index, methods);

			String skip = scenario.attributeValue(StoryQName.attrSkip, "false").toLowerCase();
			String name = scenario.attributeValue(StoryQName.attrName, "");
			Element descNode = (Element) scenario.selectSingleNode(StoryQName.nodeDescription);
			String desc = descNode == null ? "" : descNode.getText();
			html.append(String.format(STORY_SCENARIO, index, skip, name, desc, methodsHTML)).append("\n");
			index++;
		}
		return html.toString();
	}

	String buildScenarioMethods(int scenarioIndex, List<Element> methods) {
		if (methods == null) {
			return "";
		}
		StringBuilder html = new StringBuilder();
		int methodIndex = 1;
		for (Element method : methods) {
			String type = method.attributeValue(StoryQName.attrType, "given").toLowerCase();
			String skip = method.attributeValue(StoryQName.attrSkip, "false").toLowerCase();
			String name = method.attributeValue(StoryQName.attrName, "");
			String text = StoryFeatureXMLBuilder.buildElementText(method);
			html.append(String.format(STORY_SCENARIO_METHOD, scenarioIndex, methodIndex++, type, skip, name, text))
					.append("\n");
		}
		return html.toString();
	}

	/**
	 * 构建用例模板方法列表
	 * 
	 * @param html
	 */
	void buildTemplateList(StringBuilder html) {
		List<Element> methods = this.document.selectNodes(StoryQName.xpathTemplateMethod);
		String methodsHtml = this.buildTemplateMethods(methods);
		html.append(String.format(STORY_TEMPLATE_LIST, methodsHtml));
	}

	private String buildTemplateMethods(List<Element> methods) {
		if (methods == null) {
			return "";
		}
		StringBuilder html = new StringBuilder();
		int index = 1;
		for (Element method : methods) {
			String type = method.attributeValue(StoryQName.attrType, "given").toLowerCase();
			String name = method.attributeValue(StoryQName.attrName, "");
			String text = method.getText();
			html.append(String.format(STORY_TEMPLATE_METHOD, index++, type, name, text)).append("\n");
		}
		return html.toString();
	}

	/**
	 * 构建用例文件描述
	 * 
	 * @param html
	 */
	void buildStoryDescription(StringBuilder html) {
		Element desc = (Element) document.selectSingleNode(StoryQName.xpathDescription);
		html.append(String.format(STORY_DESCRIPTION_TEMPLATE, desc.getText()));
	}

	private static final String STORY_DESCRIPTION_TEMPLATE;

	private static final String STORY_TEMPLATE_LIST;

	private static final String STORY_TEMPLATE_METHOD;

	private static final String STORY_SCENARIO_LIST;

	private static final String STORY_SCENARIO;

	private static final String STORY_SCENARIO_METHOD;
	static {
		try {
			STORY_DESCRIPTION_TEMPLATE = ResourceHelper.readFromFile(StoryHtmlBuilder.class, "story-description.html");
			STORY_TEMPLATE_LIST = ResourceHelper.readFromFile(StoryHtmlBuilder.class, "story-template-list.html");
			STORY_TEMPLATE_METHOD = ResourceHelper.readFromFile(StoryHtmlBuilder.class, "story-template-method.html");

			STORY_SCENARIO_LIST = ResourceHelper.readFromFile(StoryHtmlBuilder.class, "story-scenario-list.html");
			STORY_SCENARIO = ResourceHelper.readFromFile(StoryHtmlBuilder.class, "story-scenario.html");
			STORY_SCENARIO_METHOD = ResourceHelper.readFromFile(StoryHtmlBuilder.class, "story-scenario-method.html");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
