package org.jtester.module.spring;

import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext
@AutoBeanInject
public class SpringBeanRegisterTest_Constructor2 implements JTester {

    @SpringBeanByName
    OuterClass outer;

    /**
     * Claz没有默认构造函数
     */
    @Test
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
