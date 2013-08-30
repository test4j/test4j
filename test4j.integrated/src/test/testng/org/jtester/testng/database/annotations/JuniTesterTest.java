package org.jtester.testng.database.annotations;

import java.util.List;

import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringInitMethod;
import org.test4j.database.table.ITable;
import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.ResourceLoader;
import org.test4j.fortest.service.UserService;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.IDatabase;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml",
        "org/jtester/module/spring/testedbeans/xml/load-data-init.xml" })
@AutoBeanInject
@SuppressWarnings("serial")
public class JuniTesterTest extends JTester implements IDatabase {

    @SpringBeanByName(claz = ResourceLoaderEx.class)
    ResourceLoader resourceLoader;

    @SpringBeanByName
    UserService    userService;

    /**
     * 测试程序中准备初始的数据供spring加载时使用
     */
    public void testInitMethod() {
        List<String> users = resourceLoader.getUsers();
        want.collection(users).notNull().sizeEq(2);
    }

    @Test
    public void testInitMethod2() {
        List<User> users = this.userService.findAllUser();
        want.collection(users).sizeIs(2);
    }

    public static class ResourceLoaderEx extends ResourceLoader {
        @Override
        @SpringInitMethod
        public void init() throws Exception {
            MessageHelper.info("readyDb");
            db.table(ITable.t_tdd_user).clean().insert(2, new DataMap() {
                {
                    this.put("id", "1", "2");
                    this.put("sarary", "32", "34");
                }
            }).commit();

            super.init();
        }
    }
}
