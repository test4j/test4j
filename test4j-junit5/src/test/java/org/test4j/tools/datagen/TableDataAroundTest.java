package org.test4j.tools.datagen;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.module.database.sql.DataSourceCreatorFactory;
import org.test4j.module.spec.IStory;

import static org.test4j.db.ITable.t_user;

public class TableDataAroundTest extends Test4J implements IStory {
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
                .aroundDb()
                .initReady(DataMap.create().kv("age", "35"), "t_user")
                .when("验证around功能", () -> {
                });
    }
}
