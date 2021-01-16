package org.test4j.junit4;

import org.test4j.mock.startup.JavaAgentHits;
import org.test4j.module.database.IDatabase;

/**
 * use implements {@link org.test4j.Test4J}
 *
 * @author wudarui
 */
@Deprecated
public class Test4J implements org.test4j.Test4J, IDatabase {
    static {
        JavaAgentHits.message();
    }
}