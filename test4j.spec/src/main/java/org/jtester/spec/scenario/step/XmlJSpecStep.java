package org.jtester.spec.scenario.step;

import static org.jtester.spec.scenario.step.xml.MethodDescription.VAR_START;

import java.util.Iterator;
import java.util.List;

import org.dom4j.CharacterData;
import org.dom4j.Element;
import org.jtester.spec.inner.IScenarioStep;
import org.jtester.spec.inner.StepType;
import org.jtester.spec.scenario.step.xml.MethodDescription;
import org.jtester.tools.commons.StringHelper;

@SuppressWarnings("serial")
public class XmlJSpecStep extends JSpecStep {
    /**
     * 是否有模板
     */
    private boolean hasTemplate;

    public XmlJSpecStep(String scenario, Element methodNode, List<IScenarioStep> templates) {
        super(scenario);
        String methodName = methodNode.attributeValue("name");
        if (StringHelper.isBlankOrNull(methodName)) {
            throw new RuntimeException("the jspec step method can't be empty.");
        }
        this.method = StringHelper.camel(methodName);
        this.parseStepType(methodNode);
        IScenarioStep template = JSpecStep.findTemplate(templates, this.method, this.type);
        this.hasTemplate = template != null;
        this.parseStep(methodNode, template);
    }

    @Override
    public void parseStep(Object content, IScenarioStep template) {
        if (!(content instanceof Element)) {
            String error = String.format("the step content of XmlJSpecStep must be a xml element, but actual is %s",
                    content == null ? "<null>" : content.getClass().getName());
            throw new RuntimeException(error);
        }
        Element element = (Element) content;
        this.paras = this.initParameters(template);
        this.parseParameter(element);
        this.displayText = this.getText(element, template);
    }

    /**
     * 解析设置 场景步骤的类型，以及该步骤是否被执行
     * 
     * @param type
     */
    private void parseStepType(Element element) {
        String type = element.attributeValue("type");
        if (StringHelper.isEmpty(type)) {
            this.type = StepType.Step;
        } else {
            type = type.trim();
            if ("given".equalsIgnoreCase(type)) {
                this.type = StepType.Given;
            } else if ("when".equalsIgnoreCase(type)) {
                this.type = StepType.When;
            } else if ("then".equalsIgnoreCase(type)) {
                this.type = StepType.Then;
            } else {
                throw new RuntimeException(
                        "illegal step type, the method type must be one of following values: given, when, then. but actual is "
                                + type + ".");
            }
        }
        String skip = element.attributeValue("skip", "false");
        this.isSkip = "true".equalsIgnoreCase(skip.trim());
    }

    /**
     * 解析方法的参数列表
     * 
     * @param element
     */
    @SuppressWarnings("unchecked")
    private void parseParameter(Element element) {
        List<Element> paraNodes = element.selectNodes("para");
        for (Element paraNode : paraNodes) {
            String name = paraNode.attributeValue("name");
            if (StringHelper.isEmpty(name)) {
                String error = String.format("the parameter name of method[%s.%s] can't be empty!", this.scenario,
                        this.method);
                throw new RuntimeException(error);
            }
            if (this.hasTemplate && !this.paras.containsKey(name)) {
                String error = String.format("the template[%s] doesn't contain parameter[%s].", this.method, name);
                throw new RuntimeException(error);
            }
            if (!this.hasTemplate && this.paras.containsKey(name)) {
                String error = String.format("the method[%s] have duplicated parameter[%s].", this.method, name);
                throw new RuntimeException(error);
            }
            String json = paraNode.getText().trim();
            this.paras.put(name, json);
        }
    }

    /**
     * 获取方法描述信息
     * 
     * @param xml
     * @return
     */
    protected String getText(Element element, IScenarioStep template) {
        String templateText = "";
        if (template != null) {
            templateText = template.getDisplayText();
        } else {
            templateText = getTemplateText(this.method, element);
        }
        String text = new MethodDescription(templateText, this.paras).getMethodDisplayText();
        return text;
    }

    /**
     * 场景步骤模板
     * 
     * @author darui.wudr
     */
    public static class XmlJSpecStepTemplate extends XmlJSpecStep {
        public XmlJSpecStepTemplate(Element methodNode) {
            super("step template", methodNode, null);
        }

        @Override
        protected String getText(Element element, IScenarioStep template) {
            String text = getTemplateText(this.method, element);
            return text;
        }
    }

    /**
     * 获取方法描述信息的模板内容，变量用$_#_@{var} 表示
     * 
     * @param element
     * @return
     */
    public static String getTemplateText(String method, Element element) {
        StringBuilder buff = new StringBuilder();
        for (Iterator<?> it = element.nodeIterator(); it.hasNext();) {
            Object item = it.next();
            if (item instanceof CharacterData) {
                String text = ((CharacterData) item).getText().trim();
                //text = StoryFeature.convetTextToHTML(text);
                buff.append(text);
            } else if (item instanceof Element) {
                Element para = (Element) item;
                String tag = para.getName();
                if ("para".equalsIgnoreCase(tag.trim())) {
                    String paraName = para.attributeValue("name");
                    if (StringHelper.isEmpty(paraName)) {
                        throw new RuntimeException("the parameter name of method[" + method + "] should be specified.");
                    }
                    buff.append(VAR_START + paraName + "}");
                } else {
                    buff.append(para.getTextTrim());
                }
            }
        }
        return buff.toString();
    }
}
