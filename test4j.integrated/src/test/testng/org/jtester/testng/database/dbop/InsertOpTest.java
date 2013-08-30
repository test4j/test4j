package org.jtester.testng.database.dbop;

import java.util.Iterator;

import org.jtester.database.table.TddUserTable;
import org.jtester.module.database.IDatabase;
import org.jtester.module.database.dbop.InsertOp;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.DateHelper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "serial" })
@Test(groups = { "jtester", "database" })
public class InsertOpTest extends JTester implements IDatabase {
    @Test(dataProvider = "testGetInsertCommandText_data")
    public void testGetInsertCommandText(DataMap data, String result) {
        InsertOp ds = reflector.newInstance(InsertOp.class);
        reflector.setField(ds, "quato", "");
        reflector.setField(ds, "table", "tdd_user");
        reflector.setField(ds, "data", data);

        String text = reflector.invoke(ds, "getInsertCommandText");
        want.string(text).eqWithStripSpace(result);
    }

    @DataProvider
    Iterator testGetInsertCommandText_data() {
        return new DataIterator() {
            {
                this.data(new TddUserTable() {
                }, "insert into tdd_user() values()");

                this.data(new TddUserTable() {
                    {
                        this.put(IColumn.f_id, 1);
                    }
                }, "insert into tdd_user(id) values(?)");

                this.data(new TddUserTable() {
                    {
                        this.put(IColumn.f_id, 1);
                        this.put(IColumn.f_first_name, "darui.wu");
                    }
                }, "insert into tdd_user(id,first_name) values(?,?)");
            }
        };
    }

    @Test(groups = "oracle")
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

    @Test(groups = "oracle")
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
