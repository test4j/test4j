package cn.org.atool.fluent.mybatis.generator;

import org.test4j.generator.mybatis.DataMapGenerator;
import org.test4j.generator.mybatis.db.ColumnType;

/**
 * DataMapGeneratorTest
 *
 * @author darui.wu
 * @create 2020/5/7 11:47 上午
 */
public class DataMapGeneratorMain {
    private static String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    public static void main(String[] args) {
        String outputDir = System.getProperty("user.dir") + "/test4j-generator/src/test/java";
        DataMapGenerator.build()
            .globalConfig(config -> config
                .setOutputDir(outputDir)
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
                    ;
                })
            )
            .tables(config -> config
                .addTable("no_auto_id")
                .addTable("no_primary")
                .allTable(table -> table.setMapperPrefix("new"))
            )
            .execute();
    }
}