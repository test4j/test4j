package org.jtester.spec.util;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XmlHelperTest extends JTester {

	@Test(dataProvider = "dataForParseNodesFromMethodText")
	public void testParseNodesFromMethodText(String xml) throws Exception {
		XmlHelper.parseNodesFromMethodText(xml);
	}

	@DataProvider
	public DataIterator dataForParseNodesFromMethodText() {
		return new DataIterator() {
			{
				data("xxxx<para name=\"my-name\">xxxx</para>dafsdas");
			}
		};
	}
}
