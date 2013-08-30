package org.test4j.module.database.dbop;

import org.test4j.junit.Test4J;
import org.test4j.junit.annotations.Group;
import org.test4j.module.database.IDatabase;
import org.junit.Test;

public class TableOpTest extends Test4J implements IDatabase {

	@Test
	@Group("oracle")
	public void testCount_Oralce() {
		db.useDB("eve").table("MTN_ACTIVITY").count().notNull();
	}
}
