package org.test4j.hamcrest;

import org.junit.jupiter.api.Test;

import org.test4j.junit5.Test4J;
import org.test4j.model.Address;
import org.test4j.model.User;


public class TestPropertyAssert extends Test4J {
    @Test
    public void assertObject() {
        User user = new User(1, "wu", "darui");
        user.setAddress(new Address("天堂路998号", "310012", "xxxxxx滨江大楼"));

        User actualUser = yourApi();
        want.object(actualUser).eqReflect(user);
        want.object(actualUser).propertyEq("first", "wu").propertyEq("address.postcode", "310012")
                .propertyMatch("last", the.string().isEqualTo("darui"));

        want.object(actualUser).propertyEq(new String[]{"first", "last", "address.postcode"},
                new String[]{"wu", "darui", "310012"});
    }

    public static User yourApi() {
        User user = new User(1, "wu", "darui");
        user.setAddress(new Address("天堂路998号", "310012", "xxxxxx滨江大楼"));
        return user;
    }
}
