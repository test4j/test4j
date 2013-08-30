package org.jtester.module.spring;

import mockit.NonStrict;

import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext
@AutoBeanInject
public class SpringTestedContextTest implements JTester {
    @SpringBeanByName(init = "init")
    TestedService testedService;

    @SpringBeanFrom
    @NonStrict
    TestedDao     testedDao;

    @Test
    public void testSetContext() {
        System.out.println("");
        new Expectations() {
            {
                testedDao.sayNo();
                result = "mock";
            }
        };
        String word = this.testedService.sayNo();
        want.string(word).isNull();
    }
}

class TestedService {
    private TestedDao testedDao;

    public TestedDao getTestedDao() {
        return testedDao;
    }

    public void setTestedDao(TestedDao testedDao) {
        this.testedDao = testedDao;
    }

    public String sayNo() {
        return word;
    }

    private String word;

    public void init() {
        try {
            this.word = this.testedDao.sayNo();
            throw new RuntimeException("在before class时，jmockit应该还没有初始化化mock字段(jmockit-0.999.13)！");
        } catch (NullPointerException ne) {
        }
    }
}

class TestedDao {
    public String sayNo() {
        return "no";
    }
}
