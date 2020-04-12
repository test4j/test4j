package org.test4j.hamcrest.diff;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.model.Address;
import org.test4j.model.User;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.tools.commons.DateHelper;

class DiffUtilTest extends Test4J {

    @Test
    void diff_as_string_true() {
        DiffMap diff = new DiffUtil(false, true, false).diff(
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", 100)
                        .map(),
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", "100")
                        .map()
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(0);
    }

    @Test
    void diff_as_string_false() {
        DiffMap diff = new DiffUtil(false, false, false).diff(
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", 100)
                        .map(),
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", "100")
                        .map()
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(1);
        want.string(diff.message()).contains(new String[]{"$.key2", "(Integer) 100", "(String) 100"});
    }

    @Test
    void diff_ignoreNull_false() {
        DiffMap diff = new DiffUtil(false, true, false).diff(
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", 100)
                        .map(),
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", null)
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(1);
        want.string(diff.message()).contains(new String[]{"$.key2", "(Integer) 100", "<null>"});
    }

    @Test
    void diff_ignoreNull_true() {
        DiffMap diff = new DiffUtil(true, true, false).diff(
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", 100)
                        .map(),
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", null)
                        .map()
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(0);
    }

    @Test
    void diff_date_as_string() {
        DiffMap diff = new DiffUtil(false, true, false).diff(
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("date", DateHelper.parse("2020-04-19 23:10:01"))
                        .map(),
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("date", "2020-04-19 23:10:01")
                        .map()
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(0);
    }

    @Test
    void diff_date_as_string_false() {
        DiffMap diff = new DiffUtil(false, false, false).diff(
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("date", DateHelper.parse("2020-04-19 23:10:01"))
                        .map(),
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("date", "2020-04-19 23:10:01")
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(1);
        want.string(diff.message()).contains(new String[]{"$.date", "(Date) 2020-04-19 23:10:01", "(String) 2020-04-19 23:10:01"});
    }

    @Test
    void diff_nested_map_as_string() {
        DiffMap diff = new DiffUtil(false, true, false).diff(
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", DataMap.create()
                                .kv("key3", "value3")
                                .kv("date", DateHelper.parse("2020-04-19 23:10:01"))
                                .map())
                        .map(),
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", DataMap.create()
                                .kv("key3", "value3")
                                .kv("date", "2020-04-19 23:10:01")
                        )
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(0);
    }

    @Test
    void diff_nested_map_not_string() {
        DiffMap diff = new DiffUtil(false, false, false).diff(
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", DataMap.create()
                                .kv("key3", "value3")
                                .kv("date", DateHelper.parse("2020-04-19 23:10:01"))
                                .map())
                        .map(),
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", DataMap.create()
                                .kv("key3", "value3")
                                .kv("date", "2020-04-19 23:10:01")
                        )
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(1);
        want.string(diff.message()).contains(new String[]{"$.key2.date", "(Date) 2020-04-19 23:10:01", "(String) 2020-04-19 23:10:01"});
    }

    @Test
    void diff_nested_map_not_property() {
        DiffMap diff = new DiffUtil(false, false, false).diff(
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", DataMap.create()
                                .kv("key3", "value3").map()
                        )
                        .map(),
                DataMap.create()
                        .kv("key1", "value1")
                        .kv("key2", DataMap.create()
                                .kv("key3", "value3")
                                .kv("date", "2020-04-19 23:10:01")
                        )
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(1);
        want.string(diff.message()).contains(new String[]{"$.key2.date", "<null>", "(String) 2020-04-19 23:10:01"});
    }

    @Test
    void diff_user() {
        DiffMap diff = new DiffUtil(false, true, false).diff(
                new User()
                        .setName("name1")
                        .setAge(45)
                        .setAddress(new Address().setName("address1")
                                .setPostcode("23232")
                        )
                ,
                DataMap.create()
                        .kv("name", "name1")
                        .kv("age", "45")
                        .kv("address", DataMap.create()
                                .kv("name", "address1")
                                .kv("postcode", "23231"))
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isEqualTo(1);
        want.string(diff.message()).contains(new String[]{"$.address.postcode", "expect=(String) 23231", "actual=(String) 23232"});
    }
}