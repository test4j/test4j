package org.jtester.spec.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jtester.spec.inner.IScenarioStep;
import org.jtester.spec.scenario.JSpecScenario;
import org.jtester.spec.scenario.step.StepMethod;
import org.jtester.tools.commons.StringHelper;

/**
 * java代码生成器
 * 
 * @author darui.wudr 2012-6-28 下午3:17:43
 */
public class JavaGenerator {
    /**
     * 根据场景列表生成TestNg java代码
     * 
     * @param javaClaz 要生成的java文件类名（全称）
     * @param scenarioLists
     * @return
     */
    public static String generateTestNgTestJava(String claz, List<JSpecScenario> scenarioLists) {
        if (claz == null) {
            throw new RuntimeException("the test java name can't be null!");
        }
        int index = claz.lastIndexOf('.');
        String name = claz;
        String pack = "";
        if (index > 0) {
            name = claz.substring(index + 1);
            pack = claz.substring(0, index);
        }
        StringBuilder java = new StringBuilder("");
        if (StringHelper.isEmpty(pack) == false) {
            java.append("package ").append(pack).append(";\n");
            java.append("\n");
        }

        java.append("import org.jtester.spec.JSpec;\n");
        java.append("import org.testng.annotations.Test;\n");
        java.append("import org.jtester.spec.step.JSpecScenario;\n");
        java.append("import org.jtester.spec.annotations.Given;\n");
        java.append("import org.jtester.spec.annotations.When;\n");
        java.append("import org.jtester.spec.annotations.Then;\n");
        java.append("\n");

        java.append("public class ").append(name).append(" extends JSpec {\n");
        // buff.append("\t@Override\n");
        java.append("\t@Test(dataProvider = \"story\", groups = \"jspec\")\n");
        java.append("\tpublic void runStory(JSpecScenario scenario) throws Exception {\n");
        java.append("\t\tthis.run(scenario);\n");
        java.append("\t}\n");

        Set<StepMethod> existed = new HashSet<StepMethod>();
        for (JSpecScenario scenario : scenarioLists) {
            generateTestMethod(java, existed, scenario);
        }
        java.append("}");
        return java.toString();
    }

    private static void generateTestMethod(StringBuilder java, Set<StepMethod> existed, JSpecScenario scenario) {
        List<IScenarioStep> steps = scenario.getSteps();
        for (IScenarioStep step : steps) {
            StepMethod method = new StepMethod(step);
            if (existed.contains(method)) {
                continue;
            }
            existed.add(method);
            String code = method.generateMethodJava();
            java.append(code);
        }
    }
}
