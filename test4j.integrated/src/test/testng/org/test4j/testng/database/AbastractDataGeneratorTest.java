package org.test4j.testng.database;

import java.util.ArrayList;
import java.util.List;

import mockit.Invocation;
import mockit.Mock;

import org.test4j.database.table.ITable;
import org.test4j.database.table.TddUserTable;
import org.test4j.module.database.IDatabase;
import org.test4j.testng.Test4J;
import org.test4j.tools.datagen.DataSet;
import org.testng.annotations.Test;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Test(groups = { "test4j", "database" })
public class AbastractDataGeneratorTest extends Test4J implements IDatabase {
    @Test
    public void testValue() {
        final List actual = new ArrayList();
        new MockUp<DataSet>() {
            @Mock
            public void insert(Invocation it, String table) {
                List list = reflector.getField(it.getInvokedInstance(), "datas");
                actual.addAll(list);
            }
        };
        db.table(ITable.t_tdd_user).insert(2, new TddUserTable() {
            {
                this.put(IColumn.f_id, new String[] { "163", "sohu" });
                this.put(IColumn.f_first_name, new DataGenerator() {
                    @Override
                    public Object generate(int index) {
                        return "darui.wu@" + value("id") + ".com";
                    }
                });
            }
        });
        want.collection(actual)
                .sizeEq(2)
                .propertyEq(TddUserTable.IColumn.f_first_name, new String[] { "darui.wu@163.com", "darui.wu@sohu.com" });
    }
}
