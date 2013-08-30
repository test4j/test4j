package org.test4j.module.database.dbop;

import org.test4j.junit.Test4J;
import org.test4j.junit.annotations.Group;
import org.test4j.module.database.IDatabase;
import org.test4j.tools.commons.DateHelper;
import org.junit.Test;

@SuppressWarnings({ "serial" })
public class InsertOpTest extends Test4J implements IDatabase {
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
