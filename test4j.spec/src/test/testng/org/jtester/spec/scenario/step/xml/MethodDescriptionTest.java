package org.jtester.spec.scenario.step.xml;

import static org.jtester.spec.scenario.step.xml.MethodDescription.VAR_START;
import java.util.HashMap;
import java.util.Iterator;

import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class MethodDescriptionTest extends JTester {

	@Test(dataProvider = "dataForGetVariable")
	public void testGetVariable(String template, String expected) {
		MethodDescription description = new MethodDescription(template, new HashMap());
		String var = description.getVariable(0);
		want.object(var).isEqualTo(expected);
	}

	@DataProvider
	Iterator dataForGetVariable() {
		return new DataIterator() {
			{
				data(VAR_START + "}dd", "");
				data(VAR_START + "myname}dd", "myname");
				data(VAR_START + "myname", null);
				data(VAR_START + "myname{}", null);
			}
		};
	}

	@Test
			public void testGetMethodDisplayText() {
				String template = String.format("2个变量,%slast}.%sfirst}.%sbad.", VAR_START, VAR_START, VAR_START);
				MethodDescription description = new MethodDescription(template, new HashMap() {
					{
						this.put("first", "wu");
						this.put("last", "darui");
						this.put("bad", "worse");
					}
				});
				String desc = description.getMethodDisplayText();
				want.string(desc).isEqualTo("2个变量,\nlast=darui.\nfirst=wu." + VAR_START + "bad.");
			}
}
