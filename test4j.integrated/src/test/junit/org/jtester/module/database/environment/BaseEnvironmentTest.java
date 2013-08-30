package org.jtester.module.database.environment;

import org.jtester.database.table.ITable;
import org.jtester.database.table.TddUserTable;
import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.module.database.IDatabase;
import org.junit.Test;

/**
 * 类BaseEnvironmentTest.java的实现描述：
 * 
 * @author darui.wudr 2013-1-8 下午1:24:10
 */
@SuppressWarnings("serial")
public class BaseEnvironmentTest implements IDatabase, ITable {

    @Test
    public void testConverToSqlValue_ValueIsEnum() throws Exception {
        db.table(t_tdd_user).clean().insert(2, new TddUserTable() {
            {
                this.put(IColumn.f_email, EmailEnum.values());
            }
        });
        db.table(t_tdd_user).query().reflectionEqMap(2, new TddUserTable() {
            {
                this.put(IColumn.f_email, "darui_wu", "davey_wu");
            }
        }, EqMode.EQ_STRING, EqMode.IGNORE_ORDER);
    }

    static enum EmailEnum {
        darui_wu,
        davey_wu;
    }
}
