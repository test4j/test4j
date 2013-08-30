package org.jtester.hamcrest.iassert.object.impl;

import java.util.Arrays;

import org.jtester.junit.JTester;
import org.junit.Test;

public class JSONAssertTest implements JTester {
    String json_arr = "[{\"gmtModified\":\"2009-06-19\",\"employeeId\":\"1234\",\"loginId\":\"system\",\"phone\":null,\"gmtCreate\":\"2008-02-26\",\"status\":\"enable\",\"isDeleted\":\"n\",\"password\":\"mhmW78lxgfCu4YMhqjs7Eg==\",\"gmtLastPasswordChanged\":\"2009-06-19\",\"creator\":\"sys\",\"homepageUrl\":null,\"modifier\":\"esb\",\"mobilePhone\":null,\"email\":\"system@b2btest.com\",\"name\":\"system\",\"gender\":\"M\",\"language\":\"zh_CN\",\"signature\":null},{\"gmtModified\":\"2009-08-14\",\"employeeId\":null,\"loginId\":\"wrreer\",\"phone\":null,\"gmtCreate\":\"2009-08-14\",\"status\":\"enable\",\"isDeleted\":\"n\",\"password\":\"reerer\",\"gmtLastPasswordChanged\":null,\"creator\":\"demo\",\"homepageUrl\":null,\"modifier\":\"demo\",\"mobilePhone\":null,\"email\":null,\"name\":\"asdsda\",\"gender\":\"m\",\"language\":null,\"signature\":null},{\"gmtModified\":\"2009-08-14\",\"employeeId\":null,\"loginId\":\"wrreer\",\"phone\":null,\"gmtCreate\":\"2009-08-14\",\"status\":\"enable\",\"isDeleted\":\"n\",\"password\":\"reerer\",\"gmtLastPasswordChanged\":null,\"creator\":\"demo\",\"homepageUrl\":null,\"modifier\":\"demo\",\"mobilePhone\":null,\"email\":null,\"name\":\"asdsda\",\"gender\":\"m\",\"language\":null,\"signature\":null},{\"gmtModified\":\"2009-08-14\",\"employeeId\":null,\"loginId\":\"wrreer\",\"phone\":null,\"gmtCreate\":\"2009-08-14\",\"status\":\"enable\",\"isDeleted\":\"n\",\"password\":\"reerer\",\"gmtLastPasswordChanged\":null,\"creator\":\"demo\",\"homepageUrl\":null,\"modifier\":\"demo\",\"mobilePhone\":null,\"email\":null,\"name\":\"asdsda\",\"gender\":\"m\",\"language\":null,\"signature\":null},{\"gmtModified\":\"2009-08-14\",\"employeeId\":null,\"loginId\":\"wrreer\",\"phone\":null,\"gmtCreate\":\"2009-08-14\",\"status\":\"enable\",\"isDeleted\":\"n\",\"password\":\"reerer\",\"gmtLastPasswordChanged\":null,\"creator\":\"demo\",\"homepageUrl\":null,\"modifier\":\"demo\",\"mobilePhone\":null,\"email\":null,\"name\":\"asdsda\",\"gender\":\"m\",\"language\":null,\"signature\":null},{\"gmtModified\":\"2009-08-14\",\"employeeId\":null,\"loginId\":\"wrreer\",\"phone\":null,\"gmtCreate\":\"2009-08-14\",\"status\":\"enable\",\"isDeleted\":\"n\",\"password\":\"reerer\",\"gmtLastPasswordChanged\":null,\"creator\":\"demo\",\"homepageUrl\":null,\"modifier\":\"demo\",\"mobilePhone\":null,\"email\":null,\"name\":\"asdsda\",\"gender\":\"m\",\"language\":null,\"signature\":null},{\"gmtModified\":\"2009-08-14\",\"employeeId\":null,\"loginId\":\"mooon\",\"phone\":null,\"gmtCreate\":\"2009-08-14\",\"status\":\"enbale\",\"isDeleted\":\"n\",\"password\":\"11222132\",\"gmtLastPasswordChanged\":null,\"creator\":\"demo\",\"homepageUrl\":null,\"modifier\":\"demo\",\"mobilePhone\":null,\"email\":null,\"name\":\"light\",\"gender\":\"m\",\"language\":null,\"signature\":null},{\"gmtModified\":\"2009-08-14\",\"employeeId\":null,\"loginId\":\"mooon\",\"phone\":null,\"gmtCreate\":\"2009-08-14\",\"status\":\"enbale\",\"isDeleted\":\"n\",\"password\":\"11222132\",\"gmtLastPasswordChanged\":null,\"creator\":\"demo\",\"homepageUrl\":null,\"modifier\":\"demo\",\"mobilePhone\":null,\"email\":null,\"name\":\"light\",\"gender\":\"m\",\"language\":null,\"signature\":null},{\"gmtModified\":\"2009-08-14\",\"employeeId\":null,\"loginId\":\"mooon\",\"phone\":null,\"gmtCreate\":\"2009-08-14\",\"status\":\"enbale\",\"isDeleted\":\"n\",\"password\":\"11222132\",\"gmtLastPasswordChanged\":null,\"creator\":\"demo\",\"homepageUrl\":null,\"modifier\":\"demo\",\"mobilePhone\":null,\"email\":null,\"name\":\"light\",\"gender\":\"m\",\"language\":null,\"signature\":null},{\"gmtModified\":\"2009-08-14\",\"employeeId\":null,\"loginId\":\"mooon\",\"phone\":null,\"gmtCreate\":\"2009-08-14\",\"status\":\"enbale\",\"isDeleted\":\"n\",\"password\":\"11222132\",\"gmtLastPasswordChanged\":null,\"creator\":\"demo\",\"homepageUrl\":null,\"modifier\":\"demo\",\"mobilePhone\":null,\"email\":null,\"name\":\"light\",\"gender\":\"m\",\"language\":null,\"signature\":null}]";

    @Test
    public void testIsJSONArray() {
        want.json(json_arr).isJSONArray();
    }

    @Test(expected = AssertionError.class)
    public void testIsJSONMap_failure() {
        want.json(json_arr).isJSONMap();
    }

    @Test
    public void testJsonSizeIs() {
        want.json(json_arr).isJSONArray().sizeEq(10);
    }

    @Test
    public void testKeyValueMatch() {
        want.json(json_arr).isJSONArray()
                .propertyMatch("employeeId", the.collection().hasAllItems("1234", (String) null));
    }

    String json_obj = "{'gmtModified':'2009-06-19','employeeId':'1234','loginId':'system','phone':null,'gmtCreate':'2008-02-26'}";

    @Test
    public void testHasKeyValues() {
        want.json(json_obj).isJSONMap().propertyEq("employeeId", "1234").propertyEq("phone", null);
    }

    @Test
    public void testKeyValueEq() {
        want.json(json_arr).isJSONArray()
                .propertyEq("employeeId", Arrays.asList("1234", null, null, null, null, null, null, null, null, null));
    }
}
