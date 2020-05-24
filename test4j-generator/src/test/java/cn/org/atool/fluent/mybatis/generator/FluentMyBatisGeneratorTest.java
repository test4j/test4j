package cn.org.atool.fluent.mybatis.generator;

import org.junit.jupiter.api.Test;
import org.test4j.generator.mybatis.Generator;
import org.test4j.generator.mybatis.db.ColumnType;

public class FluentMyBatisGeneratorTest {
    private static String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    @Test
    public void generate() {
        String outputDir = System.getProperty("user.dir") + "/src/test/java";
        Generator.fluentMybatis()
            .globalConfig(config -> config
                .setOutputDir(outputDir, outputDir, outputDir)
                .setDataSource(url, "root", "password")
                .setBasePackage("cn.org.atool.fluent.mybatis.generator.demo")
            )
            .tables(config -> config
                .setTablePrefix("t_")
                .addTable("address")
                .addTable("t_user", true)
                .allTable(table -> {
                    table.setColumn("gmt_created", "gmt_modified", "is_deleted")
                        .column("is_deleted", ColumnType.BOOLEAN)
                        .addBaseDaoInterface("MyCustomerInterface<${entity}, ${query}, ${update}>", MyCustomerInterface.class.getName())
                    ;
                }))
            .tables(config -> config
                .addTable("no_primary")
                .addTable("no_auto_id")
                .allTable(table -> table.setMapperPrefix("new"))
            )
            .execute();
    }
}