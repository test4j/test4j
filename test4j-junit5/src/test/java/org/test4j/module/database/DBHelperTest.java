package org.test4j.module.database;

import java.io.FileNotFoundException;
import java.util.Iterator;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit5.Test4J;
import org.test4j.module.database.utility.DBHelper;
import org.test4j.tools.commons.ResourceHelper;
import org.test4j.tools.datagen.DataProvider;

@SuppressWarnings({"rawtypes", "serial"})
public class DBHelperTest extends Test4J {

    @Test
    public void testParseSQL() throws FileNotFoundException {
        String lines = ResourceHelper.readFromFile("org/test4j/module/database/testParseSQL.sql");
        String[] statments = DBHelper.parseSQL(lines);
        want.array(statments).sizeEq(6);
    }

    @Test
    public void testParseSQL_SINGLE_LINE_NOTE_REGEX() {
        String line = "insert into tdd_user(id,first_name,last_name,my_date,sarary)--本行注释\n"
                + "values(3,'name3','last3','2011-03-20',2336);";
        String[] statments = DBHelper.parseSQL(line);
        want.array(statments)
                .sizeEq(1)
                .hasItems(
                        "insert into tdd_user(id,first_name,last_name,my_date,sarary) values(3,'name3','last3','2011-03-20',2336)");
    }

    @Test
    public void testParseSQL_MULTI_LINE_NOTE_REGEX() {
        String line = "insert into tdd_user(id,first_name,last_name,my_date,sarary)\n"
                + "values(2,'name2','last2',/**插入注释**/'2011-03-19',2335);";
        String[] statments = DBHelper.parseSQL(line);
        want.array(statments)
                .sizeEq(1)
                .hasItems(
                        "insert into tdd_user(id,first_name,last_name,my_date,sarary) values(2,'name2','last2','2011-03-19',2335)");
    }

    @Test
    public void testParseSQL_LineHasEnterChar() {
        String line = "insert into tdd_user(id,first_name,last_name,my_date,sarary)\n"
                + "values(3,'name\n3','last3','2011-03-20',2336);";
        String statement = "insert into tdd_user(id,first_name,last_name,my_date,sarary) values(3,'name\n3','last3','2011-03-20',2336)";
        String[] statments = DBHelper.parseSQL(line);
        want.array(statments).sizeEq(1);
        want.string(statments[0]).isEqualTo(statement);
    }

    @ParameterizedTest
    @MethodSource("dataGetWhereCondiction")
    public void testGetWhereCondiction(DataMap map, String where) {
        String result = DBHelper.getWhereCondiction(map);
        want.string(result).eq(where, StringMode.SameAsSpace);
    }

    public static Iterator dataGetWhereCondiction() {
        return new DataProvider()
                .data(null, "")
                .data(new DataMap(), "")
                .data(new DataMap()
                                .kv("key1", null)
                        , " where key1=?")
                .data(new DataMap()
                                .kv("key1", null)
                                .kv("key2", null)
                        , " where key1=? and key2=?")
                .data(new DataMap()
                                .kv("key1", null)
                                .kv("key2", null)
                                .kv("key3", null)
                        , " where key1=? and key2=? and key3=?");
    }
}
