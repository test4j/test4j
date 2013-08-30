package org.jtester.testng.spring.utility;

import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringInitMethod;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
		"org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public class SpringInitHelperTest extends JTester {
	@Test
	public void testInvokeSpringInitMethod() {
		want.string(privateMethod).isEqualTo("privateMethod");
		want.string(protectedMethod).isEqualTo("protectedMethod");
	}

	private static String privateMethod = "unknown";

	@SpringInitMethod
	private void privateMethod() {
		privateMethod = "privateMethod";
	}

	private static String protectedMethod = "unknown";

	@SpringInitMethod
	protected void protectedMethod() {
		protectedMethod = "protectedMethod";
	}
}
