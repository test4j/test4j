package org.jtester.module.database.dbop;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.Group;
import org.jtester.module.database.IDatabase;
import org.jtester.tools.commons.DateHelper;
import org.junit.Test;

@SuppressWarnings({ "serial" })
public class InsertOpTest extends JTester implements IDatabase {
	@Test
	@Group("oracle")
	public void testInsert_OracleDate() {
		db.useDB("eve").table("MTN_PLAN").clean().insert(new DataMap() {
			{
				put("ID", 1);
				put("GMT_CREATE", DateHelper.parse("2010-11-10"));
			}
		});
		db.table("MTN_PLAN").query().propertyEqMap(new DataMap() {
			{
				put("ID", 1);
				put("GMT_CREATE", "2010-11-10");
			}
		});
	}

	@Test
	@Group("oracle")
	public void testInsert_OracleDate_StampTime() {
		db.useDB("eve").table("MTN_PLAN").clean().insert(new DataMap() {
			{
				put("ID", 1);
				put("GMT_CREATE", "2010-11-10 12:30:45");
			}
		});
		db.table("MTN_PLAN").query().propertyEqMap(new DataMap() {
			{
				put("ID", 1);
				put("GMT_CREATE", "2010-11-10 12:30:45");
			}
		});
	}
}
