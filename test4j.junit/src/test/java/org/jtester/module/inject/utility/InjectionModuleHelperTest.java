package org.jtester.module.inject.utility;

import org.jtester.fortest.beans.User;
import org.jtester.junit.JTester;
import org.junit.Test;
import org.test4j.module.inject.annotations.Inject;

public class InjectionModuleHelperTest implements JTester {
    User           user  = new User();

    @Inject(targets = "user", properties = "first")
    private String first = "test user";

    @Test
    public void testInjectInto() throws Exception {
        want.object(user).propertyEq("first", "test user");
    }
}
