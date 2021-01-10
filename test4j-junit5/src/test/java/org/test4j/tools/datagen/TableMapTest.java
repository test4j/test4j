package org.test4j.tools.datagen;

import org.junit.jupiter.api.Test;
import org.test4j.Test4J;
import org.test4j.exception.ExtraMessageError;
import org.test4j.asserts.matcher.modes.EqMode;

import org.test4j.module.database.datagen.TableMap;
import org.test4j.tools.commons.ResourceHelper;

import java.io.FileNotFoundException;

class TableMapTest implements Test4J {

    @Test
    void test_map_list() throws FileNotFoundException {
        String text = ResourceHelper.readFromFile("org/test4j/tools/datagen/datamap_list.json");
        TableMap data = TableMap.fromText(text);
        want.list(data.keySet()).eqReflect(new String[]{"t_user", "t_user2"});
        want.list(data.get("t_user").findDataList())
                .eqDataMap(DataMap.create(2)
                                .kv("user_name", "nam1", "nam2")
                        , EqMode.IGNORE_DEFAULTS
                );
    }

    @Test
    void test_map() throws FileNotFoundException {
        String text = ResourceHelper.readFromFile("org/test4j/tools/datagen/datamap1.json");
        TableMap data = TableMap.fromText(text);
        want.list(data.keySet()).eqReflect(new String[]{"t_user"});
        want.list(data.get("t_user").findDataList()).eqDataMap(DataMap.create(2)
                        .kv("user_name", "nam1", "nam2")
                , EqMode.IGNORE_DEFAULTS
        );
    }

    @Test
    void test_map_insert() throws FileNotFoundException {
        String text = ResourceHelper.readFromFile("org/test4j/tools/datagen/user_address.json");
        TableMap data = TableMap.fromText(text);
        data.get("t_user").setInit(DataMap.create(2)
                .kv("age", 45, 34)
        );
        db.insert(data, true);
        db.table("t_user").query().eqDataMap(DataMap.create(2)
                        .kv("user_name", "nam1", "nam2")
                        .kv("age", 45, 34)
                , EqMode.IGNORE_DEFAULTS
        );
        db.table("address").query().eqDataMap(DataMap.create(3)
                .kv("address", "address1", "address2", "address3")
        );
        String text2 = ResourceHelper.readFromFile("org/test4j/tools/datagen/user_address_query.json");
        TableMap data2 = TableMap.fromText(text2);
        want.exception(() -> db.queryEq(data2), ExtraMessageError.class)
                .contains(new String[]{
                        "$[1]~[1].age", "(String) 34", "(Integer) 45"
                });
    }
}