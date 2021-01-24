package org.test4j.junit5;

import org.junit.jupiter.api.Tag;
import org.test4j.mock.startup.JavaAgentHits;
import org.test4j.module.database.IDatabase;

/**
 * use implements {@link org.test4j.Test4J}
 *
 * @author wudarui
 */
@Tag("test4j")
public class Test4J implements org.test4j.Test4J, IDatabase {
    static {
        JavaAgentHits.message();
    }
}