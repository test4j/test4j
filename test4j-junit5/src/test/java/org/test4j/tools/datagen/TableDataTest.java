package org.test4j.tools.datagen;

import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.json.JSON;
import org.test4j.junit5.Test4J;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.tools.commons.ResourceHelper;

import java.io.FileNotFoundException;

class TableDataTest extends Test4J {

    @Test
    void test_map_list() throws FileNotFoundException {
        String text = ResourceHelper.readFromFile("org/test4j/datamap/datamap_list.json");
        TableData data = TableData.map(text);
        MessageHelper.info(JSON.toJSON(data, true));
        want.list(data.keySet()).eqReflect(new String[]{"t_user", "t_user2"});
        want.list(data.get("t_user")).eqDataMap(DataMap.create(2)
                        .kv("user_name", "nam1", "nam2")
                , EqMode.IGNORE_DEFAULTS
        );
    }

    @Test
    void test_map() throws FileNotFoundException {
        String text = ResourceHelper.readFromFile("org/test4j/datamap/datamap1.json");
        TableData data = TableData.map(text);
        MessageHelper.info(JSON.toJSON(data, true));
        want.list(data.keySet()).eqReflect(new String[]{"t_user"});
        want.list(data.get("t_user")).eqDataMap(DataMap.create(2)
                        .kv("user_name", "nam1", "nam2")
                , EqMode.IGNORE_DEFAULTS
        );
    }

    @Test
    void test_map_insert() throws FileNotFoundException {
        String text = ResourceHelper.readFromFile("org/test4j/datamap/user_address.json");
        TableData data = TableData.map(text)
                .with("t_user", DataMap.create(2)
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
        String text2 = ResourceHelper.readFromFile("org/test4j/datamap/user_address_query.json");
        TableData data2 = TableData.map(text2);
        want.exception(() -> db.queryEq(data2), AssertionError.class)
                .contains(new String[]{
                        "$[1]~[1].age", "(String) 34", "(Integer) 45"
                });
    }
}