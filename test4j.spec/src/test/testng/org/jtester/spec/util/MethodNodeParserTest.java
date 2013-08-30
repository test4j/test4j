package org.jtester.spec.util;

import java.util.List;

import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.spec.util.XmlHelper.MethodNode;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ResourceHelper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MethodNodeParserTest extends JTester {

	@Test(dataProvider = "dataForIsParaBegin")
	public void testParseParameterName(String input, String paraName) throws Exception {
		MethodNodeParser parser = new MethodNodeParser(input);
		MethodNode para = parser.parseParameterName(0);
		want.object(para).propertyEq("paraName", paraName);
		char ch = parser.currIndexChar();
		want.character(ch).is('a');
	}

	@DataProvider
	public DataIterator dataForIsParaBegin() {
		return new DataIterator() {
			{
				data("name=\"name\">a", "name");
				data(" name   =   \"nam e \"   >a", "nam e ");
			}
		};
	}

	public void testIsBgnParaElement_NotBegin() throws Exception {
		MethodNodeParser parser = new MethodNodeParser("<part name");
		boolean isBegin = parser.isBgnParaElement(0);
		want.bool(isBegin).is(false);
		want.object(parser).propertyEq("currIndex", 0);
	}

	public void testIsBgnParaElement() throws Exception {
		MethodNodeParser parser = new MethodNodeParser("<para name");
		boolean isBegin = parser.isBgnParaElement(0);
		want.bool(isBegin).is(true);
		want.object(parser).propertyEq("currIndex", 6);
	}

	@Test(dataProvider = "dataForIsParaBegin_Illegal")
	public void testParseParameterName_Illegal(String input, String expected) throws Exception {
		MethodNodeParser parser = new MethodNodeParser(input);
		try {
			parser.parseParameterName(0);
			want.fail();
		} catch (RuntimeException e) {
			String error = e.getMessage();
			want.string(error).contains(expected, StringMode.IgnoreSpace);
		}
	}

	@DataProvider
	public DataIterator dataForIsParaBegin_Illegal() {
		return new DataIterator() {
			{
				data("nane", "only name property allowed by para element");
				data(" name+", "expected char('=')");
				data(" name = '", "expected char('\"')");
				data(" name=\"name\"a", "expected char('>')");
				data(" name=\"\">", "the name of para can't be null!");
			}
		};
	}

	@Test(dataProvider = "dataForIsEndParaElement")
	public void testIsEndParaElement(String input, boolean expected, int currIndex) throws Exception {
		MethodNodeParser parser = new MethodNodeParser(input);
		boolean isEnd = parser.isEndParaElement(0);
		want.bool(isEnd).is(expected);
		want.object(parser).propertyEq("currIndex", currIndex);
	}

	@DataProvider
	public DataIterator dataForIsEndParaElement() {
		return new DataIterator() {
			{
				data("</para>xxx", true, 7);
				data("< / para >xxx", true, 10);
				data("<\\para>xxx", false, 0);
				data("</part>xxx", false, 0);
			}
		};
	}

	@Test(dataProvider = "dataForParseParaValue")
	public void testParseParameterValue(String input, String expected) throws Exception {
		MethodNodeParser parser = new MethodNodeParser(input);
		MethodNode paraNode = new MethodNode(true);
		parser.parseParameterValue(paraNode, 0);
		want.object(paraNode).propertyEq("text", expected);
		char ch = parser.currIndexChar();
		want.character(ch).is('x');
	}

	@DataProvider
	public static DataIterator dataForParseParaValue() {
		return new DataIterator() {
			{
				data("abc</para>x", "abc");
				data("abc< / para >x", "abc");
				data("xb<part>c</para>x", "xb<part>c");
				data("xb</part>c</para>x", "xb</part>c");
				data("<abc</para>x", "<abc");
				data("<![CDATA[abc]]></para>x", "abc");
				data("<![CDA TA[abc] ]></para>x", "<![CDA TA[abc] ]>");
			}
		};
	}

	@Test(dataProvider = "dataForParseParaValue_Exception")
	public void testParseParameterValue_Exception(String input, String expected) throws Exception {
		MethodNodeParser parser = new MethodNodeParser(input);
		MethodNode paraNode = new MethodNode(true);
		try {
			parser.parseParameterValue(paraNode, 0);
			want.fail();
		} catch (RuntimeException e) {
			String error = e.getMessage();
			want.string(error).contains(expected, StringMode.IgnoreSpace, StringMode.SameAsQuato);
		}
	}

	@DataProvider
	public static DataIterator dataForParseParaValue_Exception() {
		return new DataIterator() {
			{
				data("xbc<para>x", "you haven't end with previous para element");
				data("<![CDATA[xbc]]> x</para>x", "expected end of para element at index");
				data("<![CDATA[xbc</para>x", "ou have '<![CDATA[' begin of para value, but not end with ']]>'");
				data("xbc]]></para>x", "you have ']]>' end of para value, but not begin with '<![CDATA['");
				data("<![CDATA[xbc]]>", "The element type 'para' must be terminated by the matching end-tag '</para>'");
			}
		};
	}

	@Test(dataProvider = "dataForParseMethodNodes")
	public void testParseMethodNodes(String fileNo, MethodNode[] expected) throws Exception {
		String text = ResourceHelper
				.readFromFile(MethodNodeParserTest.class, "testParseMethodNodes_" + fileNo + ".txt");
		MethodNodeParser parser = new MethodNodeParser(text);
		List<MethodNode> nodes = parser.parseMethodNodes();
		want.list(nodes).reflectionEq(expected);
	}

	@DataProvider
	public DataIterator dataForParseMethodNodes() {
		return new DataIterator() {
			{
				data("01", new MethodNode[] { new MethodNode("asdfsdaf\n"), new MethodNode("name1", "asdfsadf"),
						new MethodNode("\nasdfsadf") });
				data("02", new MethodNode[] { new MethodNode("asdfsdaf\n"), new MethodNode("name1", "asdfsadf"),
						new MethodNode("name2", "asdfsadf") });
			}
		};
	}
}
