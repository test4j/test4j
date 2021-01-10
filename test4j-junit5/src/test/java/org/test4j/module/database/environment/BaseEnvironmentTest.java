package org.test4j.module.database.environment;


import org.junit.jupiter.api.Test;
import org.test4j.db.ITable;
import org.test4j.db.dm.UserDataMap;
import org.test4j.asserts.matcher.modes.EqMode;
import org.test4j.module.database.IDatabase;

/**
 * 类BaseEnvironmentTest.java的实现描述：
 *
 * @author darui.wudr 2013-1-8 下午1:24:10
 */
public class BaseEnvironmentTest implements IDatabase, ITable {

    @Test
    public void testConvertToSqlValue_ValueIsEnum() throws Exception {
        db.table(t_user).clean().insert(new UserDataMap(true, 2)
            .arr("e_mail", (Object[]) EmailEnum.values())
        );
        db.table(t_user).query().eqMap(new UserDataMap(true, 2)
                .kv("e_mail", "darui_wu", "davey_wu")
            , EqMode.EQ_STRING, EqMode.IGNORE_ORDER);
    }

    enum EmailEnum {
        darui_wu,
        davey_wu;
    }
}