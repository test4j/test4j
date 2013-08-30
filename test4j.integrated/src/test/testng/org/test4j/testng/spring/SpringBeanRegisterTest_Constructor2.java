package org.test4j.testng.spring;

import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext
@AutoBeanInject
@Test(groups = "test4j")
public class SpringBeanRegisterTest_Constructor2 extends Test4J {

    @SpringBeanByName
    OuterClass outer;

    /**
     * Claz没有默认构造函数
     */
    public void test_ClazNoDefaultConstructor() {
        Object inner1 = outer.getInner();
        want.object(inner1).isNull();

        Object inner2 = outer.getInner2();
        want.object(inner2).notNull();
    }

    public static class OuterClass {
        InnerClazz  inner;

        InnerClazz2 inner2;

        public void setInner(InnerClazz inner) {
            this.inner = inner;
        }

        public InnerClazz getInner() {
            return inner;
        }

        public InnerClazz2 getInner2() {
            return inner2;
        }

        public void setInner2(InnerClazz2 inner2) {
            this.inner2 = inner2;
        }
    }

    public static class InnerClazz {
        public InnerClazz(String value) {

        }
    }

    public static class InnerClazz2 {

    }
}
