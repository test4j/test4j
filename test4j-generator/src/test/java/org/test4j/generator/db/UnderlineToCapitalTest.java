package org.test4j.generator.db;

import org.junit.Test;
import org.test4j.generator.convert.UnderlineToCapital;

class UnderlineToCapitalTest {

    @Test
    public void convertPath() {
        UnderlineToCapital.convertPath("/Users/darui.wu/workspace/fluent-mybatis-v1.3.0/fluent-mybatis-test/src/test/java/cn/org/atool/fluent/mybatis/method");
    }

    @Test
    public void convertLine() {
        String line = UnderlineToCapital.convertLine(".user_name.values(\"test_3_aaa\")");
    }
}