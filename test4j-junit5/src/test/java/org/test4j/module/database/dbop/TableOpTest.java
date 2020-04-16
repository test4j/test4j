package org.test4j.module.database.dbop;

import org.junit.jupiter.api.Test;
import org.test4j.db.ITable;
import org.test4j.db.datamap.table.UserTableMap;
import org.test4j.junit5.Test4J;

import java.sql.SQLException;
import java.util.Date;

import static org.test4j.db.mapping.UserMP.Column;

@SuppressWarnings({"serial", "unchecked"})
public class TableOpTest extends Test4J {

    @Test
    public void testInsertData() throws SQLException {
        db.table(ITable.t_user).clean().insert(new UserTableMap()
                .kv(Column.id, 1)
                .kv(Column.first_name, "darui.wu")
                .kv(Column.gmt_created, new Date())
        );
        db.table(ITable.t_user).query().eqMap(new UserTableMap(1)
                .kv(Column.id, 1)
                .kv(Column.first_name, "darui.wu")
        );
    }


    @Test
    public void testInsert_NoSuchColumn() throws SQLException {
        try {
            new TableOp(ITable.t_user)
                    .insert(new UserTableMap()
                            .kv("no_column", "darui.wu")
                            .kv(Column.gmt_created, new Date())
                    );
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).contains("no_column");
        }
    }

    @Test
    public void testInsert_BadType() throws SQLException {
        try {
            new TableOp(ITable.t_user).insert(new UserTableMap()
                    .kv(Column.first_name, "darui.wu")
                    .kv(Column.gmt_created, "2011-08-19ss")
            );
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).contains("2011-08-19ss");
        }
    }

    @Test
    public void testInsert_DataIterator() {
        new TableOp(ITable.t_user).clean().insert(new UserTableMap(2)
                .kv(Column.id, 1, 2)
                .kv(Column.first_name, "darui.wu", "data.iterator")
        );
        db.query("select * from t_user").sizeEq(2)
                .eqByProperties(Column.first_name, new String[]{"darui.wu", "data.iterator"});
    }

    @Test
    public void testInsert_DataIterator_1() {
        db.table(ITable.t_user).clean().insert(new UserTableMap()
                        .kv(Column.id, 1)
                        .kv(Column.first_name, "darui.wu")
                , new UserTableMap()
                        .kv(Column.id, 2)
                        .kv(Column.first_name, "data.iterator")
        );
        db.query("select * from t_user").sizeEq(2)
                .eqByProperties(Column.first_name, new String[]{"darui.wu", "data.iterator"});
    }

    @Test
    public void testInsert_DataIterator_2() {
        db.table(ITable.t_user).clean().insert(new UserTableMap()
                .kv(Column.id, 1)
                .kv(Column.first_name, "darui.wu")
        );
        db.table(ITable.t_user).insert(new UserTableMap()
                .kv(Column.id, 2)
                .kv(Column.first_name, "data.iterator")
        );
        db.query("select * from t_user").sizeEq(2)
                .eqByProperties(Column.first_name, new String[]{"darui.wu", "data.iterator"});
    }

    @Test
    public void testCount_MySQL() {
        db.table(ITable.t_user).count().notNull();
    }

    @Test
    public void testQueryWhere_DataMap() {
        db.table(ITable.t_user).clean().insert(new UserTableMap(3)
                .kv(Column.id, 100, 101, 102)
                .kv(Column.first_name, "name1", "name2", "name3")
                .kv(Column.post_code, "310012", "310000")
        );
        db.table(ITable.t_user).count().eq(3);
        db.table(ITable.t_user).queryWhere(new UserTableMap()
                .kv(Column.post_code, "310000")
        ).eqByProperties(Column.id, new int[]{101, 102});
    }

    @Test
    public void testQueryWhere_String() {
        db.table(ITable.t_user).clean().insert(new UserTableMap(3)
                .kv(Column.id, 100, 101, 102)
                .kv(Column.first_name, "name1", "name2", "name3")
                .kv(Column.post_code, "310012", "310000")
        );
        db.table(ITable.t_user).count().eq(3);
        db.table(ITable.t_user).queryWhere("post_code=310000").eqByProperties("id", new int[]{101, 102});
    }
}
