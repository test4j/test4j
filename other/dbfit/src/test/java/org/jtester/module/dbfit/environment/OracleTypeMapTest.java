package org.jtester.module.dbfit.environment;

import org.jtester.module.database.IDatabase;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings({ "serial" })
@Test(groups = { "jtester", "database", "oracle", "manual" })
public class OracleTypeMapTest extends JTester implements IDatabase {

	@Test(groups = { "broken-install" })
	@DbFit(then = "testGetJavaType.oracle.then.wiki")
	public void testGetJavaType() {
		db.useDB("eve").table("MTN_ALL_TYPES").clean().insert(new DataMap() {
			{
				this.put("ID", 1);
				this.put("T_BLOB", "t_blob");
				this.put("T_CLOB", "t_clob");
				this.put("T_CHAR", "t_char");
				this.put("T_DATE", "2011-9-12");
				// this.put("T_LONG", "asdfdsafdsafdsafsdafsdaf");
				this.put("T_NUMBER", "23243.876");
				this.put("T_RAW", "asdfdsf");
				this.put("T_TIMESTAMP", "2011-9-5 12:23:34");
				this.put("T_VARCHAR", "asdfadsf");
			}
		}).commit();
	}

	@Test(groups = "broken-install")
	@DbFit(when = "testGetJavaType.oracle.when.wiki")
	public void testGetJavaType_Query() {
		db.useDB("eve").table("MTN_ALL_TYPES").query().reflectionEqMap(new DataMap() {
			{
				this.put("ID", 2);
				this.put("T_BLOB", "t_blob");
				// this.put("T_CLOB", "745F636C6F62");
				// this.put("T_CHAR", "t_char");
				this.put("T_DATE", "2011-09-12");
				// this.put("T_LONG", "asdfdsafdsafdsafsdafsdaf");
				this.put("T_NUMBER", 23243.876);
				// this.put("T_RAW", "asdfdsf");
				// this.put("T_TIMESTAMP", "2011-9-5 12:23:34");
				this.put("T_VARCHAR", "asdfadsf");
			}
		});
	}

	public void testOracleCLOB() {
		db.useDB("mjr").table("mjr_msg_bus").clean().insert(1, new DataMap() {
			{
				this.put("ID", "1");
				this.put("CONTENT", "CLOB DATA");
				this.put("USER_ID", "test");
				this.put("IS_DELETED", "n");
			}
		}).commit();
		db.table("mjr_msg_bus").query().reflectionEqMap(new DataMap() {
			{
				this.put("CONTENT", "CLOB DATA");
			}
		});
	}
}
