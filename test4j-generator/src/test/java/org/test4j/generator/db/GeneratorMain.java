package org.test4j.generator.db;

import org.test4j.generator.FileGenerator;
import org.test4j.generator.annotation.Table;
import org.test4j.generator.annotation.Tables;

/**
 * DataMapGeneratorTest
 *
 * @author darui.wu
 * @create 2020/5/7 11:47 上午
 */
public class GeneratorMain {
    private final static String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    public static void main(String[] args) {
        FileGenerator.build(Empty.class);
    }

    @Tables(
        url = url, username = "root", password = "password",
//        srcDir = "test4j-generator/src/test/java",
        testDir = "test4j-generator/src/test/java",
        basePack = "cn.org.atool.fluent.mybatis.generator.demo",
        tables = {
            @Table(value = "address"),
            @Table(value = "t_user", tablePrefix = "t_",
                gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted"),
            @Table(value = {"no_auto_id", "no_primary"}, mapperPrefix = "new")
        }
    )
    static class Empty {
    }
}