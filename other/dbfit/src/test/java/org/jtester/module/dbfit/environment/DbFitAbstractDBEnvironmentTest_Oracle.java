package org.test4j.module.dbfit.environment;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.module.database.environment.DBEnvironment;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.module.database.environment.types.OracleEnvironment;
import org.test4j.module.dbfit.IDbFit;
import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.module.dbfit.environment.DbFitAbstractDBEnvironment;
import org.test4j.module.dbfit.environment.DbFitOracleEnvironment;
import org.test4j.testng.test4j;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "database" })
public class DbFitAbstractDBEnvironmentTest_Oracle extends test4j implements IDbFit {

    public void testExtractParamNames_oracle() {
        DbFitAbstractDBEnvironment env = new DbFitOracleEnvironment(new OracleEnvironment(null, null));
        fit.setSymbol("id", "1001");
        String[] vars = env.extractParamNames("update table set date='2011-07-01 14:23:35' where id=:id");
        want.array(vars).sizeEq(1).reflectionEq(new String[] { "id" }, EqMode.IGNORE_ORDER);
    }

    public void testExtractParamNames_mysql() {
        DbFitAbstractDBEnvironment env = new DbFitOracleEnvironment(new OracleEnvironment(null, null));
        fit.setSymbol("id", "1001");
        String[] vars = env.extractParamNames("update table set refence='@myreference' where id=:id");
        want.array(vars).sizeEq(1).reflectionEq(new String[] { "id" }, EqMode.IGNORE_ORDER);
    }

    @DbFit(when = "data/DbFitAbstractDBEnvironmentTest_Oracle/testCreateStatementWithBoundFixtureSymbols.when.wiki", then = "data/DbFitAbstractDBEnvironmentTest_Oracle/testCreateStatementWithBoundFixtureSymbols.then.wiki")
    public void testCreateStatementWithBoundFixtureSymbols() throws SQLException {
        fit.setSymbol("id", "1");
        DBEnvironment env = DbFitAbstractDBEnvironment.convert(DBEnvironmentFactory.getCurrentDBEnvironment());
        env.connect();
        PreparedStatement statement = env
                .createStatementWithBoundFixtureSymbols("update test4j_user set name='@modified_name' where id=@id");
        statement.execute();
        statement.close();
    }

    @Test(groups = "oracle")
    @DbFit(when = "data/DbFitAbstractDBEnvironmentTest_Oracle/oracle.when.wiki", then = "data/DbFitAbstractDBEnvironmentTest_Oracle/oracle.then.wiki")
    public void testCreateStatementWithBoundFixtureSymbols_Oracle() throws SQLException {
        fit.setSymbol("id", "123");
        fit.useSpecDataSource("", "eve.properties");
        fit.execute("update MTN_SEND_OVERVIEW set CREATOR=':mycreator' where id=:id", "commit");
        fit.commit();
    }
}
