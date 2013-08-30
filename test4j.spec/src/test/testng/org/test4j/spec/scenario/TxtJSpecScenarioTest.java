package org.test4j.spec.scenario;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import mockit.Mock;

import org.test4j.spec.inner.IScenario;
import org.test4j.spec.inner.StepType;
import org.test4j.spec.scenario.step.JSpecStep;
import org.test4j.testng.Test4J;
import org.test4j.tools.commons.ResourceHelper;
import org.testng.annotations.Test;

@SuppressWarnings({ "serial" })
public class TxtJSpecScenarioTest extends Test4J {

    @Test(groups = "jspec")
    public void testParseJSpecScenarioFrom_NoScenario() throws Exception {
        String lines = "line1" + "\n" + "line2";
        List<IScenario> list = TxtJSpecScenario.parseJSpecScenarioFrom(lines);
        want.list(list).sizeEq(0);
    }

    @Test(groups = "jspec")
    public void testParseJSpecScenarioFrom() throws Exception {
        String file = System.getProperty("user.dir")
                + "/src/test/resources/org/test4j/spec/scenario/TxtJSpecScenarioTest.testParseSpecScenarioFrom.story";
        want.file(file).isExists();
        new MockUp<TxtJSpecScenario>() {
            int   index = 0;
            int[] count = new int[] { 12, 13 };

            @Mock(invocations = 2)
            public void $init(List<String> lines, List<JSpecStep> templates) {
                want.list(lines).sizeEq(count[index++]);
            }
        };
        List<IScenario> list = TxtJSpecScenario.parseJSpecScenarioFrom(new FileInputStream(file), null);
        want.list(list).sizeEq(2);
    }

    @Test
    public void testJSpecScenario() throws Exception {
        String file = System.getProperty("user.dir")
                + "/src/test/resources/org/test4j/spec/scenario/TxtJSpecScenarioTest.testJSpecScenario.story";
        want.file(file).isExists();
        String[] arr = ResourceHelper.readLinesFromFile("file://" + file, "utf8");
        List<String> lines = new ArrayList<String>();
        for (String line : arr) {
            lines.add(line);
        }

        TxtJSpecScenario jspec = new TxtJSpecScenario(lines, null);
        want.object(jspec).propertyEq("scenario", "场景描述");
        List<JSpecStep> steps = reflector.getField(jspec, "steps");
        want.list(steps).sizeEq(4).propertyEqMap(4, new DataMap() {
            {
                this.put("type", StepType.Given, StepType.When, StepType.When, StepType.Then);
                this.put("method", "testMethod", "applyMethod", "执行方法", "checkMethod");
            }
        });
        want.list(steps).sizeEq(4).propertyEqMap(4, new DataMap() {
            {
                this.put("paras",// <br>
                        new LinkedHashMap<String, String>() {
                            {
                                this.put("变量1", "变量1");
                                this.put("变量2", "变,分行\n量2");
                            }
                        }, // <br>
                        new LinkedHashMap<String, String>(), // <br>
                        new LinkedHashMap<String, String>(),// <br>
                        new LinkedHashMap<String, String>() {
                            {
                                this.put("变量1", "变量1");
                                this.put("变量2", "变,分行\n量2");
                            }
                        });
            }
        });
    }

    @Test
    public void testParseJSpecScenarioFrom_ContainSkipScenario() throws Exception {
        String file = System.getProperty("user.dir") + "/src/test/resources/org/test4j/spec/txt/SkipScenarioDemo.story";
        want.file(file).isExists();
        List<IScenario> list = TxtJSpecScenario.parseJSpecScenarioFrom(new FileInputStream(file), null);
        want.list(list).sizeEq(2).propertyEq("isSkip", new Boolean[] { true, false });
    }
}
