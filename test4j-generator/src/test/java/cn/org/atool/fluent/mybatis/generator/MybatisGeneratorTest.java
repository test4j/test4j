package cn.org.atool.fluent.mybatis.generator;

import org.test4j.generator.mybatis.model.BuildConfig;
import org.test4j.generator.mybatis.model.Generator;
import org.test4j.generator.mybatis.rule.ColumnType;
import org.junit.jupiter.api.Test;

public class MybatisGeneratorTest {
    private static String url = "jdbc:mysql://localhost:3306/fluent_mybatis";

    @Test
    public void generate() {
        String outputDir = System.getProperty("user.dir") + "/src/test/java";
        new Generator(
            new BuildConfig()
                .setTablePrefix("t_")
                .addTable("address")
                .addTable("t_user", true)
                .allTable(table -> {
                    table.setColumn("gmt_created", "gmt_modified", "is_deleted")
                        .column("is_deleted", ColumnType.BOOLEAN)
                        .addBaseDaoInterface("MyCustomerInterface<${entity}, ${query}, ${update}>", "cn.org.atool.fluent.mybatis.generator.MyCustomerInterface")
                    ;
                })
            ,
            new BuildConfig()
                .addTable("no_auto_id")
                .addTable("no_primary")
                .allTable(table -> table.setMapperPrefix("new"))
        )
            .setOutputDir(outputDir, outputDir, outputDir)
            .setDataSource(url, "root", "password")
            .setBasePackage("cn.org.atool.fluent.mybatis.generator")
            .execute();
    }
}