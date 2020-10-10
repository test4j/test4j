package org.test4j.generator.db;

import org.junit.Test;
import org.test4j.generator.convert.UnderlineToCapital;

public class UnderlineToCapitalTest {

    @Test
    public void convertPath() {
        String path = "/Users/wudarui/workspace/github/fluent-mybatis/fluent-mybatis-test/src/test/java/cn/org/atool/fluent/mybatis";
        UnderlineToCapital.convertPath(path);
    }

    @Test
    public void convertLine() {
        String line = UnderlineToCapital.convertLine(".user_name.values(\"test_3_aaa\")");
    }
}