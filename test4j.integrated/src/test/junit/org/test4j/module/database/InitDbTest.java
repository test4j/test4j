package org.test4j.module.database;

import org.junit.Test;
import org.test4j.junit.JTester;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "classpath:/org/jtester/fortest/hibernate/project-placeholder.xml",
        "classpath:/org/jtester/fortest/hibernate/project-datasource.xml" })
public class InitDbTest implements JTester {

    @Test
    public void testInitDb() {

    }
}
