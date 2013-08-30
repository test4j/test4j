package org.jtester.spec.scenario.step;

import java.util.Map;

import org.jtester.spec.inner.IScenarioStep;
import org.jtester.spec.inner.ISpecMethod.SpecMethodID;
import org.jtester.spec.inner.StepType;

public class StepMethod {
    private SpecMethodID        methodID;

    private Map<String, String> paras;

    public StepMethod(IScenarioStep step) {
        methodID = step.getSpecMethodID();
        paras = step.getParas();
    }

    public String generateMethodJava() {
        StringBuilder code = new StringBuilder();
        code.append("\t@").append(StepType.Step.name()).append("\n");
        code.append("\tpublic void ").append(methodID.getMethodName()).append("(");
        if (paras != null) {
            boolean isFirst = true;
            for (String para : paras.keySet()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    code.append(", // <br>\n\t\t");
                }
                code.append("final @Named(\"").append(para).append("\") ");
                code.append("String ").append(para);
            }
        }
        if (paras != null && paras.size() != 0) {
            code.append("//<br>\n\t");
        }
        code.append(")throws Exception {\n");
        code.append("\t\t// TODO\n");
        code.append("\t}\n");
        return code.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((methodID == null) ? 0 : methodID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StepMethod other = (StepMethod) obj;
        if (methodID == null) {
            if (other.methodID != null)
                return false;
        } else if (!methodID.equals(other.methodID))
            return false;
        return true;
    }
}
