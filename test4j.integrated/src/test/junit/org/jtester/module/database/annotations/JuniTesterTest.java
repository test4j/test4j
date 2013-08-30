package org.jtester.module.database.annotations;

import java.util.List;

import org.jtester.database.table.ITable;
import org.jtester.database.table.TddUserTable;
import org.jtester.fortest.beans.User;
import org.jtester.fortest.service.ResourceLoader;
import org.jtester.fortest.service.UserService;
import org.jtester.junit.JTester;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringInitMethod;
import org.junit.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml",
        "org/jtester/module/spring/testedbeans/xml/load-data-init.xml" })
@AutoBeanInject
@SuppressWarnings("serial")
public class JuniTesterTest implements JTester {

    @SpringBeanByName(claz = ResourceLoaderEx.class)
    ResourceLoader resourceLoader;

    @SpringBeanByName
    UserService    userService;

    /**
     * 测试程序中准备初始的数据供spring加载时使用
     */
    @Test
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
            db.table(ITable.t_tdd_user).clean().insert(2, new TddUserTable() {
                {
                    this.put(IColumn.f_id, "1", "2");
                    this.put(IColumn.f_sarary, "32", "34");
                }
            }).commit();
            super.init();
        }
    }
}
