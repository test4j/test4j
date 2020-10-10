package org.test4j.generator.db;

import org.junit.jupiter.api.Test;
import org.test4j.generator.convert.UnderlineToCapital;
import org.test4j.junit5.Test4J;

import static org.junit.jupiter.api.Assertions.*;

class UnderlineToCapitalTest extends Test4J {

    @Test
    void convertPath() {
        UnderlineToCapital.convertPath("/Users/darui.wu/workspace/fluent-mybatis-v1.3.0/fluent-mybatis-test/src/test/java/cn/org/atool/fluent/mybatis/method");
    }

    @Test
    void convertLine() {
        String line = UnderlineToCapital.convertLine(".user_name.values(\"test_3_aaa\")");
        want.string(line).eq(".userName.values(\"test_3_aaa\")");
    }
}