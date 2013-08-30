package org.jtester.spec.scenario.xmlparser;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.dom4j.CharacterData;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.jtester.spec.inner.StepType;
import org.jtester.spec.scenario.step.XmlJSpecStep;
import org.jtester.spec.scenario.step.xml.MethodDescription;
import org.jtester.spec.scenario.xmlparser.entity.ScenarioList;
import org.jtester.spec.scenario.xmlparser.entity.ScenarioMethod;
import org.jtester.spec.scenario.xmlparser.entity.StoryDescription;
import org.jtester.spec.scenario.xmlparser.entity.StoryScenario;
import org.jtester.spec.scenario.xmlparser.entity.TemplateList;
import org.jtester.spec.scenario.xmlparser.entity.TemplateMethod;
import org.jtester.tools.commons.StringHelper;

/**
 * 将一个xml的document对象转换为StoryFeature对象
 * 
 * @author darui.wudr 2012-7-17 下午4:48:57
 */
@SuppressWarnings({ "unchecked", "serial" })
public class StoryFeatureXMLBuilder implements Serializable {
	private final Document document;

	public StoryFeatureXMLBuilder(Document document) {
		this.document = document;
	}

	/**
	 * 根据xml对象构造StoryFeature对象
	 * 
	 * @param document
	 * @return
	 */
	public StoryFeature build() {
		StoryFeature story = new StoryFeature();
		StoryDescription description = this.buildStoryDescription();
		story.setDescription(description);
		TemplateList templates = this.buildStoryTemplates();
		story.setTemplates(templates);
		ScenarioList scenarios = this.buildStoryScenarios(templates);
		story.setScenarios(scenarios);

		return story;
	}

	/**
	 * 更新用例文档的描述
	 * 
	 * @param document
	 * @param description
	 */
	public static void setStoryDescription(Document document, String description) {
		Element node = (Element) document.selectSingleNode(StoryQName.xpathDescription);
		if (node == null) {
			node = document.getRootElement().addElement(StoryQName.nodeDescription);
		}
		node.setText(description);
	}

	/**
	 * 返回用例文档的描述
	 * 
	 * @param document
	 * @return
	 */
	public static String getStoryDescription(Document document) {
		Element node = (Element) document.selectSingleNode(StoryQName.xpathDescription);
		return node == null ? "" : node.getText();
	}

	/**
	 * 构建用例场景列表
	 * 
	 * @return
	 */
	ScenarioList buildStoryScenarios(TemplateList templates) {
		ScenarioList list = new ScenarioList();
		List<Element> nodes = this.document.selectNodes(StoryQName.xpathScenario);
		if (nodes == null) {
			return list;
		}
		int index = 0;
		for (Element node : nodes) {
			StoryScenario scenario = buildStoryScenario(node, ++index, templates);
			list.addScenario(scenario);
		}
		return list;
	}

	/**
	 * 构建场景
	 * 
	 * @param node
	 * @return
	 */
	private StoryScenario buildStoryScenario(Element node, int scenarioIndex, TemplateList templates) {
		String name = node.attributeValue(StoryQName.attrName, "");
		String skip = node.attributeValue(StoryQName.attrSkip, "false");
		StoryScenario scenario = new StoryScenario(name, skip);

		Element descNode = (Element) node.selectSingleNode(StoryQName.nodeDescription);
		String description = descNode == null ? "" : descNode.getText();
		scenario.setDescription(description);
		scenario.setPathID(scenarioIndex);

		List<Element> methods = node.selectNodes(StoryQName.nodeScenarioMethod);
		int methodIndex = 0;
		for (Element mnode : methods) {
			ScenarioMethod method = buildScenarioMethod(mnode, templates);
			method.setPathID(scenarioIndex, ++methodIndex);
			scenario.addMethod(method);
		}
		return scenario;
	}

	/**
	 * 构建场景
	 * 
	 * @param node
	 * @return
	 */
	private static StoryScenario buildStoryScenario(Element node) {
		String name = node.attributeValue(StoryQName.attrName, "");
		String skip = node.attributeValue(StoryQName.attrSkip, "false");
		StoryScenario scenario = new StoryScenario(name, skip);

		Element descNode = (Element) node.selectSingleNode(StoryQName.nodeDescription);
		String description = descNode == null ? "" : descNode.getText();
		scenario.setDescription(description);

		return scenario;
	}

	/**
	 * 构建场景步骤
	 * 
	 * @param mnode
	 * @return
	 */
	private static ScenarioMethod buildScenarioMethod(Element node, TemplateList templates) {
		String name = node.attributeValue(StoryQName.attrName, "");
		String type = node.attributeValue(StoryQName.attrType, "given").toLowerCase();
		String skip = node.attributeValue(StoryQName.attrSkip, "false").toLowerCase();
		ScenarioMethod method = new ScenarioMethod(name, type, skip);

		String template = XmlJSpecStep.getTemplateText(name, node);
		LinkedHashMap<String, String> paras = new LinkedHashMap<String, String>();
		boolean useTemplate = false;
		if (templates != null) {
			TemplateMethod templateMethod = templates.findTemplate(name, type);
			if (templateMethod != null) {
				useTemplate = true;
				template = templateMethod.getTemplateText();
				paras = templateMethod.getParas();
			}
		}
		method.setParas(paras);
		method.setTemplateText(template);
		parseParameter(paras, useTemplate, node);
		String displayText = new MethodDescription(template, paras).getMethodDisplayText();
		method.setDisplayText(displayText);
		String initialText = StoryFeatureXMLBuilder.buildElementText(node);
		method.setInitialText(initialText);

		return method;
	}

	/**
	 * 构建用例模板列表
	 * 
	 * @return
	 */
	private TemplateList buildStoryTemplates() {
		TemplateList list = new TemplateList();
		List<Element> nodes = this.document.selectNodes(StoryQName.xpathTemplateMethod);
		if (nodes == null) {
			return list;
		}
		int index = 0;
		for (Element node : nodes) {
			TemplateMethod method = buildTemplateMethod(node);
			method.setPathID(++index);
			list.add(method);
		}
		return list;
	}

	/**
	 * 构建用例步骤模板
	 * 
	 * @param node
	 * @return
	 */
	private static TemplateMethod buildTemplateMethod(Element node) {
		String name = node.attributeValue(StoryQName.attrName, "");
		String type = node.attributeValue(StoryQName.attrType, "given");
		TemplateMethod method = new TemplateMethod(name, type);
		String template = XmlJSpecStep.getTemplateText(name, node);
		method.setTemplateText(template);
		LinkedHashMap<String, String> paras = new LinkedHashMap<String, String>();
		parseParameter(paras, false, node);
		method.setParas(paras);
		String displayText = new MethodDescription(template, paras).getMethodDisplayText();
		method.setDisplayText(displayText);
		String initialText = StoryFeatureXMLBuilder.buildElementText(node);
		method.setInitialText(initialText);
		return method;
	}

	/**
	 * 构建用例文件描述
	 * 
	 * @param html
	 */
	StoryDescription buildStoryDescription() {
		StoryDescription description = new StoryDescription();
		Element desc = (Element) document.selectSingleNode(StoryQName.xpathDescription);
		description.setDescription(desc.getText());
		return description;
	}

	/**
	 * 构造节点中的文本内容+子节点内容
	 * 
	 * @param method
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String buildElementText(Element method) {
		StringBuffer buff = new StringBuffer();
		for (Iterator it = method.nodeIterator(); it.hasNext();) {
			Node item = (Node) it.next();
			if (item instanceof CharacterData) {
				String text = ((CharacterData) item).getText();
				buff.append(text == null ? "" : text.trim());
			} else {
				String xml = ((Node) item).asXML();
				buff.append("\n").append(xml == null ? "" : xml.trim());
			}
		}
		return buff.toString();
	}

	/**
	 * 解析方法的参数列表
	 * 
	 * @param element
	 */
	static void parseParameter(LinkedHashMap<String, String> paras, boolean useTemplate, Element element) {
		List<Element> paraNodes = element.selectNodes("para");
		for (Element paraNode : paraNodes) {
			String name = paraNode.attributeValue("name");
			if (StringHelper.isEmpty(name)) {
				String error = String.format("the parameter name of method can't be empty!");
				throw new RuntimeException(error);
			}
			if (useTemplate && !paras.containsKey(name)) {
				String error = String.format("the template doesn't contain parameter[%s].", name);
				throw new RuntimeException(error);
			}
			if (!useTemplate && paras.containsKey(name)) {
				String error = String.format("the method have duplicated parameter[%s].", name);
				throw new RuntimeException(error);
			}
			String json = paraNode.getText().trim();
			paras.put(name, json);
		}
	}

	/**
	 * 根据document和方法的xpathID构造ScenarioMethod对象(方法的原始内容）
	 * 
	 * @param document
	 * @param xpathID
	 * @return
	 */
	public static ScenarioMethod getScenarioMethodInitialContent(Document document, String xpathID) {
		ScenarioMethod method = new ScenarioMethod("not found method by pathID", StepType.Given);
		if (!xpathID.startsWith(StoryQName.xpathScenario) || !xpathID.contains(StoryQName.nodeScenarioMethod)) {
			return method;
		}
		Element element = (Element) document.selectSingleNode(xpathID);
		if (element == null) {
			return method;
		}
		return buildScenarioMethod(element, null);
	}

	/**
	 * 根据document和方法的xpathID构造TemplateMethod对象
	 * 
	 * @param document2
	 * @param xpathID
	 * @return
	 */
	public static TemplateMethod getTemplateMethod(Document document, String xpathID) {
		TemplateMethod method = new TemplateMethod("not found method by pathID", StepType.Given);
		if (!xpathID.startsWith(StoryQName.xpathTemplateMethod)) {
			return method;
		}
		Element element = (Element) document.selectSingleNode(xpathID);
		if (element == null) {
			return method;
		}
		return buildTemplateMethod(element);
	}

	/**
	 * 根据XPathID获取用例场景实例
	 * 
	 * @param document
	 * @param xpathID
	 * @return
	 */
	public static StoryScenario getStoryScenario(Document document, String xpathID) {
		StoryScenario scenario = new StoryScenario("");
		if (!xpathID.startsWith(StoryQName.xpathScenario) || xpathID.contains(StoryQName.nodeScenarioMethod)) {
			return scenario;
		}
		Element element = (Element) document.selectSingleNode(xpathID);
		if (element == null) {
			return scenario;
		}
		return buildStoryScenario(element);
	}
}
