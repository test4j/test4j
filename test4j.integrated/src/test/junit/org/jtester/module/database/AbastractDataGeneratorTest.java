package org.jtester.module.database;

import java.util.ArrayList;
import java.util.List;

import mockit.Mock;

import org.jtester.database.table.ITable;
import org.jtester.database.table.TddUserTable;
import org.jtester.junit.JTester;
import org.jtester.tools.datagen.DataSet;
import org.junit.Test;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class AbastractDataGeneratorTest implements JTester {
    @Test
    public void testValue() {
        final List actual = new ArrayList();
        new MockUp<DataSet>() {
            DataSet it;

            @Mock
            public void insert(String table) {
                List list = reflector.getField(it, "datas");
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
