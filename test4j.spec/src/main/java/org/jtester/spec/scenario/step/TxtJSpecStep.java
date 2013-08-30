package org.jtester.spec.scenario.step;

import static org.jtester.spec.scenario.step.xml.MethodDescription.VAR_START;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jtester.spec.inner.IScenarioStep;
import org.jtester.spec.scenario.step.txt.LineType;
import org.jtester.spec.scenario.step.txt.ParameterParser2;
import org.jtester.tools.commons.StringHelper;

@SuppressWarnings("serial")
public class TxtJSpecStep extends JSpecStep {
    public TxtJSpecStep(String scenario, String line, LineType type) {
        super(scenario);
        String text = type.getSurfixText(line);
        if (StringHelper.isBlankOrNull(text)) {
            throw new RuntimeException("the jspec step method can't be empty.");
        }
        this.parseStepType(type);
        this.method = StringHelper.camel(text);
        this.error = null;
    }

    /**
     * 解析设置 场景步骤的类型，以及该步骤是否被执行
     * 
     * @param type
     */
    protected void parseStepType(LineType type) {
        this.type = type.getStepType();
        switch (type) {
            case Given:
            case When:
            case Then:
            case GivenTemplate:
            case WhenTemplate:
            case ThenTemplate:
                this.isSkip = false;
                break;
            case SkipGiven:
            case SkipWhen:
            case SkipThen:
                this.isSkip = true;
                break;
            default:
                throw new RuntimeException(
                        "illegal step type, the line should be start with Given(SkipGiven), When(SkipWhen), Then(SkipThen).");
        }
    }

    private boolean hasTemplate = false;

    @Override
    public void parseStep(Object content, IScenarioStep template) {
        if (!(content instanceof String)) {
            String error = String.format("The step content must be a string, but actual is ",
                    content == null ? "<null>" : content.getClass().getName());
            throw new RuntimeException(error);
        }
        this.hasTemplate = template != null;
        this.initialText = StringHelper.trim((String) content);
        this.paras = this.initParameters(template);
        String textTemplate = this.parseParameter(this.initialText);
        if (template != null) {
            textTemplate = template.getDisplayText();
        }
        this.displayText = getText(textTemplate);
    }

    protected String getText(String textTemplate) {
        String text = textTemplate;
        for (Map.Entry<String, String> entry : this.paras.entrySet()) {
            text = text.replace(VAR_START + entry.getKey() + "}", entry.getKey() + "=" + entry.getValue());
        }
        return text;
    }

    /**
     * 解析方法的参数列表
     * 
     * @param element
     */
    private String parseParameter(String text) {
        Map<String, String> paras = new LinkedHashMap<String, String>();
        String textTemplate = ParameterParser2.parserParameter(text, paras);
        for (Map.Entry<String, String> para : paras.entrySet()) {
            String name = para.getKey();
            if (this.hasTemplate && !this.paras.containsKey(name)) {
                String error = String.format("the template[%s] doesn't contain parameter[%s].", this.method, name);
                throw new RuntimeException(error);
            }
            if (!this.hasTemplate && this.paras.containsKey(name)) {
                String error = String.format("the method[%s] have duplicated parameter[%s].", this.method, name);
                throw new RuntimeException(error);
            }
            String value = para.getValue();
            this.paras.put(name, value);
        }
        return textTemplate;
    }

    public static class TxtJSpecStepTemplate extends TxtJSpecStep {
        public TxtJSpecStepTemplate(String line, LineType type) {
            super("step template", line, type);
        }

        @Override
        protected String getText(String textTemplate) {
            return textTemplate;
        }
    }
}
