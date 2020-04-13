package org.test4j.hamcrest.diff;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.model.Address;
import org.test4j.model.User;
import org.test4j.module.core.utility.MessageHelper;

/**
 * @author darui.wu
 * @create 2020/4/13 2:20 下午
 */
public class DiffTest_Object extends Test4J {
    @Test
    void test_object() {
        DiffMap diff = new DiffFactory().diff(
                new User().setName("name1")
                        .setAddress(new Address().setName("add1").setPostcode("123")),
                new User().setName("name1")
                        .setAddress(new Address().setName("add1").setPostcode("124"))
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isGt(0);
        want.string(diff.message()).contains(new String[]{"$.address.postcode", "(String) 124", "(String) 123"});
    }

    @Test
    void test_object_list() {
        DiffMap diff = new DiffFactory().diff(
                new User().setName("name1")
                        .setAddresses(list(new Address().setName("add1").setPostcode("123"))),
                new User().setName("name1")
                        .setAddresses(list(new Address().setName("add1").setPostcode("124")))
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isGt(0);
        want.string(diff.message()).contains(new String[]{"$.addresses[1]~[1].postcode", "(String) 124", "(String) 123"});
    }

    @Test
    void test_diff_by_hybrid() {
        DiffMap diff = new DiffFactory().diff(
                new User().setName("name1")
                        .setAddresses(list(new Address().setName("add1").setPostcode("123"))),
                DataMap.create()
                        .kv("name", "name1")
                        .kv("addresses", list(new Address().setName("add1").setPostcode("124")))
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isGt(0);
        want.string(diff.message()).contains(new String[]{"$.addresses[1]~[1].postcode", "(String) 124", "(String) 123"});
    }

    @Test
    void test_diff_by_matcher() {
        DiffMap diff = new DiffFactory().diff(
                new User().setName("name1")
                        .setAddresses(list(new Address().setName("add1").setPostcode("123"))),
                DataMap.create()
                        .kv("name", "name1")
                        .kv("addresses", the.collection().eqReflect(new Object[]{new Address().setName("add1").setPostcode("124")}))
        );
        MessageHelper.info(diff.message());
        want.number(diff.diff).isGt(0);
        want.string(diff.message()).contains(new String[]{"$.addresses", "actual=(ArrayList)", "124", "123"});
    }
}