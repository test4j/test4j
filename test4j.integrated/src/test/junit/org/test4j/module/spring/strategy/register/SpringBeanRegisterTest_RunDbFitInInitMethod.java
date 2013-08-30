package org.test4j.module.spring.strategy.register;

import org.junit.Test;
import org.test4j.database.table.ITable;
import org.test4j.fortest.service.UserService;
import org.test4j.fortest.service.UserServiceImpl;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.AutoBeanInject.BeanMap;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.SpringInitMethod;

@SuppressWarnings("serial")
@SpringContext({ "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
@AutoBeanInject(maps = { @BeanMap(intf = "**.*Service", impl = "**.*ServiceImpl"),
        @BeanMap(intf = "**.*Dao", impl = "**.*DaoImpl") })
public class SpringBeanRegisterTest_RunDbFitInInitMethod implements Test4J {
    @SpringBeanByName(claz = UserServiceImplEx.class)
    UserService userService;

    /**
     * 测试在init方法中执行dbfit文件
     */
    @Test
    public void paySalary() {
        double total = this.userService.paySalary("310010");
        want.number(total).isEqualTo(3100d);
    }

    public static class UserServiceImplEx extends UserServiceImpl {
        @SpringInitMethod
        public void initDbFit() {
            db.table(ITable.t_tdd_user).clean().insert(3, new DataMap() {
                {
                    this.put("id", "1", "2", "3");
                    this.put("sarary", "1000.0", "2100.0", "1000.0");
                    this.put("post_code", "310010", "310010", "310012");
                }
            }).commit();
        }
    }
}
