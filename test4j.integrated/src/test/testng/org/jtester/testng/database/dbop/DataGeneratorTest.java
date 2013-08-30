package org.jtester.testng.database.dbop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jtester.database.table.ITable;
import org.jtester.database.table.TddUserTable;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.module.database.IDatabase;
import org.jtester.testng.JTester;
import org.jtester.tools.datagen.DataSet;
import org.jtester.tools.datagen.EmptyDataSet;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
@Test(groups = { "jtester", "database" })
public class DataGeneratorTest extends JTester implements IDatabase {

    @Test(dataProvider = "dataGenerator")
    public void testParseMapList(final Object input, Object expected) {
        DataSet ds = new EmptyDataSet();
        List<DataMap> maps = reflector.invoke(ds, "parseMapList", 5, new DataMap() {
            {
                this.put("key1", "value1");
                this.put("key2", input);
            }
        });
        want.collection(maps).sizeEq(5)
                .propertyEq("key1", new String[] { "value1", "value1", "value1", "value1", "value1" });
        want.collection(maps).propertyEq("key2", expected, EqMode.IGNORE_DEFAULTS);
    }

    @DataProvider
    public DataIterator dataGenerator() {
        return new DataIterator() {
            {
                data(1, new Integer[] { 1, 1, 1, 1, 1 });
                data(new Integer[] { 1, 2, 3 }, new Integer[] { 1, 2, 3, 3, 3 });
                data(new Integer[] { 1, 2, 3, 4, 5, 6 }, new Integer[] { 1, 2, 3, 4, 5 });
                data(DataGenerator.repeat(1, 2), new Integer[] { 1, 2, 1, 2, 1 });
                data(DataGenerator.random(Integer.class), new Integer[] { null, null, null, null, null });
                data(DataGenerator.increase(1, 1), new Integer[] { 1, 2, 3, 4, 5 });
            }
        };
    }

    @Test
    public void testInsert2() {
        db.table(ITable.t_tdd_user).clean().insert(5, new TddUserTable() {
            {
                this.put(IColumn.f_id, DataGenerator.increase(100, 1));
                this.put(IColumn.f_first_name, "wu");
                this.put(IColumn.f_last_name, DataGenerator.random(String.class));
                this.put(IColumn.f_post_code, DataGenerator.repeat("310012", "310000"));
                this.put(IColumn.f_gmt_created, new Object[] { new Date(), "2011-09-06" });
            }
        }).commit();
    }

    @Test
    public void testInsert() {
        db.table(ITable.t_tdd_user).clean().insert(new DataSet() {
            {
                data(5, new TddUserTable() {
                    {
                        this.put(IColumn.f_id, DataGenerator.increase(100, 1));
                        this.put(IColumn.f_first_name, "wu");
                        this.put(IColumn.f_last_name, DataGenerator.random(String.class));
                        this.put(IColumn.f_post_code, DataGenerator.repeat("310012", "310000"));
                        this.put(IColumn.f_gmt_created, new Object[] { new Date(), "2011-09-06" });
                    }
                });
            }
        }).commit();
        db.table(ITable.t_tdd_user).query()
                .propertyEq(TddUserTable.IColumn.f_first_name, new String[] { "wu", "wu", "wu", "wu", "wu" })
                .reflectionEqMap(new ArrayList() {
                    {
                        add(new TddUserTable() {
                            {
                                this.put(IColumn.f_id, 100);
                                this.put(IColumn.f_post_code, "310012");
                                this.put(IColumn.f_gmt_created, null);
                            }
                        });
                        add(new TddUserTable() {
                            {
                                this.put(IColumn.f_id, 101);
                                this.put(IColumn.f_post_code, "310000");
                                this.put(IColumn.f_gmt_created, "2011-09-06");
                            }
                        });
                        add(new TddUserTable() {
                            {
                                this.put(IColumn.f_id, 102);
                                this.put(IColumn.f_post_code, "310012");
                                this.put(IColumn.f_gmt_created, "2011-09-06");
                            }
                        });
                        add(new TddUserTable() {
                            {
                                this.put(IColumn.f_id, 103);
                                this.put(IColumn.f_post_code, "310000");
                                this.put(IColumn.f_gmt_created, "2011-09-06");
                            }
                        });
                        add(new TddUserTable() {
                            {
                                this.put(IColumn.f_id, 104);
                                this.put(IColumn.f_post_code, "310012");
                                this.put(IColumn.f_gmt_created, "2011-09-06");
                            }
                        });
                    }
                }, EqMode.IGNORE_DEFAULTS);
    }

    @Test
    public void testInsert_CountDataMap() {
        db.table(ITable.t_tdd_user).clean().insert(5, new TddUserTable() {
            {
                this.put(IColumn.f_id, DataGenerator.increase(100, 1));
                this.put(IColumn.f_first_name, "wu");
                this.put(IColumn.f_last_name, DataGenerator.random(String.class));
                this.put(IColumn.f_post_code, DataGenerator.repeat("310012", "310000"));
                this.put(IColumn.f_gmt_created, new Object[] { new Date(), "2011-09-06" });
            }
        }).commit();

        db.table(ITable.t_tdd_user)
                .query()
                .propertyEq(
                        new String[] { TddUserTable.IColumn.f_id, TddUserTable.IColumn.f_first_name,
                                TddUserTable.IColumn.f_post_code, TddUserTable.IColumn.f_gmt_created },
                        new Object[][] { { 100, "wu", "310012", null },// <br>
                                { 101, "wu", "310000", "2011-09-06" },// <br>
                                { 102, "wu", "310012", "2011-09-06" },// <br>
                                { 103, "wu", "310000", "2011-09-06" },// <br>
                                { 104, "wu", "310012", "2011-09-06" } }, EqMode.IGNORE_DEFAULTS);
    }

    public void testInsertByDataGenerator() {
        db.table(ITable.t_tdd_user).clean().insert(2, new TddUserTable() {
            {
                this.put(IColumn.f_id, new int[] { 100, 101 });
                this.put(IColumn.f_first_name, new DataGenerator() {
                    @Override
                    public Object generate(int index) {
                        return "myname_" + (index + 1);
                    }
                });
            }
        });
        db.table(ITable.t_tdd_user).query().sizeEq(2)
                .propertyEq(TddUserTable.IColumn.f_first_name, new String[] { "myname_1", "myname_2" });
    }

    public void testInsertByDataGenerator_UserFields() {
        db.table(ITable.t_tdd_user).clean().insert(2, new TddUserTable() {
            {
                this.put(IColumn.f_id, new int[] { 100, 101 });
                this.put(IColumn.f_first_name, new DataGenerator() {
                    @Override
                    public Object generate(int index) {
                        return "myname_" + value("id");
                    }
                });
            }
        });
        db.table(ITable.t_tdd_user).query().sizeEq(2)
                .propertyEq(TddUserTable.IColumn.f_first_name, new String[] { "myname_100", "myname_101" });
    }

    @Test
    public void testData_JSON() {

    }
}
