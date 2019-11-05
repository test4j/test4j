package org.test4j.junit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.example.spring.ServiceA;
import org.test4j.example.spring.SpringConfig;

/**
 * 使用spring容器测试简单示例
 */
@ContextConfiguration(classes = {SpringConfig.class})
public class SpringTestDemo extends Test4J {
    @Autowired
    private ServiceA serviceA;

    @Test
    public void test() {
        String result = this.serviceA.say("welcome to spring test");
        want.string(result).eq("welcome to spring test");
    }
}
