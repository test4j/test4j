package org.test4j.module.database;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "classpath:/org/test4j/fortest/hibernate/project-placeholder.xml",
        "classpath:/org/test4j/fortest/hibernate/project-datasource.xml" })
public class InitDbTest implements Test4J {

    @Test
    public void testInitDb() {

    }
}
