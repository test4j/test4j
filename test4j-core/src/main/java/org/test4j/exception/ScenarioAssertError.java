package org.test4j.exception;

import org.test4j.module.spec.internal.IScenario;
import org.test4j.tools.commons.StringHelper;

import java.util.List;

public class ScenarioAssertError extends AssertionError {

    private static final long serialVersionUID = 7714583337455735690L;

    public ScenarioAssertError(IScenario scenario, List<Throwable> errors) {
        super(getError(scenario, errors));
    }

    private static String getError(IScenario scenario, List<Throwable> errors) {
        StringBuilder buff = new StringBuilder();

        buff.append(scenario.toString()).append("\n");
        if (errors.size() == 1) {
            buff.append(" throw an exception:");
        } else {
            buff.append(" throw " + errors.size() + " exceptions:");
        }
        for (Throwable error : errors) {
            buff.append("\n");
            buff.append(StringHelper.toString(error));
        }
        buff.append("\n").append(scenario.toString()).append(" throw errors at:");
        return buff.toString();
    }
}
