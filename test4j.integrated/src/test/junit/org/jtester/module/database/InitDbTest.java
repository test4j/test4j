package org.jtester.module.database;

import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext({ "classpath:/org/jtester/fortest/hibernate/project-placeholder.xml",
        "classpath:/org/jtester/fortest/hibernate/project-datasource.xml" })
public class InitDbTest implements JTester {

    @Test
    public void testInitDb() {

    }
}
