package org.test4j.module.database.dbop;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.Test4J;
import org.test4j.db.dm.UserDataMap;

import org.test4j.tools.datagen.DataProvider;

import java.util.Iterator;


@SuppressWarnings({"rawtypes", "serial"})
public class InsertOpTest implements Test4J {
    @ParameterizedTest
    @MethodSource("testGetInsertCommandText_data")
    public void testGetInsertCommandText(DataMap data, String result) {
        InsertOp ds = reflector.newInstance(InsertOp.class);
        reflector.setFieldValue(ds, "quato", "");
        reflector.setFieldValue(ds, "table", "t_user");

        String text = reflector.invoke(ds, "getInsertCommandText", data);
        want.string(text).eqWithStripSpace(result);
    }

    public static Iterator testGetInsertCommandText_data() {
        return new DataProvider() {
            {
                this.data(new UserDataMap(true)
                    , "insert into t_user() values()");

                this.data(new UserDataMap(true)
                        .kv("id", 1)
                    , "insert into t_user(id) values(?)");

                this.data(new UserDataMap(true)
                        .kv("id", 1)
                        .kv("first_name", "darui.wu")
                    , "insert into t_user(id,first_name) values(?,?)");
            }
        };
    }
}