package cn.org.atool.fluent.mybatis.generator;

import org.junit.jupiter.api.Test;
import org.test4j.generator.mybatis.DataMapGenerator;
import org.test4j.generator.mybatis.config.GlobalConfig;
import org.test4j.generator.mybatis.config.TableConfig;
import org.test4j.generator.mybatis.db.ColumnType;
import org.test4j.junit5.Test4J;

/**
 * DataMapGeneratorTest
 *
 * @author darui.wu
 * @create 2020/5/7 11:47 上午
 */
public class DataMapGeneratorTest extends Test4J {
    private static String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    @Test
    public void generate() {
        String outputDir = System.getProperty("user.dir") + "/src/test/java";
        new DataMapGenerator(
            new TableConfig()
                .setTablePrefix("t_")
                .addTable("address")
                .addTable("t_user", true)
                .allTable(table -> {
                    table.setColumn("gmt_created", "gmt_modified", "is_deleted")
                        .column("is_deleted", ColumnType.BOOLEAN)
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
            .setBasePackage("cn.org.atool.fluent.mybatis.generator.dmtest")
        ).execute();
    }
}