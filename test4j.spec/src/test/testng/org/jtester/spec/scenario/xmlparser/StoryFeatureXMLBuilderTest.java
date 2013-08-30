package org.jtester.spec.scenario.xmlparser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.spec.inner.StepType;
import org.jtester.spec.scenario.xmlparser.entity.ScenarioList;
import org.jtester.spec.scenario.xmlparser.entity.ScenarioMethod;
import org.jtester.spec.scenario.xmlparser.entity.StoryDescription;
import org.jtester.spec.scenario.xmlparser.entity.StoryScenario;
import org.jtester.spec.scenario.xmlparser.entity.TemplateList;
import org.jtester.spec.scenario.xmlparser.entity.TemplateMethod;
import org.jtester.spec.util.XmlHelper;
import org.jtester.testng.JTester;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
public class StoryFeatureXMLBuilderTest extends JTester {
	private Document document;

	@BeforeMethod
	public void initDocument() {
		this.document = XmlHelper.buildFromClasspath("org/jtester/spec/scenario/test-story.xml", "utf-8");
	}

	@Test
	public void testBuild() {
		StoryFeature story = new StoryFeatureXMLBuilder(document).build();
		want.object(story).reflectionEqMap(new DataMap() {
			{
				this.put("description", new StoryDescription("用例故事描述"));
				this.put("templates", new TemplateList() {
					{
						this.add(new TemplateMethod("do template method", StepType.Given).setInitialText("模板内容"));
						this.add(new TemplateMethod("do template method2", StepType.Then).setInitialText("模板内容"));
					}
				});
				this.put("scenarios", new ScenarioList() {
					{
						this.addScenario(new StoryScenario("用例场景一", false) {
							{
								this.setDescription("场景详细描述");
								this.addMethod(new ScenarioMethod("do init method1", StepType.Given, false));
								this.addMethod(new ScenarioMethod("do init method2", StepType.Given, true));
								this.addMethod(new ScenarioMethod("do something", StepType.When, false));
								this.addMethod(new ScenarioMethod("check result", StepType.Then, false));
							}
						});
						this.addScenario(new StoryScenario("用例场景二", true) {
							{
								this.setDescription("场景详细描述");
								this.addMethod(new ScenarioMethod("do template method", StepType.Given, false));
							}
						});
					}
				});
			}
		}, EqMode.IGNORE_DEFAULTS);
	}

	@Test
	public void testBuildStoryScenarios() {
		ScenarioList list = new StoryFeatureXMLBuilder(document).buildStoryScenarios(null);
		List<StoryScenario> scenarios = list.getScenarios();
		want.list(scenarios).reflectionEq(new ArrayList<StoryScenario>() {
			{
				this.add(new StoryScenario("用例场景一", false) {
					{
						this.setDescription("场景详细描述");
						this.addMethod(new ScenarioMethod("do init method1", StepType.Given, false));
						this.addMethod(new ScenarioMethod("do init method2", StepType.Given, true));
						this.addMethod(new ScenarioMethod("do something", StepType.When, false));
						this.addMethod(new ScenarioMethod("check result", StepType.Then, false));
					}
				});
				this.add(new StoryScenario("用例场景二", true) {
					{
						this.setDescription("场景详细描述");
						this.addMethod(new ScenarioMethod("do template method", StepType.Given, false));
					}
				});
			}
		}, EqMode.IGNORE_DEFAULTS);
	}
}
