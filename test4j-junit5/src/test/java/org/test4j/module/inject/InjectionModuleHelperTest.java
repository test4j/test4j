package org.test4j.module.inject;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.model.User;

public class InjectionModuleHelperTest extends Test4J {
    User user = new User();

    @Inject(targets = "user", properties = "first")
    private final String first = "test user";

    @Test
    public void testInjectInto() throws Exception {
        want.object(user).eqByProperties("first", "test user");
    }
}
