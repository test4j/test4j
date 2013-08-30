package org.jtester.spec.scenario.step.txt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jtester.spec.scenario.step.txt.ParameterParser2;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
public class ParameterParser2Test extends JTester {

	@Test(dataProvider = "dataForParserSinglePara")
	public void testParseSinglePara(String words, String key, String value, int expectedIndex) {
		Map<String, String> paras = new HashMap<String, String>();
		int index = ParameterParser2.parseSinglePara(paras, words.toCharArray(), 0, "1");
		want.map(paras).hasEntry(key, value);
		want.number(index).isEqualTo(expectedIndex);
	}

	@DataProvider
	public Iterator dataForParserSinglePara() {
		return new DataIterator() {
			{
				data("customerId=10023", "customerId", "10023", 16);
				data("id=\"abc\"|说明】", "id", "abc", 12);
				data("id=\"a\\|bc\"|说明】", "id", "a|bc", 14);
				data("id=\"a\\}bc\"|说明】", "id", "a}bc", 14);
				data("id=\"a\\\\bc\"|说明】", "id", "a\\bc", 14);
				data("id=\"a\\\"bc\"|说明】", "id", "a\"bc", 14);
				data("abc", "1", "abc", 3);
				data("\"=abc\"", "1", "=abc", 6);
				data("\"\\}bc\"", "1", "}bc", 6);
			}
		};
	}

	@Test(dataProvider = "dataForParserParameter")
	public void testParserParameter(String words, Map<String, String> expected) {
		Map<String, String> paras = new LinkedHashMap<String, String>();
		ParameterParser2.parserParameter(words, paras);
		want.map(paras).reflectionEq(expected);
	}

	@SuppressWarnings("serial")
	@DataProvider
	public Iterator dataForParserParameter() {
		return new DataIterator() {
			{
				data("do【value1】in【value2】", new HashMap<String, String>() {
					{
						put("1", "value1");
						put("2", "value2");
					}
				});

				data("do【key1=value1|说明】in【value2", new HashMap<String, String>() {
					{
						put("key1", "value1");
						put("2", "value2");
					}
				});

				data("do【key1=va\\】lue1|说明】in【value2", new HashMap<String, String>() {
					{
						put("key1", "va】lue1");
						put("2", "value2");
					}
				});
			}
		};
	}
}
