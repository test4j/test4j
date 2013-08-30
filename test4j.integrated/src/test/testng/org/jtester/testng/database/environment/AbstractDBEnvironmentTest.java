package org.jtester.testng.database.environment;

import java.util.HashMap;
import java.util.Map;

import mockit.Mock;

import org.jtester.database.table.ITable;
import org.jtester.database.table.TddUserTable;
import org.jtester.module.database.environment.DBEnvironment;
import org.jtester.module.database.environment.DBEnvironmentFactory;
import org.jtester.module.database.environment.TableMeta;
import org.jtester.module.database.environment.TableMeta.ColumnMeta;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
@Test(groups = { "jtester", "database" })
public class AbstractDBEnvironmentTest extends JTester {

    @Test
    public void testGetTableMetaData() throws Exception {
        new MockUp<ColumnMeta>() {
            @Mock
            public boolean isNullable() {
                return false;
            }
        };
        DBEnvironment db = DBEnvironmentFactory.getCurrentDBEnvironment();
        TableMeta meta = db.getTableMetaData(ITable.t_tdd_user);
        Map data = new HashMap() {
            {
                this.put(TddUserTable.IColumn.f_id, "123");
                this.put(TddUserTable.IColumn.f_first_name, "darui.wu");
            }
        };
        meta.fillData(data, db);
        want.object(meta).notNull();
        want.map(meta.getColumns()).sizeEq(13);
        want.map(data).sizeEq(13).reflectionEqMap(new TddUserTable() {
            {
                this.put(IColumn.f_id, "123");
                this.put(IColumn.f_first_name, "darui.wu");
                this.put(IColumn.f_post_code, "jteste");
                this.put(IColumn.f_address_id, 0);
                this.put(IColumn.f_last_name, "jtester");
                this.put(IColumn.f_sarary, 0.0);
            }
        });
    }
}
