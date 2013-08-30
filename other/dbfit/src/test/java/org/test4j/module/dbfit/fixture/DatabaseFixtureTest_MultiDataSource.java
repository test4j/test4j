package org.test4j.module.dbfit.fixture;

import org.apache.log4j.Logger;
import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

/**
 * 在dbfit文件中的多数据源连接
 * 
 * @author darui.wudr
 */
@Test(groups = "test4j")
public class DatabaseFixtureTest_MultiDataSource extends Test4J {

    private final static Logger log4j = Logger.getLogger(DatabaseFixtureTest_MultiDataSource.class);

    @DbFit(when = "DatabaseFixtureTest_MultiDataSource.wiki")
    public void multiDataSource() {
        log4j.info("test");
    }

    @DbFit(when = "DatabaseFixtureTest_connectFromFile.wiki")
    public void connectFromFile() {
        log4j.info("test");
    }

    @DbFit(when = "DatabaseFixtureTest_connectFromFile_oracle.wiki")
    @Test(groups = "broken-install")
    public void connectFromFile_orcle() {
        log4j.info("test");
    }
}
