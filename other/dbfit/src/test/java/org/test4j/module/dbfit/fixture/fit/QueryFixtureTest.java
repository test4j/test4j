package org.test4j.module.dbfit.fixture.fit;

import org.test4j.module.database.IDatabase;
import org.test4j.module.dbfit.annotations.DbFit;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@Test(groups = { "test4j", "dbfit" })
public class QueryFixtureTest extends Test4J implements IDatabase {

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
