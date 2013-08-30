package org.jtester.spec.scenario;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.IScenarioStep;
import org.jtester.spec.scenario.step.JSpecStep;
import org.jtester.spec.scenario.step.XmlJSpecStep;
import org.jtester.spec.scenario.step.XmlJSpecStep.XmlJSpecStepTemplate;
import org.jtester.spec.scenario.xmlparser.StoryQName;
import org.jtester.tools.commons.StringHelper;

import ext.jtester.apache.commons.io.IOUtils;

/**
 * XML文件描述的故事场景解析器
 * 
 * @author darui.wudr 2012-6-28 下午3:26:51
 */
@SuppressWarnings("unchecked")
public class XmlJSpecScenario extends JSpecScenario {

    public XmlJSpecScenario(Element scenarioNode, List<IScenarioStep> templates) {
        this.parse(scenarioNode, templates);
    }

    /**
     * 解析场景节点
     * 
     * @param scenarioNode
     * @param templates
     */
    private void parse(Element scenarioNode, List<IScenarioStep> templates) {
        this.scenario = scenarioNode.attributeValue(StoryQName.attrName);
        if (StringHelper.isBlankOrNull(this.scenario)) {
            this.scenario = "default scenario";
        }
        this.isSkip = "true".equalsIgnoreCase(scenarioNode.attributeValue(StoryQName.attrSkip, "false").trim());
        Node descNode = scenarioNode.selectSingleNode(StoryQName.nodeScenarioDescription);
        this.description = descNode == null ? "" : descNode.getText();

        List<Element> methodNodes = scenarioNode.selectNodes(StoryQName.nodeScenarioMethod);
        for (Element methodNode : methodNodes) {
            JSpecStep jspecStep = new XmlJSpecStep(this.scenario, methodNode, templates);
            this.steps.add(jspecStep);
        }
    }

    private static List<IScenario> parseJSpecScenarioFrom(Document document) {
        List<IScenario> scenarios = new ArrayList<IScenario>();

        List<IScenarioStep> templates = parseJSpecTemplate(document);
        List<Element> scenarioNodes = document.selectNodes(StoryQName.xpathScenario);
        for (Element scenarioNode : scenarioNodes) {
            JSpecScenario scenario = new XmlJSpecScenario(scenarioNode, templates);
            scenarios.add(scenario);
        }
        return scenarios;
    }

    /**
     * 从文档中解析模板步骤
     * 
     * @param document
     * @return
     */
    private static List<IScenarioStep> parseJSpecTemplate(Document document) {
        List<IScenarioStep> templates = new ArrayList<IScenarioStep>();
        List<Element> templateNodes = document.selectNodes(StoryQName.xpathTemplateMethod);
        for (Element templateNode : templateNodes) {
            JSpecStep template = new XmlJSpecStepTemplate(templateNode);
            templates.add(template);
        }
        return templates;
    }

    /***
     * 从文本流中解析需要运行的测试场景
     * 
     * @param is
     * @param encoding 文本流编码，如果为null，则自动获取，如果自动获取失败，则使用默认编码
     * @return
     */
    public static List<IScenario> parseJSpecScenarioFrom(InputStream is, String encoding) {
        try {
            SAXReader reader = new SAXReader();
            if (StringHelper.isEmpty(encoding) == false) {
                reader.setEncoding(encoding);
            }
            Document document = reader.read(is);
            List<IScenario> scenarios = parseJSpecScenarioFrom(document);
            return scenarios;
        } catch (Exception e) {
            throw new RuntimeException("parse story xml file error.", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 从文本流中解析需要运行的测试场景
     * 
     * @param xml
     * @param encoding
     * @return
     */
    public static List<IScenario> parseJSpecScenarioFrom(String xml, String encoding) {
        StringReader strReader = new StringReader(xml);
        try {
            SAXReader reader = new SAXReader();
            if (StringHelper.isEmpty(encoding) == false) {
                reader.setEncoding(encoding);
            }
            Document document = reader.read(strReader);
            List<IScenario> scenarios = parseJSpecScenarioFrom(document);
            return scenarios;
        } catch (Exception e) {
            throw new RuntimeException("parse story xml file error.", e);
        } finally {
            strReader.close();
        }
    }
}
