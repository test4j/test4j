package org.jtester.json.helper;

import org.jtester.json.encoder.beans.test.User;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "jtester", "json" })
public class ClazzMapTest extends JTester {

	@Test(dataProvider = "type_data")
	public void testGetClazzName(Object obj, String expected) {
		String clazzname = ClazzMap.getClazzName(obj);
		want.string(clazzname).start(expected);
	}

	@DataProvider
	public Object[][] type_data() {
		return new Object[][] { { Integer.valueOf(1), "Integer" }, // <br>
				{ new int[1], "int[]@" }, // <br>
				{ new Integer[0], "Integer[]@" }, // <br>
				{ new String(), "string" }, // <br>
				{ new String[0], "string[]@" }, // <br>
				{ new User(), "org.jtester.json.encoder.beans.test.User@" }, // <br>
				{ new User[0], "[Lorg.jtester.json.encoder.beans.test.User;@" } // <br>
		};
	}

	public void testGetClazzName_Primitive() {
		String clazzname = ClazzMap.getClazzName(1);
		want.string(clazzname).isEqualTo("Integer");
	}
}
