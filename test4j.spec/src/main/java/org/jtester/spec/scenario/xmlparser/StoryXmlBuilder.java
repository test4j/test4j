package org.jtester.spec.scenario.xmlparser;

import java.util.List;

import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jtester.spec.scenario.xmlparser.entity.ScenarioMethod;
import org.jtester.spec.scenario.xmlparser.entity.StoryScenario;
import org.jtester.spec.scenario.xmlparser.entity.TemplateMethod;
import org.jtester.spec.util.XmlHelper;
import org.jtester.spec.util.XmlHelper.MethodNode;
import org.jtester.tools.commons.StringHelper;

/**
 * xml形式的用例文件构建器
 * 
 * @author darui.wudr 2012-7-12 下午1:59:41
 */
public class StoryXmlBuilder implements StoryQName {
    /**
     * 新增用例场景
     * 
     * @param parent
     * @param scenario
     */
    public static String addScenario(Element parent, StoryScenario scenario) {
        Element element = parent.addElement(nodeScenario);
        Element scenarioDescription = element.addElement(nodeDescription);

        String desc = filteCDATA(scenario.getDescription());
        CDATA cdata = DocumentHelper.createCDATA(desc);
        scenarioDescription.clearContent();
        scenarioDescription.add(cdata);

        modifyScenario(element, scenario);
        String path = StoryXmlBuilder.getXPathID(element);
        return path;
    }

    /**
     * 往document文档中追加一个场景节点
     * 
     * @param document
     * @param scenario
     */
    public static String addScenario(Document document, StoryScenario scenario) {
        Element story = ((Element) document.selectSingleNode(StoryQName.xpathRoot));
        String path = StoryXmlBuilder.addScenario(story, scenario);

        return path;
    }

    /**
     * 修改用例场景
     * 
     * @param scenarioNode
     * @param scenario
     */
    public static void modifyScenario(Element scenarioNode, StoryScenario scenario) {
        scenarioNode.addAttribute(attrName, scenario.getScenarioName());
        scenarioNode.addAttribute(attrSkip, String.valueOf(scenario.isSkip()));
        Element descNode = (Element) scenarioNode.selectSingleNode(nodeDescription);
        if (descNode == null) {
            descNode = scenarioNode.addElement(StoryQName.nodeDescription);
        }

        String desc = filteCDATA(scenario.getDescription());
        CDATA cdata = DocumentHelper.createCDATA(desc);
        descNode.clearContent();
        descNode.add(cdata);
    }

    /**
     * 新增用例步骤
     * 
     * @param parent
     * @param method
     */
    public static String addScenarioMethod(Element parent, ScenarioMethod method) {
        Element element = parent.addElement(nodeScenarioMethod);
        modifyScenarioMethod(element, method);
        String path = StoryXmlBuilder.getXPathID(element);
        return path;
    }

    /**
     * 修改用例步骤
     * 
     * @param methodNode
     * @param method
     */
    public static void modifyScenarioMethod(Element methodNode, ScenarioMethod method) {
        // 这一行必须在最前面，防止内容解析出错时把相应的节点内容先清空了
        List<MethodNode> nodes = XmlHelper.parseMethodContext(method.getInitialText());

        methodNode.addAttribute(attrName, method.getMethodName());
        methodNode.addAttribute(attrType, method.getMethodType());
        methodNode.addAttribute(attrSkip, String.valueOf(method.isSkip()));
        addMethodChildren(methodNode, nodes);
    }

    /**
     * 修改模板方法
     * 
     * @param templateNode
     * @param template
     */
    public static void modifyTemplateMethod(Element templateNode, TemplateMethod template) {
        List<MethodNode> nodes = XmlHelper.parseMethodContext(template.getInitialText());

        templateNode.addAttribute(attrName, template.getMethodName());
        templateNode.addAttribute(attrType, template.getMethodType());
        addMethodChildren(templateNode, nodes);
    }

    static void addMethodChildren(Element method, List<MethodNode> children) {
        method.clearContent();
        for (MethodNode item : children) {
            if (item.isPara()) {
                Element para = DocumentHelper.createElement("para");
                para.addAttribute("name", item.getParaName());
                String value = filteCDATA(item.getText());
                CDATA cdata = DocumentHelper.createCDATA(value);
                para.add(cdata);
                method.add(para);
            } else {
                String text = filteCDATA(item.getText());
                CDATA cdata = DocumentHelper.createCDATA(text);
                method.add(cdata);
            }
        }
    }

    /**
     * 新建用例故事
     * 
     * @param description
     * @return
     */
    public static Document createStory(String description) {
        StringBuilder buff = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        buff.append("<story>\n");
        String desc = filteCDATA(description);
        buff.append(String.format("\t<description>%s</description>\n", desc));
        buff.append("\t<templates>\n\t</templates>\n");
        buff.append("\t<scenarios>\n\t</scenarios>\n");
        buff.append("</story>");
        Document document = XmlHelper.buildFromString(buff.toString());
        return document;
    }

    /**
     * 修改用例描述
     * 
     * @param textNode
     * @param text
     */
    public static void modifyStoryDescription(Element textNode, String text) {
        String desc = filteCDATA(text);
        CDATA cdata = DocumentHelper.createCDATA(desc);
        textNode.clearContent();
        textNode.add(cdata);
    }

    /**
     * 在xml文档中移除xpath指定的节点
     * 
     * @param document
     * @param xPathID
     */
    public static void removeElement(Document document, String xPathID) {
        Element node = (Element) document.selectSingleNode(xPathID);
        if (node != null) {
            node.detach();
        }
    }

    /**
     * 标准化节点的xpath表达式
     * 
     * @param xpath
     * @return
     */
    public static String convertPathID(String xpath) {
        String[] items = xpath.split("/");
        StringBuilder buff = new StringBuilder("");
        for (String item : items) {
            buff.append("/").append(item);
            if (item.equals(StoryQName.nodeTemplateMethod) || item.equals(StoryQName.nodeScenarioMethod)
                    || item.equals(StoryQName.nodeScenario)) {
                buff.append("[1]");
            }
        }
        return buff.toString().substring(1);
    }

    /**
     * 返回节点的标准化xpath表达式
     * 
     * @param node
     * @return
     */
    public static String getXPathID(Element node) {
        String path = node.getUniquePath();
        String xpath = convertPathID(path);
        return xpath;
    }

    /**
     * 新增模板方法
     * 
     * @param parent
     * @param template
     */
    public static String addTemplateMethod(Element parent, TemplateMethod template) {
        Element element = parent.addElement(nodeTemplateMethod);
        modifyTemplateMethod(element, template);
        String path = StoryXmlBuilder.getXPathID(element);
        return path;
    }

    /**
     * 往document文档中追加一个模板方法
     * 
     * @param document
     * @param template
     */
    public static String addTemplateMethod(Document document, TemplateMethod template) {
        Element templatesNode = ((Element) document.selectSingleNode(StoryQName.xpathTemplate));
        if (templatesNode == null) {
            templatesNode = document.getRootElement().addElement(StoryQName.nodeMethodTemplate);
        }
        String path = StoryXmlBuilder.addTemplateMethod(templatesNode, template);
        return path;
    }

    /**
     * 往文档父节点parentXPath下追加一个子节点
     * 
     * @param document
     * @param parentXPathID
     * @param method
     */
    public static String addScenarioMethod(Document document, String parentXPathID, ScenarioMethod method) {
        Element parent = (Element) document.selectSingleNode(parentXPathID);
        if (parent == null) {
            return parentXPathID;
        }
        String path = addScenarioMethod(parent, method);
        return path;
    }

    /**
     * 更新指定文档中的节点内容
     * 
     * @param document
     * @param mimeXPathID
     * @param method
     */
    public static String uptScenarioMethod(Document document, String mimeXPathID, ScenarioMethod method) {
        Element myself = (Element) document.selectSingleNode(mimeXPathID);
        if (myself == null) {
            return mimeXPathID;
        }
        modifyScenarioMethod(myself, method);
        return mimeXPathID;
    }

    /**
     * 在文档brother节点前插入一个新节点
     * 
     * @param document
     * @param brother
     * @param method
     */
    @SuppressWarnings("unchecked")
    public static String insScenarioMethod(Document document, String brotherXPathID, ScenarioMethod method) {
        Element brother = (Element) document.selectSingleNode(brotherXPathID);
        if (brother == null) {
            return brotherXPathID;
        }
        int index = brother.getParent().elements().indexOf(brother);
        Element newNode = (Element) brother.clone();
        modifyScenarioMethod(newNode, method);
        brother.getParent().elements().add(index, newNode);
        String path = StoryXmlBuilder.getXPathID(newNode);
        return path;
    }

    /**
     * 更新文档中xpathID节点的场景内容
     * 
     * @param document
     * @param xpathID
     * @param scenario
     */
    public static String uptStoryScenario(Document document, String xpathID, StoryScenario scenario) {
        if (!xpathID.startsWith(StoryQName.xpathScenario) || xpathID.contains(StoryQName.nodeScenarioMethod)) {
            return xpathID;
        }
        Element scenarioNode = (Element) document.selectSingleNode(xpathID);
        if (scenarioNode == null) {
            return xpathID;
        }
        StoryXmlBuilder.modifyScenario(scenarioNode, scenario);
        return xpathID;
    }

    /**
     * 复制xpathID的用例场景内容（包括具体步骤）
     * 
     * @param document
     * @param xpathID
     * @param scenario
     */
    public static String cpyStoryScenario(Document document, String xpathID, StoryScenario scenario) {
        if (!xpathID.startsWith(StoryQName.xpathScenario) || xpathID.contains(StoryQName.nodeScenarioMethod)) {
            return xpathID;
        }
        Element oldScenario = (Element) document.selectSingleNode(xpathID);
        if (oldScenario == null) {
            return xpathID;
        }
        String pathID = StoryXmlBuilder.addScenario(document, scenario);
        Element newScenario = (Element) document.selectSingleNode(pathID);
        StoryXmlBuilder.cpyScenarioSteps(oldScenario, newScenario);
        return pathID;
    }

    /**
     * 从场景oldScenario拷贝步骤到newScenario场景
     * 
     * @param oldScenario
     * @param newScenario
     */
    @SuppressWarnings("unchecked")
    private static void cpyScenarioSteps(Element oldScenario, Element newScenario) {
        List<Element> methods = oldScenario.selectNodes(StoryQName.nodeScenarioMethod);
        for (Element method : methods) {
            Element newMethod = method.createCopy();
            newScenario.add(newMethod);
        }
    }

    /**
     * 往文档场景列表parentID中追加一个子节点scenario
     * 
     * @param document
     * @param parentID
     * @param scenario
     */
    public static String addStoryScenario(Document document, String parentID, StoryScenario scenario) {
        if (!parentID.equals(StoryQName.nodeStory)) {
            return parentID;
        }
        Element scenariosNode = (Element) document.selectSingleNode(StoryQName.xpathRoot);
        String pathID = StoryXmlBuilder.addScenario(scenariosNode, scenario);
        return pathID;
    }

    /**
     * 更新文档中模板方法内容
     * 
     * @param document
     * @param xpathID
     * @param method
     */
    public static String uptTemplateMethod(Document document, String xpathID, TemplateMethod method) {
        if (!xpathID.startsWith(StoryQName.xpathTemplateMethod)) {
            return xpathID;
        }
        Element templateNode = (Element) document.selectSingleNode(xpathID);
        if (templateNode == null) {
            return xpathID;
        }
        StoryXmlBuilder.modifyTemplateMethod(templateNode, method);
        return xpathID;
    }

    /**
     * 在文档中新增一个模板节点
     * 
     * @param document
     * @param xpathID
     * @param method
     */
    public static String addTemplateMethod(Document document, String xpathID, TemplateMethod method) {
        if (!xpathID.equals(StoryQName.xpathTemplate)) {
            return xpathID;
        }
        String methodPathID = StoryXmlBuilder.addTemplateMethod(document, method);
        return methodPathID;
    }

    /**
     * 更新文档中xpathID节点的场景内容<br>
     * 
     * @param document
     * @param xpathID
     * @param scenario
     */
    public static void uptStoryDescription(Document document, String description) {
        Element node = (Element) document.selectSingleNode(StoryQName.xpathDescription);
        if (node == null) {
            node = document.getRootElement().addElement(StoryQName.nodeDescription);
        }
        StoryXmlBuilder.modifyStoryDescription(node, description);
    }

    /**
     * 转换&lt;![DATA[ 和 ]]&gt;
     * 
     * @param text
     * @return
     */
    public static String filteCDATA(String text) {
        if (StringHelper.isBlankOrNull(text)) {
            return "";
        } else {
            return text.replaceAll("\\]\\]>", "]]&gt;").replaceAll("<!\\[CDATA\\[", "&lt;![CDATA[");
        }
    }
}
