package org.jtester.spec.scenario;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.module.ICore.DataMap;
import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.StepType;
import org.jtester.spec.scenario.step.JSpecStep;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ResourceHelper;
import org.testng.annotations.Test;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class XmlJSpecScenarioTest extends JTester {

    @Test
    public void testParseJSpecScenarioFrom() throws Exception {
        InputStream is = ResourceHelper.getResourceAsStream(XmlJSpecScenarioTest.class, "test-story.xml");
        List<IScenario> scenarios = XmlJSpecScenario.parseJSpecScenarioFrom(is, "utf-8");
        List list = Arrays.asList( // <br>
                new EmptyJSpecScenario("用例场景一", false, "场景详细描述"), /** <br> */
                new EmptyJSpecScenario("用例场景二", true, "场景详细描述"));
        want.list(scenarios).sizeEq(2).reflectionEqMap(list, EqMode.IGNORE_ORDER);
        List steps1 = Arrays.asList(
                newStep(StepType.Given, "doInitMethod1", false).setDisplayText("准备数据步骤一\n参数一=abc,\n参数二=123."),// <br>
                newStep(StepType.Given, "doInitMethod2", true),// <br>
                newStep(StepType.When, "doSomething", false),// <br>
                newStep(StepType.Then, "checkResult", false));
        want.list(scenarios.get(0).getSteps()).reflectionEqMap(steps1, EqMode.IGNORE_ORDER);
        want.list(scenarios.get(1).getSteps()).reflectionEqMap(newStep(StepType.Given, "doTemplateMethod", false));
    }

    private static EmptyJSpecStep newStep(StepType type, String name, boolean isSkip) {
        return new EmptyJSpecStep(type, name, isSkip);
    }

    @Test
    public void testParseJSpecTemplate() throws Exception {
        Document document = new SAXReader().read(ResourceHelper.getResourceAsStream(XmlJSpecScenarioTest.class,
                "template-test.xml"));
        List<JSpecStep> templates = reflector.invoke(XmlJSpecScenario.class, "parseJSpecTemplate", document);
        want.list(templates).sizeEq(3).reflectionEqMap(3, new DataMap() {
            {
                this.put("scenario", "step template");
                this.put("method", "templateOne", "templateTwo", "templateThree");
                this.put("type", StepType.Given, StepType.When, StepType.Then);
                this.put("paras", new DataMap() {
                    {
                        this.put("参数1", "{\"name1\":\"value1\",\"name2\":\"value2\"}");
                        this.put("参数2", "[1,2,3,4]");
                    }
                }, new DataMap(), new DataMap());
                this.put("displayText", "初始化步骤，有2个参数$_#_@_&{参数1}$_#_@_&{参数2}", "", "");
            }
        });
    }

    @Test
    public void testParseJSpecTemplate_UnExisted() throws Exception {
        Document document = new SAXReader().read(ResourceHelper.getResourceAsStream(XmlJSpecScenarioTest.class,
                "template-unexisted.xml"));
        List<JSpecStep> templates = reflector.invoke(XmlJSpecScenario.class, "parseJSpecTemplate", document);
        want.list(templates).sizeEq(0);
    }
}

@SuppressWarnings({ "serial" })
class EmptyJSpecScenario extends DataMap {
    public EmptyJSpecScenario(String scenario, boolean skip, String descript) {
        this.put("scenario", scenario);
        this.put("isSkip", skip);
        this.put("description", descript);
    }
}

@SuppressWarnings("serial")
class EmptyJSpecStep extends DataMap {
    public EmptyJSpecStep(StepType type, String name, boolean isSkip) {
        this.put("type", type);
        this.put("method", name);
        this.put("isSkip", isSkip);
    }

    public EmptyJSpecStep setDisplayText(String text) {
        this.put("displayText", text);
        return this;
    }
}
