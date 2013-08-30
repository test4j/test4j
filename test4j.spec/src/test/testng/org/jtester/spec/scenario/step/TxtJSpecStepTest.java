package org.jtester.spec.scenario.step;

import java.io.InputStream;
import java.util.List;

import org.jtester.spec.inner.IScenario;
import org.jtester.spec.inner.StepType;
import org.jtester.spec.scenario.TxtJSpecScenario;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ResourceHelper;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
public class TxtJSpecStepTest extends JTester {
    /**
     * 验证txt文件格式的模板方法
     * 
     * @throws Exception
     */
    @Test
    public void testParseStepType() throws Exception {
        InputStream is = ResourceHelper.getResourceAsStream(XmlJSpecStepTest.class, "template-step.story");
        List<IScenario> scenarios = TxtJSpecScenario.parseJSpecScenarioFrom(is, "utf-8");
        want.list(scenarios).sizeEq(2).reflectionEqMap(2, new DataMap() {
            {
                this.put("scenario", "scenario1", "scenario2");
            }
        });
        want.list(scenarios.get(0).getSteps()).reflectionEqMap(2, new DataMap() {
            {
                this.put("method", "whenMethod");
                this.put("type", StepType.When, StepType.Then);
                this.put("paras", new DataMap() {
                    {
                        this.put("参数1", "[1,2,3]");
                        this.put("参数2", "[2,3,4]");
                    }
                }, new DataMap() {
                    {
                        this.put("参数", "234");
                    }
                });
                this.put("displayText", "模板方法测试，参数一参数1=[1,2,3],参数二参数2=[2,3,4]", "验证方法，同名不同类型\n参数=234");
            }
        });
        want.list(scenarios.get(1).getSteps()).reflectionEqMap(2, new DataMap() {
            {
                this.put("method", "givenMethod", "whenMethod");
                this.put("type", StepType.Given, StepType.When);
                this.put("paras", new DataMap() {
                    {
                        this.put("参数", "init");
                    }
                }, new DataMap() {
                    {
                        this.put("参数1", "[1,2,3]");
                        this.put("参数2", "[4,5,6]");
                    }
                });
            }
        });
    }
}
