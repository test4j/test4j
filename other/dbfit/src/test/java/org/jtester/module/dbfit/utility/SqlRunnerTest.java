package org.jtester.module.dbfit.utility;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jtester.module.database.annotations.Transactional;
import org.jtester.module.database.annotations.Transactional.TransactionMode;
import org.jtester.module.dbfit.IDbFit;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.module.dbfit.annotations.DbFit.AUTO;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
@DbFit(auto = AUTO.AUTO)
@SuppressWarnings({ "rawtypes" })
public class SqlRunnerTest extends JTester implements IDbFit {

	@Test
	@Transactional(TransactionMode.COMMIT)
	public void testExecute() throws SQLException {
		fit.useDefaultDataSource();
		fit.execute("delete from tdd_user where 1=1");
		fit.execute("insert into tdd_user(id,first_name) values(1,'darui.wu')");
	}

	@Test
	public void testQuery_ForMap() throws SQLException {
		Map map = fit.query("select * from tdd_user", Map.class);
		want.map(map).notNull().hasEntry("id", 1, "first_name", "dddd");
		Timestamp time = (Timestamp) map.get("my_date");
		want.string(time.toString()).start("2011-03-18");
	}

	@DbFit(when = "data/SqlRunnerTest/testQuery_ForMap.when.wiki")
	@Test
	public void testQueryList_MapList() throws SQLException {
		List<Map> maps = (List<Map>) fit.queryList("select * from tdd_user", Map.class);

		want.collection(maps).sizeEq(2).propertyEq("id", new Object[] { 1, 2 });
	}

	@DbFit(when = "data/SqlRunnerTest/testQuery_ForMap.when.wiki")
	@Test
	public void testQueryList_PoJoList() throws SQLException {
		List<UserPoJo> users = (List<UserPoJo>) fit.queryList("select * from tdd_user", UserPoJo.class);

		want.collection(users).sizeEq(2).propertyEq("id", new Object[] { 1, 2 });
	}

	@DbFit(when = "data/SqlRunnerTest/testQuery_ForMap.when.wiki")
	@Test
	public void testQuery_ForPoJo() throws SQLException {
		UserPoJo user = fit.query("select * from tdd_user", UserPoJo.class);
		want.object(user).notNull().propertyEq(new String[] { "id", "firstName" }, new Object[] { 1, "dddd" });
		want.string(user.myDate.toString()).start("2011-03-18");
	}

	@Test
	public void testExecuteFromFile() throws Exception {
		fit.executeSQLFile("org/jtester/module/dbfit/utility/data/SqlRunnerTest/testExecuteFromFile.sql");
	}

	public static class UserPoJo {
		int id;
		String firstName;
		Date myDate;
	}

}
