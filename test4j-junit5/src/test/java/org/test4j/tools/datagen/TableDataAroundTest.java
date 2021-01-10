package org.test4j.tools.datagen;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.test4j.Test4J;
import org.test4j.module.database.proxy.DataSourceCreatorFactory;
import org.test4j.module.spec.IStory;
import org.test4j.module.spec.internal.TableDataAround;

import static org.test4j.asserts.matcher.modes.EqMode.EQ_STRING;
import static org.test4j.db.ITable.t_user;

public class TableDataAroundTest implements Test4J, IStory {
    @BeforeAll
    public static void setup() {
        DataSourceCreatorFactory.create("dataSource");
    }

    @Test
    void findFile() {
        String file = TableDataAround.findFile(this.getClass(), "findFile");
        want.string(file).eq("org/test4j/tools/datagen/TableDataAroundTest.findFile.json");
    }

    @Test
    void file_UnExisted() {
        want.exception(() -> TableDataAround.findFile(this.getClass(), "file_UnExisted")
            , RuntimeException.class).contains(new String[]{
            "org/test4j/tools/datagen/TableDataAroundTest.file_UnExisted.json",
            "org/test4j/tools/datagen/TableDataAroundTest/file_UnExisted.json"
        });
    }

    @Test
    void test_around_data() {
        db.table(t_user).clean();
        story.scenario()
            .dbAround()
            .initAround(
                DataMap.create().kv("age", "35"),
                DataMap.create().kv("age", "35"),
                "t_user"
            )
            .handleAround(
                data -> data.apply(table -> table.kv("address_id", "23"), true, t_user),
                data -> data.dataMap(t_user).kv("address_id", "23")
            )
            .when("验证around功能", () -> {
            });
        db.table(t_user).query().eqDataMap(DataMap.create(2)
                .kv("age", "35")
                .kv("address_id", "23")
                .kv("user_name", "nam1", "nam2")
            , EQ_STRING
        );
    }
}