package org.jtester.spec.scenario.xmlparser;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.spec.inner.StepType;
import org.jtester.spec.scenario.xmlparser.entity.ScenarioMethod;
import org.jtester.spec.scenario.xmlparser.entity.StoryScenario;
import org.jtester.spec.scenario.xmlparser.entity.TemplateMethod;
import org.jtester.spec.util.XmlHelper;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ResourceHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
public class StoryXmlBuilderTest extends JTester {
    Document document;

    @BeforeMethod
    public void initDocument() {
        this.document = XmlHelper.buildFromClasspath("org/jtester/spec/scenario/test-story.xml", "utf-8");
    }

    @Test(groups = "darui.wudr")
    public void testAddScenario() throws Exception {
        Element scenarios = (Element) document.selectSingleNode(StoryQName.xpathRoot);
        StoryXmlBuilder.addScenario(scenarios, new StoryScenario("我的测试场景", true).setDescription("场景描述"));
        String xml = XmlHelper.toXml(document);
        want.string(xml).contains(
                "<scenario name='我的测试场景' skip='true'><description><![CDATA[场景描述]]></description></scenario>",
                StringMode.SameAsSpace, StringMode.SameAsQuato);
    }

    @Test(groups = "darui.wudr")
    public void testModifyScenario() {
        Element scenarioNode = (Element) document.selectSingleNode(StoryQName.xpathScenario + "[2]");
        StoryXmlBuilder.modifyScenario(scenarioNode,
                new StoryScenario("修改后的测试场景", false).setDescription("new description"));
        String xml = XmlHelper.toXml(document);
        want.string(xml).contains("<scenario name='修改后的测试场景' skip='false'>" + // <br>
                "<description><![CDATA[new description]]></description>" + // <br>
                "<method type='given' name='do template method'>method text</method>" + // <br>
                "</scenario>", StringMode.IgnoreSpace, StringMode.SameAsQuato);
    }

    @Test(groups = "darui.wudr")
    public void testAddScenarioMethod() throws Exception {
        Element scenario = (Element) document.selectSingleNode(StoryQName.xpathScenario + "[1]");
        StoryXmlBuilder.addScenarioMethod(scenario, new ScenarioMethod("我的准备工作", StepType.Given, true)
                .setInitialText("具体描述<para name=\"参数\">ddd</para>ddd"));
        String xml = XmlHelper.toXml(document);
        want.string(xml)
                .contains(
                        "<method name='我的准备工作' type='given' skip='true'><![CDATA[具体描述]]><para name='参数'><![CDATA[ddd]]></para><![CDATA[ddd]]></method>",
                        StringMode.SameAsSpace, StringMode.SameAsQuato);
    }

    @Test(groups = "darui.wudr")
    public void testModifyScenarioMethod() {
        Element methodNode = (Element) document.selectSingleNode(StoryQName.xpathScenario + "[2]/method[1]");
        StoryXmlBuilder
                .modifyScenarioMethod(methodNode, new ScenarioMethod("我的准备工作", StepType.Then, true)
                        .setInitialText("具体描述<para name=\"参数\">ddd</para>ddd"));
        String xml = XmlHelper.toXml(document);
        want.string(xml)
                .contains(
                        "<method type='then' name='我的准备工作' skip='true'><![CDATA[具体描述]]><para name='参数'><![CDATA[ddd]]></para><![CDATA[ddd]]></method>",
                        StringMode.SameAsSpace, StringMode.SameAsQuato);
    }

    @Test(groups = "darui.wudr")
    public void testModifyScenarioMethod_IllegalTag() {
        Element methodNode = (Element) document.selectSingleNode(StoryQName.xpathScenario + "[2]/method[1]");
        try {
            StoryXmlBuilder.modifyScenarioMethod(methodNode,
                    new ScenarioMethod("我的准备工作", StepType.Then, true).setInitialText("具体描述<para name=\"参数\">dddddd"));
            want.fail();
        } catch (Exception e) {
            // Throwable e1 = e.getCause();
            want.string(e.getMessage()).contains(
                    "The element type 'para' must be terminated by the matching end-tag '</para>'");
        }
        String xml = XmlHelper.toXml(document);
        // 验证场景2方法1的内容没有发生变化
        want.string(xml).contains("<method type='given' name='do template method'>method text</method>",
                StringMode.SameAsSpace, StringMode.SameAsQuato);
    }

    @Test(groups = "darui.wudr")
    public void testModifyScenarioMethod_IllegalName() {
        Element methodNode = (Element) document.selectSingleNode(StoryQName.xpathScenario + "[2]/method[1]");
        try {
            StoryXmlBuilder.modifyScenarioMethod(methodNode, new ScenarioMethod("我的准备工作", StepType.Then, true)
                    .setInitialText("具体描述<para name=\"\">ddd</para>ddd"));
            want.fail("应该发生参数错误");
        } catch (Exception e) {
            String error = e.getMessage();
            want.string(error).contains("the name of para can't be null!");
        }
        String xml = XmlHelper.toXml(document);
        // 验证场景2方法1的内容没有发生变化
        want.string(xml).contains("<method type='given' name='do template method'>method text</method>",
                StringMode.SameAsSpace, StringMode.SameAsQuato);
    }

    @Test(groups = "darui.wudr")
    public void testAddTemplateMethod() throws Exception {
        Element scenario = (Element) document.selectSingleNode(StoryQName.xpathTemplate);
        StoryXmlBuilder.addTemplateMethod(scenario,
                new TemplateMethod("我的准备工作", StepType.Given).setInitialText("具体描述<para name=\"参数\">ddd</para>ddd"));
        String xml = XmlHelper.toXml(document);
        want.string(xml)
                .contains(
                        "<method name='我的准备工作' type='given'><![CDATA[具体描述]]><para name='参数'><![CDATA[ddd]]></para><![CDATA[ddd]]></method>",
                        StringMode.SameAsSpace, StringMode.SameAsQuato);
    }

    @Test(groups = "darui.wudr")
    public void testModifyStoryDescription() {
        Element description = (Element) document.selectSingleNode(StoryQName.xpathDescription);
        StoryXmlBuilder.modifyStoryDescription(description, "新的用例描述信息");
        String xml = XmlHelper.toXml(document);
        want.string(xml).contains("<story><description><![CDATA[新的用例描述信息]]></description>", StringMode.IgnoreSpace);
    }

    @Test(groups = "darui.wudr")
    public void testModifyTemplateMethod() {
        Element template = (Element) document.selectSingleNode(StoryQName.xpathTemplateMethod + "[1]");
        StoryXmlBuilder.modifyTemplateMethod(template,
                new TemplateMethod("新的准备方法", StepType.Then).setInitialText("新的描述<para name=\"新的参数\">ddd</para>ddd"));
        String xml = XmlHelper.toXml(document);
        want.string(xml).contains(
                "<template>"
                        + // <br>
                        "<method type='then' name='新的准备方法'><![CDATA[新的描述]]><para name='新的参数'><![CDATA[ddd]]></para><![CDATA[ddd]]></method>"
                        + // <br>
                        "<method type='then' name='do template method2'>模板内容</method>" + // <br>
                        "</template>", StringMode.IgnoreSpace, StringMode.SameAsQuato);
    }

    @Test(groups = "darui.wudr")
    public void testCreateStory() {
        Document document = StoryXmlBuilder.createStory("我的测试用例");
        String xml = XmlHelper.toXml(document);
        want.string(xml).contains(
                "<story><description>我的测试用例</description><templates></templates><scenarios></scenarios></story>",
                StringMode.IgnoreSpace, StringMode.SameAsQuato);
    }

    @Test(groups = "darui.wudr", dataProvider = "dataConvertPathID")
    public void testConvertPathID(String xpath, String expectedPathID) {
        String newPath = StoryXmlBuilder.convertPathID(xpath);
        want.string(newPath).eq(expectedPathID);
    }

    @DataProvider
    Iterator dataConvertPathID() {
        return new DataIterator() {
            {
                data("/story", "/story");
                data("/story/template/method", "/story/template/method[1]");
                data("/story/template/method[2]", "/story/template/method[2]");
                data("/story/scenario", "/story/scenario[1]");
                data("/story/scenario/method", "/story/scenario[1]/method[1]");
                data("/story/scenario[2]/method", "/story/scenario[2]/method[1]");
                data("/story/scenario[2]/method[1]", "/story/scenario[2]/method[1]");
            }
        };
    }

    @Test
    public void testCpyStoryScenario() throws Exception {
        StoryXmlBuilder.cpyStoryScenario(document, StoryQName.xpathRoot + "/scenario[1]",
                new StoryScenario("我拷贝的场景").setDescription("新的描述"));
        String xml = XmlHelper.toXml(document);
        String oldNode = ResourceHelper
                .readFromFile(StoryXmlBuilderTest.class, "testCpyStoryScenario.old_scenario.xml");
        want.string(xml).contains(oldNode, StringMode.IgnoreSpace);
        String newNode = ResourceHelper
                .readFromFile(StoryXmlBuilderTest.class, "testCpyStoryScenario.new_scenario.xml");
        want.string(xml).contains(newNode, StringMode.IgnoreSpace);
    }
}
