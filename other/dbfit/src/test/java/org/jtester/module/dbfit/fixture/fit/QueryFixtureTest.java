package org.jtester.module.dbfit.fixture.fit;

import org.jtester.module.database.IDatabase;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@Test(groups = { "jtester", "dbfit" })
public class QueryFixtureTest extends JTester implements IDatabase {

	@DbFit(then = "testQueryFixture.then.wiki")
	public void testQueryFixture() {
		db.table("demo_big_int_id").clean().insert(new DataMap() {
			{
				put("id", 123456);
				put("is_delete", "Y");
			}
		});
		db.query("select * from demo_big_int_id where id=123456").reflectionEqMap(new DataMap() {
			{
				put("id", 123456);
				put("is_delete", "Y");
			}
		});

		db.table("demo_big_int_id").query().reflectionEqMap(new DataMap() {
			{
				put("id", 123456);
				put("is_delete", "Y");
			}
		});
	}
}
