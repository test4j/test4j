package org.test4j.testng.database;

import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@SpringContext( { "classpath:/org/jtester/fortest/hibernate/project-placeholder.xml",
		"classpath:/org/jtester/fortest/hibernate/project-datasource.xml" })
@Test(groups = "jtester")
public class InitDbTest extends JTester {

	@Test
	public void testInitDb() {

	}
}
