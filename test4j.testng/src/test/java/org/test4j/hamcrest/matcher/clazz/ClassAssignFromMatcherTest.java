package org.test4j.hamcrest.matcher.clazz;

import mockit.Mocked;

import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j", "assertion" })
public class ClassAssignFromMatcherTest extends Test4J {

    public void testClassAssignFromMatcher() {
        want.object(new B()).clazIsSubFrom(A.class);
    }

    @Mocked
    A a;

    @Test(description = "测试wanted(Class)中传入的参数是null时，断言是正确的。")
    public void testClassAssignFromMatcher_wanted() {
        new Expectations() {
            {
                a.say(the.string().wanted(String.class));
                result = "mock";
            }
        };
        String result = a.say(null);
        want.string(result).isEqualTo("mock");
    }

    public static class A {
        public String say(String name) {
            return "nothing";
        }
    }

    public static class B extends A {

    }
}
