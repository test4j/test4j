package cn.org.atool.fluent.mybatis.generator;

import org.test4j.generator.mybatis.rule.ColumnType;
import org.junit.jupiter.api.Test;

public class MybatisGeneratorTest {
    private static String url = "jdbc:mysql://localhost:3306/fluent_mybatis";

    @Test
    public void generate() {
        String outdir = System.getProperty("user.dir") + "/src/test/java";
        new MybatisGenerator("cn.org.atool.fluent.mybatis.generator.demo")
                .setOutputDir(outdir, outdir, outdir)
                .setEntitySetChain(true)
                .setDataSource(url, "root", "password")
                .generate(new TableConvertor("t_")
                                .addTable("address")
                                .addTable("t_user", true)
                                .allTable(table -> {
                                    table.column("is_deleted", ColumnType.BOOLEAN)
                                            .setGmtCreateColumn("gmt_created")
                                            .setGmtModifiedColumn("gmt_modified")
                                            .setVersionColumn("version")
                                            .setLogicDeletedColumn("is_deleted")
                                            .addBaseDaoInterface("MyCustomerInterface<${entity}, ${query}, ${update}>", "cn.org.atool.fluent.mybatis.generator.MyCustomerInterface")
                                    ;
                                })
                        , new TableConvertor()
                                .addTable("no_auto_id")
                                .addTable("no_primary")
                                .allTable(table -> table.setMapperPrefix("new"))
                );
    }
}