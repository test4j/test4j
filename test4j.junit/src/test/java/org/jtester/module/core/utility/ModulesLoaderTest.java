package org.jtester.module.core.utility;

import java.util.Arrays;
import java.util.List;

import mockit.Mock;

import org.jtester.junit.JTester;
import org.jtester.tools.commons.ConfigHelper;
import org.junit.Test;

public class ModulesLoaderTest implements JTester {
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
                Arrays.asList("jmock", "jmockit", "inject", "tracer"));
        want.collection(list).not(the.collection().hasItems("database")).sizeEq(4);
    }
}
