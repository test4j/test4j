package org.jtester.testng.database;

import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext( { "classpath:/org/jtester/fortest/hibernate/project-placeholder.xml",
		"classpath:/org/jtester/fortest/hibernate/project-datasource.xml" })
@Test(groups = "jtester")
public class InitDbTest extends JTester {

	@Test
	public void testInitDb() {

	}
}
