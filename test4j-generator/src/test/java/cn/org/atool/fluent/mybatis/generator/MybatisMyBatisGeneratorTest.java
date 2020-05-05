package cn.org.atool.fluent.mybatis.generator;

import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.TableConfig;
import org.test4j.generator.mybatis.MyBatisGenerator;
import org.test4j.generator.mybatis.db.ColumnType;
import org.junit.jupiter.api.Test;

public class MybatisMyBatisGeneratorTest {
    private static String url = "jdbc:mysql://localhost:3306/fluent_mybatis";

    @Test
    public void generate() {
        String outputDir = System.getProperty("user.dir") + "/src/test/java";
        new MyBatisGenerator(
            new TableConfig()
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
            new TableConfig()
                .addTable("no_auto_id")
                .addTable("no_primary")
                .allTable(table -> table.setMapperPrefix("new"))
        ).setGlobalConfig(new GlobalConfig()
            .setOutputDir(outputDir, outputDir, outputDir)
            .setDataSource(url, "root", "password")
            .setBasePackage("cn.org.atool.fluent.mybatis.generator.demo")
        ).execute();
    }
}