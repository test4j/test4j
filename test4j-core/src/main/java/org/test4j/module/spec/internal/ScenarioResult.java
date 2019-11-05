package org.test4j.module.spec.internal;


import lombok.Getter;
import lombok.Setter;
import org.test4j.function.SExecutor;
import org.test4j.module.ICore;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.tools.commons.StringHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScenarioResult implements Serializable {
    private static List<String> Filter_List = new ArrayList();

    static {
        addFilter("*.lambda.");
        addFilter("java.util.");
        addFilter("org.test4j.");
        addFilter("mockit.integration.");
        addFilter("org.springframework.");
        addFilter("*\\$\\$EnhancerByCGLIB\\$\\$*");

    }

    @Setter
    private String scenarioName;

    private List<StepResult> steps = new ArrayList<>();

    @Getter
    private StepResult lastStep = null;

    @Getter
    @Setter
    private Throwable exception;

    public ScenarioResult(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    private static void addFilter(String filter) {
        Filter_List.add("\\s*at\\s+" +
                filter.replaceAll("\\.", "\\\\.").replaceAll("\\*", "\\.\\*") +
                "[^\\)]*\\)"
        );
    }

    /**
     * 执行lambda表达式
     *
     * @param type
     * @param description
     * @param lambda
     * @param eKlass
     * @throws Exception
     */
    public void doStep(StepType type, String description, SExecutor lambda, Class<? extends Throwable> eKlass) throws Exception {
        StepResult result = this.addResult(type, description);
        try {
            lambda.doIt();
            if (eKlass != null) {
                ICore.want.fail("expected exception: " + eKlass.getName());
            }
        } catch (Throwable e) {
            if(eKlass != null){
                SpecContext.setExpectedException(e);
            }else {
                result.setError(e);
                throw e;
            }
        }
    }

    /**
     * 执行lambda表达式
     *
     * @param type
     * @param lambda
     * @param eKlass
     *
     * @throws Exception
     */
    public void doStep(StepType type, SExecutor lambda, Class<? extends Throwable> eKlass) throws Exception {
        this.doStep(type, null, lambda, eKlass);
    }

    private StepResult addResult(StepType type, String description) {
        this.lastStep = new StepResult(this, type, description);
        this.steps.add(this.lastStep);
        return this.lastStep;
    }

    public boolean isFailure() {
        return this.exception != null;
    }

    /**
     * 输出测试结果
     */
    public void print() {
        StringBuilder buff = new StringBuilder();
        buff.append("\n\n")
                .append("=========Scenario: ").append(this.scenarioName).append(" =============\n");
        steps.forEach(stepResult -> {
            buff.append(stepResult.toString()).append("\n");
        });
        if (exception != null) {
            buff.append(StringHelper.exceptionTrace(this.exception, Filter_List));
        }
        MessageHelper.info(buff.toString());
    }
}
