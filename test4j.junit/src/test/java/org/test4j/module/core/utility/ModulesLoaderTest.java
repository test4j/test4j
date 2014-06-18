package org.test4j.module.core.utility;

import java.util.Arrays;
import java.util.List;

import mockit.Mock;

import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.tools.commons.ConfigHelper;

public class ModulesLoaderTest extends Test4J {
    /**
     * 测试database.type未设置时,database和dbfit模块失效
     */
    @Test
    public void testFilterModules() {
        new MockUp<ConfigHelper>() {
            @Mock
            public String databaseType() {
                return null;
            }
        };
        List<String> list = reflector.invokeStatic(ModulesLoader.class, "filterModules",
                Arrays.asList("database", "jmockit", "inject", "tracer"));
        want.collection(list).not(the.collection().hasItems("database")).sizeEq(2);
    }
}
