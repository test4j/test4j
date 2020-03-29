package org.test4j.module.database.environment;


import org.junit.jupiter.api.Test;
import org.test4j.db.ITable;
import org.test4j.db.datamap.table.UserTableMap;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.module.database.IDatabase;

import static org.test4j.db.mapping.UserMP.*;

/**
 * 类BaseEnvironmentTest.java的实现描述：
 *
 * @author darui.wudr 2013-1-8 下午1:24:10
 */
public class BaseEnvironmentTest implements IDatabase, ITable {

    @Test
    public void testConvertToSqlValue_ValueIsEnum() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(2)
                .arr(Column.e_mail, EmailEnum.values())
        );
        db.table(t_user).query().eqMap(new UserTableMap(2)
                        .kv(Column.e_mail, "darui_wu", "davey_wu")
                , EqMode.EQ_STRING, EqMode.IGNORE_ORDER);
    }

    enum EmailEnum {
        darui_wu,
        davey_wu;
    }
}
