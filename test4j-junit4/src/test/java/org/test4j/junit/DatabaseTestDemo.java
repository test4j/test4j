package org.test4j.junit;

import org.junit.Test;

public class DatabaseTestDemo extends Test4J {
    @Test
    public void test() {
        String table = "db_test_demo_table";
        db.execute(new StringBuilder()
                .append("drop table if exists ").append(table).append(";")
                .append("create table ").append(table).append("(")
                .append("id int primary key not null,")
                .append("name varchar(20) null)")
                .toString());
        // 准备数据
        db.table(table).clean().insert(new DataMap(2) {
            {
                this.kv("id", 1, 2);
                this.kv("name", "1", "2");
            }
        });
        // 执行测试方法，这里简单调用db功能，更新记录
        db.execute("update " + table + " set name='45' where id = 2");
        // 验证数据库
        db.table(table).query().eqDataMap(new DataMap(2) {
            {
                this.kv("id", 1, 2);
                this.kv("name", "1", "45");
            }
        });
    }
}
