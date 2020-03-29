package org.test4j.junit4;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.example.mix.ServiceMix;
import org.test4j.example.spring.SpringConfig;
import org.test4j.module.spec.IStory;
import org.test4j.module.spec.annotations.Mix;

@ContextConfiguration(classes = SpringConfig.class)
public class StoryTestDemo extends Test4J implements IStory {
    @Mix
    private ServiceMix serviceMix;

    @Test
    public void test() {
        story.scenario("story测试场景演示")
                .given("前置条件", () -> serviceMix.do_given_method("my condition"))
                .when("执行测试", () -> System.out.println("执行测试......"))
                .then("后置验证", () -> serviceMix.do_then_method("hello, spec"))
        ;
    }
}
