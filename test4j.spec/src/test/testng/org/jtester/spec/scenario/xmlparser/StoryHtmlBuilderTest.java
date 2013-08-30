package org.jtester.spec.scenario.xmlparser;

import java.util.List;

import mockit.Mock;

import org.dom4j.Document;
import org.dom4j.Element;
import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.spec.util.XmlHelper;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ResourceHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings({ "unchecked" })
public class StoryHtmlBuilderTest extends JTester {

	private Document document;

	@BeforeMethod
	public void initDocument() {
		this.document = XmlHelper.buildFromClasspath("org/jtester/spec/scenario/test-story.xml", "utf-8");
	}

	@Test
	public void testBuildStoryDescription() throws Exception {
		StringBuilder html = new StringBuilder();
		new StoryHtmlBuilder(document).buildStoryDescription(html);
		String expected = ResourceHelper.readFromFile(StoryHtmlBuilderTest.class, "description.html");
		want.string(html.toString()).isEqualTo(expected, StringMode.SameAsSpace);
	}

	@Test
	public void testBuildTemplateList() throws Exception {
		StringBuilder html = new StringBuilder();
		new StoryHtmlBuilder(document).buildTemplateList(html);
		String expected = ResourceHelper.readFromFile(StoryHtmlBuilderTest.class, "templateList.html");
		want.string(html.toString()).isEqualTo(expected, StringMode.SameAsSpace);
	}

	@Test
	public void testBuildScenarioMethods() throws Exception {
		Element scenario = (Element) document.selectSingleNode(StoryQName.xpathScenario + "[2]");
		String html = new StoryHtmlBuilder(document).buildScenarioMethods(2,
				scenario.selectNodes(StoryQName.nodeScenarioMethod));
		String expected = ResourceHelper.readFromFile(StoryHtmlBuilderTest.class, "scenarioMethod.html");
		want.string(html.trim()).isEqualTo(expected, StringMode.SameAsSpace);
	}

	@Test
	public void testBuildScenario() throws Exception {
		String html = new StoryHtmlBuilder(document).buildScenarioList(this.document
				.selectNodes(StoryQName.xpathScenario + "[2]"));
		String expected = ResourceHelper.readFromFile(StoryHtmlBuilderTest.class, "scenario.html");
		want.string(html.trim()).isEqualTo(expected, StringMode.SameAsSpace);
	}

	@Test
	public void testBuildScenarioList() throws Exception {
		StringBuilder html = new StringBuilder();
		final String scenario = ResourceHelper.readFromFile(StoryHtmlBuilderTest.class, "scenario.html");
		new MockUp<StoryHtmlBuilder>() {
			@Mock
			public String buildScenarioList(List<Element> scenarios) {
				return scenario;
			}
		};
		new StoryHtmlBuilder(document).buildScenarioList(html);
		String expected = ResourceHelper.readFromFile(StoryHtmlBuilderTest.class, "scenarioList.html");
		want.string(html.toString().trim()).isEqualTo(expected, StringMode.SameAsSpace);
	}
}
