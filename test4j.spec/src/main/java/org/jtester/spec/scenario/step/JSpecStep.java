package org.jtester.spec.scenario.step;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jtester.spec.exceptions.SkipStepException;
import org.jtester.spec.inner.IScenarioStep;
import org.jtester.spec.inner.ISpecMethod.SpecMethodID;
import org.jtester.spec.inner.StepType;
import org.jtester.spec.util.ParaConverter;
import org.jtester.tools.commons.StringHelper;

@SuppressWarnings("serial")
public abstract class JSpecStep implements IScenarioStep, Serializable {
    protected StepType                      type;

    protected String                        method;

    protected String                        initialText;

    protected String                        displayText;

    protected LinkedHashMap<String, String> paras;

    protected boolean                       isSkip;

    protected Throwable                     error;

    protected String                        scenario;

    protected JSpecStep(String scenario) {
        this.scenario = scenario;
        this.error = null;
    }

    @Override
    public void setError(Throwable error) {
        this.error = error;
    }

    @Override
    public boolean isSuspend() {
        return isSkip || (this.error instanceof SkipStepException);
    }

    @Override
    public boolean isSuccess() {
        return this.error == null;
    }

    @Override
    public boolean isFailure() {
        return this.error != null && !(this.error instanceof SkipStepException);
    }

    @Override
    public Throwable getError() {
        return this.error;
    }

    @Override
    public Object[] getArguments(List<String> paraNameds, List<Type> paraTypes) {
        List<Object> values = new ArrayList<Object>();
        for (int index = 0; index < paraNameds.size(); index++) {
            String paraNamed = paraNameds.get(index);
            if (this.paras.containsKey(paraNamed) == false) {
                String keys = StringHelper.merger(this.paras.keySet(), ',');
                String error = String.format("can't find parameter %s, the existed parameters are [%s].", paraNamed,
                        keys);
                throw new RuntimeException(error);
            }
            String paraValue = this.paras.get(paraNamed);
            try {
                Type paraType = paraTypes.get(index);
                Object value = ParaConverter.convert(paraValue, paraType);
                isSubType(value, paraType);
                values.add(value);
            } catch (Throwable e) {
                String err = String.format("the json\n %s \n covert to parameter @Named(\"%s\") error:%s", paraValue,
                        paraNamed, e.getMessage());
                throw new RuntimeException(err, e);
            }
        }
        return values.toArray(new Object[0]);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void isSubType(Object value, Type paraType) {
        Class clazType = null;
        if (paraType instanceof Class) {
            clazType = (Class) paraType;
        } else if (paraType instanceof ParameterizedType) {
            clazType = (Class) ((ParameterizedType) paraType).getRawType();
        }
        if (value != null && clazType != null && !clazType.isAssignableFrom(value.getClass())) {
            String err = String.format("convert to type[%s] error", clazType.getName());
            throw new RuntimeException(err);
        }
    }

    /**
     * 解析场景步骤的具体内容<br>
     * etc: 参数, 描述等
     * 
     * @param content
     */
    public abstract void parseStep(Object content, IScenarioStep template);

    @Override
    public SpecMethodID getSpecMethodID() {
        SpecMethodID id = new SpecMethodID(method, paras.size());
        return id;
    }

    @Override
    public StepType getType() {
        return type;
    }

    public String getInitialText() {
        return initialText;
    }

    @Override
    public String getDisplayText() {
        return displayText;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public LinkedHashMap<String, String> getParas() {
        return paras;
    }

    @Override
    public String toString() {
        return type + " method[" + method + "], skip status[" + isSkip + "]";
    }

    public String toTxtString() {
        StringBuilder buff = new StringBuilder();
        if (this.error == null) {
            buff.append("\tSUCCESS ");
        } else if (this.error == SkipStepException.instance) {
            buff.append("\tSUSPEND ");
        } else {
            buff.append("\tFAILURE ");
        }
        buff.append(type.name()).append("\t").append(this.method);
        buff.append("\n\t\t").append(this.displayText).append("\n");
        if (this.error != null && this.error != SkipStepException.instance) {
            buff.append("\tError:").append(this.error.getMessage()).append("\n");
        }
        return buff.toString();
    }

    public static IScenarioStep findTemplate(List<IScenarioStep> templates, String method, StepType type) {
        if (templates == null || method == null || type == null) {
            return null;
        }
        for (IScenarioStep template : templates) {
            if (method.equals(template.getMethod()) && type.equals(template.getType())) {
                return template;
            }
        }
        return null;
    }

    /**
     * 根据模板初始化参数内容
     * 
     * @param template
     * @return
     */
    protected LinkedHashMap<String, String> initParameters(IScenarioStep template) {
        LinkedHashMap<String, String> inits = new LinkedHashMap<String, String>();
        if (template == null) {
            return inits;
        }
        for (Map.Entry<String, String> para : template.getParas().entrySet()) {
            inits.put(para.getKey(), para.getValue());
        }
        return inits;
    }
}
