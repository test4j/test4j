package org.test4j.junit4;

import org.junit.Test;
import org.test4j.db.datamap.table.UserTableMap;
import org.test4j.junit4.Test4J;

public class DatabaseTestDemo extends Test4J {
    @Test
    public void test() {
        String table = "t_user";

        // 准备数据
        db.table(table).clean().insert(new UserTableMap(2)
                .id.values(1, 2)
                .user_name.values("1", "2")
        );
        // 执行测试方法，这里简单调用db功能，更新记录
        db.execute("update " + table + " set user_name='45' where id = 2");
        // 验证数据库
        db.table(table).query().eqDataMap(new UserTableMap(2)
                .id.values(1, 2)
                .user_name.values("1", "45")
        );
    }
}
