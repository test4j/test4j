package org.test4j.testng.database;

import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext({ "classpath:/org/test4j/fortest/hibernate/project-placeholder.xml",
        "classpath:/org/test4j/fortest/hibernate/project-datasource.xml" })
@Test(groups = "test4j")
public class InitDbTest extends Test4J {

    @Test
    public void testInitDb() {

    }
}
