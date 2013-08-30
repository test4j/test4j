package org.test4j.module.database.environment;

import java.util.Map;

import mockit.Mock;

import org.junit.Test;
import org.test4j.database.table.ITable;
import org.test4j.database.table.TddUserTable;
import org.test4j.junit.Test4J;
import org.test4j.module.database.environment.TableMeta.ColumnMeta;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class AbstractDBEnvironmentTest implements Test4J {

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
        Map data = new TddUserTable() {
            {
                this.put(IColumn.f_id, "123");
                this.put(IColumn.f_first_name, "darui.wu");
            }
        };
        meta.fillData(data, db);
        want.object(meta).notNull();
        want.map(meta.getColumns()).sizeEq(13);
        want.map(data).sizeEq(13).reflectionEqMap(new TddUserTable() {
            {
                this.put(IColumn.f_id, "123");
                this.put(IColumn.f_first_name, "darui.wu");
                this.put(IColumn.f_post_code, "test4j");
                this.put(IColumn.f_address_id, 0);
                this.put(IColumn.f_last_name, "test4j");
                this.put(IColumn.f_sarary, 0.0);
            }
        });
    }
}
