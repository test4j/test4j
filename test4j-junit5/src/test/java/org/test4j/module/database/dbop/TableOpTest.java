package org.test4j.module.database.dbop;

import org.junit.jupiter.api.Test;
import org.test4j.db.ITable;
import org.test4j.db.dm.UserDataMap;
import org.test4j.junit5.Test4J;

import java.sql.SQLException;
import java.util.Date;


@SuppressWarnings({"serial", "unchecked"})
public class TableOpTest extends Test4J {

    @Test
    public void testInsertData() throws SQLException {
        db.table(ITable.t_user).clean().insert(new UserDataMap(true)
            .id.values(1)
            .firstName.values("darui.wu")
            .gmtCreated.values(new Date())
        );
        db.table(ITable.t_user).query().eqMap(new UserDataMap(true, 1)
            .id.values(1)
            .firstName.values("darui.wu")
        );
    }

    @Test
    public void testInsert_BadType() throws SQLException {
        try {
            new TableOp(ITable.t_user).insert(new UserDataMap(true)
                .firstName.values("darui.wu")
                .gmtCreated.values("2011-08-19ss")
            );
            want.fail();
        } catch (Exception e) {
            String message = e.getMessage();
            want.string(message).contains("2011-08-19ss");
        }
    }

    @Test
    public void testInsert_DataIterator() {
        new TableOp(ITable.t_user).clean().insert(new UserDataMap(true, 2)
            .id.values(1, 2)
            .firstName.values("darui.wu", "data.iterator")
        );
        db.query("select * from t_user").sizeEq(2)
            .eqByProperties("first_name", new String[]{"darui.wu", "data.iterator"});
    }

    @Test
    public void testInsert_DataIterator_1() {
        db.table(ITable.t_user).clean().insert(new UserDataMap(true)
                .id.values(1)
                .firstName.values("darui.wu")
            , new UserDataMap(true)
                .id.values(2)
                .firstName.values("data.iterator")
        );
        db.query("select * from t_user").sizeEq(2)
            .eqByProperties("first_name", new String[]{"darui.wu", "data.iterator"});
    }

    @Test
    public void testInsert_DataIterator_2() {
        db.table(ITable.t_user).clean().insert(new UserDataMap(true)
            .id.values(1)
            .firstName.values("darui.wu")
        );
        db.table(ITable.t_user).insert(new UserDataMap(true)
            .id.values(2)
            .firstName.values("data.iterator")
        );
        db.query("select * from t_user").sizeEq(2)
            .eqByProperties("first_name", new String[]{"darui.wu", "data.iterator"});
    }

    @Test
    public void testCount_MySQL() {
        db.table(ITable.t_user).count().notNull();
    }

    @Test
    public void testQueryWhere_DataMap() {
        db.table(ITable.t_user).clean().insert(new UserDataMap(true, 3)
            .id.values(100, 101, 102)
            .firstName.values("name1", "name2", "name3")
            .postCode.values("310012", "310000")
        );
        db.table(ITable.t_user).count().eq(3);
        db.table(ITable.t_user).queryWhere(new UserDataMap(true)
            .postCode.values("310000")
        ).eqByProperties("id", new int[]{101, 102});
    }

    @Test
    public void testQueryWhere_String() {
        db.table(ITable.t_user).clean().insert(new UserDataMap(true, 3)
            .id.values(100, 101, 102)
            .firstName.values("name1", "name2", "name3")
            .postCode.values("310012", "310000")
        );
        db.table(ITable.t_user).count().eq(3);
        db.table(ITable.t_user).queryWhere("post_code=310000").eqByProperties("id", new int[]{101, 102});
    }
}
