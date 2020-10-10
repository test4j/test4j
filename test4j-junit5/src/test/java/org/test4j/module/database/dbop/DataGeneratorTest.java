package org.test4j.module.database.dbop;

import org.junit.jupiter.api.Test;
import org.test4j.db.ITable;
import org.test4j.db.dm.UserDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.junit5.Test4J;

import java.util.Date;


@SuppressWarnings({"serial"})
public class DataGeneratorTest extends Test4J {

    @Test
    public void testInsert_CountDataMap() {
        db.table(ITable.t_user).clean().insert(new UserDataMap(true, 5) {
            {
                this.kv("id", DataGenerator.increase(100, 1));
                this.kv("first_name", "wu");
                this.kv("last_name", DataGenerator.random(String.class));
                this.kv("post_code", DataGenerator.repeat("310012", "310000"));
                this.kv("gmt_created", new Date(), "2011-09-06");
            }
        });

        db.table(ITable.t_user).query()
            .eqByProperties(
                new String[]{"id", "first_name",
                    "post_code", "gmt_created"},
                new Object[][]{{100, "wu", "310012", null},// <br>
                    {101, "wu", "310000", "2011-09-06"},// <br>
                    {102, "wu", "310012", "2011-09-06"},// <br>
                    {103, "wu", "310000", "2011-09-06"},// <br>
                    {104, "wu", "310012", "2011-09-06"}}, EqMode.IGNORE_DEFAULTS);
    }

    @Test
    public void testInsertByDataGenerator() {
        db.table(ITable.t_user).clean().insert(new UserDataMap(true, 2) {
            {
                this.kv("id", 100, 101);
                this.kv("first_name", new DataGenerator() {
                    @Override
                    public Object generate(int index) {
                        return "myname_" + (index + 1);
                    }
                });
            }
        });
        db.table(ITable.t_user).query().sizeEq(2)
            .eqByProperties("first_name", new String[]{"myname_1", "myname_2"});
    }

    @Test
    public void testInsertByDataGenerator_UserFields() {
        db.table(ITable.t_user).clean().insert(new UserDataMap(true, 2) {
            {
                this.kv("id", 100, 101);
                this.kv("first_name", new DataGenerator() {
                    @Override
                    public Object generate(int index) {
                        return "myname_" + value("id", index);
                    }
                });
            }
        });
        db.table(ITable.t_user).query().sizeEq(2)
            .eqByProperties("first_name", new String[]{"myname_100", "myname_101"});
    }
}
