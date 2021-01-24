package org.test4j.testng;

import org.test4j.mock.startup.JavaAgentHits;
import org.test4j.module.database.IDatabase;
import org.testng.annotations.Test;

/**
 * TestNG base test
 *
 * @author wudarui
 */
public class Test4J implements org.test4j.Test4J, IDatabase {
    static {
        JavaAgentHits.message();
    }
}
