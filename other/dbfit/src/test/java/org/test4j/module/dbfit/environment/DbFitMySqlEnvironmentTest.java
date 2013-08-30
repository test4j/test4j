package org.test4j.module.dbfit.environment;

import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.annotations.DbFit.AUTO;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = "mysql")
public class DbFitMySqlEnvironmentTest extends Test4J {

    /**
     * 在dbfit文件中测试mysql bigint数据类型
     */
    @DbFit(auto = AUTO.AUTO)
    public void testBigInt() {

    }
}
