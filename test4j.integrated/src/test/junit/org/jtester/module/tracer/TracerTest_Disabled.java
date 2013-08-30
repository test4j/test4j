package org.jtester.module.tracer;

import java.io.FileNotFoundException;

import mockit.Mock;

import org.jtester.database.table.ITable;
import org.jtester.fortest.hibernate.User;
import org.jtester.fortest.hibernate.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringBeanByType;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.tools.commons.ResourceHelper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
@SuppressWarnings({ "serial" })
@SpringContext({ "classpath:/org/jtester/fortest/hibernate/project.xml" })
public class TracerTest_Disabled implements JTester {
    @SpringBeanByType
    private UserService userService;

    @Before
    public void disabledTracer() {
        new MockUp<TracerHelper>() {
            @Mock
            public boolean doesTracerEnabled() {
                return false;
            }
        };
    }

    @Test
    public void monitorSpringFalse() {
        db.table(ITable.t_tdd_user).clean().insert(3, new DataMap() {
            {
                this.put("id", "1", "2", "3");
                this.put("is_deleted", "false", "false", "true");
            }
        });
        User user1 = userService.getUser(1);
        want.object(user1).notNull();

        User user = new User();
        user.setName("new user");
        userService.newUser(user);
        db.table(ITable.t_tdd_user).queryWhere("name='new user'").reflectionEqMap(1, new DataMap() {
            {
                this.put("count", "1");
            }
        });
    }

    @Test
    // (dependsOnMethods = "monitorSpringFalse")
    public void monitorSpringFalse_check() throws FileNotFoundException {
        String tracerInfo = ResourceHelper
                .readFromFile("target/tracer/org/jtester/tracer/TracerTest#monitorSpringFalse.html");
        want.string(tracerInfo).notContain("paras").notContain("result");
    }

    @Test
    public void monitorJdbcFalse() {
        db.table(ITable.t_tdd_user).clean().insert(3, new DataMap() {
            {
                this.put("id", "1", "2", "3");
                this.put("is_deleted", "false", "false", "true");
            }
        });
        User user1 = userService.getUser(1);
        want.object(user1).notNull();

        User user = new User();
        user.setName("new user");
        userService.newUser(user);
        db.table(ITable.t_tdd_user).queryWhere("name='new user'").reflectionEqMap(1, new DataMap() {
            {
                this.put("count", "1");
            }
        });
    }

    @Test
    // (dependsOnMethods = "monitorSpringFalse")
    public void monitorJdbcFalse_check() throws FileNotFoundException {
        String tracerInfo = ResourceHelper
                .readFromFile("target/tracer/org/jtester/tracer/TracerTest#monitorJdbcFalse.html");
        want.string(tracerInfo).notContain("SQL-Statement");
    }
}
