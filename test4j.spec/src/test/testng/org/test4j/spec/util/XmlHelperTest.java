package org.test4j.spec.util;

import org.test4j.spec.util.XmlHelper;
import org.test4j.testng.Test4J;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XmlHelperTest extends Test4J {

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
