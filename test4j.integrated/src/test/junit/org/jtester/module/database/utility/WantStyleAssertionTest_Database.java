package org.jtester.module.database.utility;

import org.jtester.database.table.ITable;
import org.jtester.database.table.TddUserTable;
import org.jtester.junit.JTester;
import org.jtester.tools.commons.ListHelper;
import org.junit.Test;

@SuppressWarnings({ "serial", "unchecked" })
public class WantStyleAssertionTest_Database implements JTester {

    @Test
    public void testDatabase() {
        db.table(ITable.t_tdd_user).clean().insert(2, new TddUserTable() {
            {
                this.put(IColumn.f_id, "1", "2");
                this.put(IColumn.f_first_name, "darui", "jobs");
                this.put(IColumn.f_last_name, "wu", "he");
            }
        });

        db.query("select id,first_name,last_name from tdd_user").reflectionEqMap(ListHelper.toList(new TddUserTable() {
            {
                this.put(IColumn.f_id, 1);
                this.put(IColumn.f_first_name, "darui");
            }
        }, new TddUserTable() {
            {
                this.put(IColumn.f_id, 2);
                this.put(IColumn.f_last_name, "he");
            }
        }));
    }
}
