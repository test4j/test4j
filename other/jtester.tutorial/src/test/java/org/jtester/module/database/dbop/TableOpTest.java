package org.jtester.module.database.dbop;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.Group;
import org.jtester.module.database.IDatabase;
import org.junit.Test;

public class TableOpTest extends JTester implements IDatabase {

	@Test
	@Group("oracle")
	public void testCount_Oralce() {
		db.useDB("eve").table("MTN_ACTIVITY").count().notNull();
	}
}
