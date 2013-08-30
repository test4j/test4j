package org.jtester.module.dbfit.environment;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;

import org.jtester.module.database.IDatabase;
import org.jtester.module.database.environment.typesmap.AbstractTypeMap;
import org.jtester.module.database.environment.typesmap.MySQLTypeMap;
import org.jtester.module.dbfit.annotations.DbFit;
import org.jtester.testng.JTester;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "serial", "rawtypes" })
@Test(groups = { "jtester", "database" })
public class TypeMapTest extends JTester implements IDatabase {

	@Test(dataProvider = "dataObjectByType_Timestamp")
	public void testToObjectByType_Timestamp(Class dateType, String input, String output) {
		AbstractTypeMap typeMap = new MySQLTypeMap();
		java.util.Date result = (java.util.Date) typeMap.toObjectByType(input, dateType.getName());
		want.date(result).eqByFormat(output);
	}

	@DataProvider
	public Iterator dataObjectByType_Timestamp() {
		return new DataIterator() {
			{
				data(Timestamp.class, "2011-09-04 12:23:12", "2011-09-04 12:23:12");
				data(Timestamp.class, "2011-09-05", "2011-09-05");
				data(Timestamp.class, "2011-09-06 12:23:12.0", "2011-09-06 12:23:12");
				data(Timestamp.class, "2011-09-06 12:23:12.234", "2011-09-06 12:23:12");

				data(Time.class, "2011-09-06 12:23:12.234", "12:23:12");
				data(Time.class, "12:23:12", "12:23:12");
				data(Time.class, " 2011-09-06 2:23:12.000 ", "02:23:12");

				data(Date.class, "2011-09-06 12:23:12.234", "2011-09-06");
				data(Date.class, "2011-9-06 12:23:12.234", "2011-09-06");
				data(Date.class, " 11-09-06 2:23:12.000 ", "0011-09-06");
			}
		};
	}

	@Test(dataProvider = "dataObjectByType")
	public void testToObjectByType(Class javaType, String input, Object output) {
		AbstractTypeMap typeMap = new MySQLTypeMap();
		Object result = typeMap.toObjectByType(input, javaType.getName());
		want.object(result).reflectionEq(output);
	}

	@DataProvider
	public Iterator dataObjectByType() {
		return new DataIterator() {
			{
				data(String.class, "i am string", "i am string");
				data(Integer.class, "124", 124);
				data(Long.class, "124456", 124456L);
				data(Short.class, "124", Short.valueOf("124"));
				data(Double.class, "124.0", 124d);
				data(Float.class, "124.0", 124f);
				data(Boolean.class, "1", true);
				data(Boolean.class, "0", false);
				data(Boolean.class, "true", true);
				data(Boolean.class, "False", false);

				data(BigDecimal.class, "12345.34", BigDecimal.valueOf(12345.34d));
			}
		};
	}

	@Test
	@DbFit(then = "testGetJavaType_AllType.then.wiki")
	public void testGetJavaType_AllType_Insert() {
		db.table("all_types").clean().insert(new DataMap() {
			{
				this.put("TINYINT", "12");
				this.put("SMALLINT", "12");
				this.put("MEDIUMINT", "12");
				this.put("INTEGER", "12");
				this.put("BIGINT", "12");
				this.put("FLOAT", "12.1");
				this.put("DOUBLE", "12.2");
				this.put("DECIMAL", "12.3");
				this.put("DATE", "2011-9-3"); // <br>
				this.put("DATETIME", "2011-9-3 6:12:45"); // <br>
				this.put("TIME", "6:12:45"); // <br>
				this.put("YEAR", "2011"); // <br>
				this.put("CHAR", "char"); // <br>
				this.put("VARCHAR", "varchar"); // <br>
				this.put("TINYBLOB", "tinyblob"); // <br>
				this.put("BLOB", "blob"); // <br>
				this.put("MEDIUMBLOB", "mediumblob"); // <br>
				this.put("LONGBLOB", "longblob"); // <br>
				this.put("TINYTEXT", "tinytext"); // <br>
				this.put("TEXT", "text"); // <br>
				this.put("MEDIUMTEXT", "mediumtext"); // <br>
				this.put("LONGTEXT", "longtext"); // <br>
				this.put("ENUM", "2"); // <br>
				this.put("BINARY", "binary");
				this.put("VARBINARY", "varbinary");
				this.put("BIT", "1"); // <br>
				this.put("BOOLEAN", "true"); // <br>
			}
		});
		db.commit();
	}

	@Test
	@DbFit(when = "testGetJavaType_AllType.when.wiki")
	public void testGetJavaType_AllType_Query() {
		db.table("all_types").query().reflectionEqMap(new DataMap() {
			{
				this.put("TINYINT", 12);
				this.put("SMALLINT", 12);
				this.put("MEDIUMINT", 12);
				this.put("INTEGER", 12);
				this.put("BIGINT", 12);
				this.put("FLOAT", 12.1f);
				this.put("DOUBLE", 12.2d);
				this.put("DECIMAL", 12);
				this.put("DATE", "2011-09-03"); // <br>
				this.put("DATETIME", "2011-09-03 06:12:45"); // <br>
				this.put("CHAR", "char"); // <br>
				this.put("VARCHAR", "varchar"); // <br>
				this.put("TINYTEXT", "tinytext"); // <br>
				this.put("TEXT", "text"); // <br>
				this.put("MEDIUMTEXT", "mediumtext"); // <br>
				this.put("LONGTEXT", "longtext"); // <br>
				this.put("ENUM", "2"); // <br>
				this.put("BIT", true); // <br>
			}
		});
		db.commit();
	}
}
